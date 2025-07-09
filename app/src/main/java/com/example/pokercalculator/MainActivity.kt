package com.example.pokercalculator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections
import kotlin.math.abs
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private val people = mutableListOf<String>()
    private val cost = mutableListOf<Double>()
    private val rebuy = mutableListOf<Int>()
    private var useRebuy = true  // Domyślnie używamy rebuy
    private var rebuyValue = 50

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter

    @SuppressLint("NotifyDataSetChanged", "UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val switchRebuy = findViewById<Switch>(R.id.switchRebuy)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)
        val editRebuyValue = findViewById<EditText>(R.id.editRebuyValue)
        val headerRebuy = findViewById<TextView>(R.id.headerRebuy)

        headerRebuy.visibility = if (switchRebuy.isChecked) View.VISIBLE else View.GONE

        recyclerView = findViewById(R.id.recyclerViewPlayers)
        adapter = PlayerAdapter(people, cost, rebuy, useRebuy)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString().trim()

            if (name.isNotEmpty()) {
                people.add(name)
                cost.add(0.0)  // Automatycznie ustawiamy 0.0 dla wyniku
                rebuy.add(0)   // Automatycznie ustawiamy 0 dla rebuy
                adapter.notifyDataSetChanged()
                editTextName.text.clear()
            }
        }

        switchRebuy.setOnCheckedChangeListener { _, isChecked ->
            useRebuy = isChecked
            adapter.setUseRebuy(useRebuy)
            editRebuyValue.visibility = if (useRebuy) View.VISIBLE else View.GONE
            headerRebuy.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        editRebuyValue.setText(rebuyValue.toString())

        editRebuyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                // Spróbuj sparsować wpisaną liczbę; jeśli nie da się – zostaw starą wartość
                val newValue = s.toString().toIntOrNull()
                if (newValue != null) {
                    rebuyValue = newValue
                    Toast.makeText(
                        this@MainActivity,
                        "Ustawiono rebuy na $rebuyValue",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        buttonCalculate.setOnClickListener {
            // 1) Zrób kopię kosztów — oryginał pozostaje nietknięty
            val workingCosts = cost.toMutableList()

            // 2) Jeśli rebuy włączony, to dopiero na kopii odejmij opłaty
            if (useRebuy) {
                for (i in workingCosts.indices) {
                    workingCosts[i] = workingCosts[i] - rebuyValue - rebuy[i] * rebuyValue
                }
            }

            // 3) Sprawdź sumę na kopii
            val message = checkValues(workingCosts)
            if (message.isNotEmpty()) {
                val parts = message.split(" ")
                if(parts[2] == "brakuje")
                    textViewResult.setTextColor(Color.RED)
                else
                    textViewResult.setTextColor(Color.GREEN)
                textViewResult.text = message
            } else {
                // 4) Przelicz przelewy na kopii
                val tempTransfers = calculateTransfers(workingCosts)
                if(tempTransfers[0] == "Nie ma graczy")
                    textViewResult.setTextColor(Color.RED)
                else
                    textViewResult.setTextColor(Color.WHITE)
                textViewResult.text = tempTransfers.joinToString("\n")
            }
        }

        textViewResult.setOnLongClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Szczegóły przelewów", textViewResult.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Skopiowane do schowka", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun calculateTransfers(costList: MutableList<Double>): List<String> {
        val result = mutableListOf<String>()
        val localCost = costList.toMutableList()
        val localPeople = people.toMutableList()

        if(localPeople.isEmpty()) {
            result.add("Nie ma graczy")
            return result
        }

        while (true) {
            // 1) sortuj malejąco według localCost
            for (i in localCost.indices) {
                for (j in 0 until localCost.size - i - 1) {
                    if (localCost[j] < localCost[j + 1]) {
                        Collections.swap(localCost, j, j + 1)
                        Collections.swap(localPeople, j, j + 1)
                    }
                }
            }
            // 2) połóż skrajną parę
            val top = localCost[0]
            val bottom = localCost[localCost.size - 1]
            if (top + bottom >= 0) {
                // nadpłata nad długiego
                result.add("${localPeople.last()} daje ${"%.2f".format(-bottom)}zł dla ${localPeople.first()}")
                localCost[0] = top + bottom
                localCost[localCost.size - 1] = 0.0
            } else {
                // dług przewyższa nadpłatę
                result.add("${localPeople.last()} daje ${"%.2f".format(top)}zł dla ${localPeople.first()}")
                localCost[0] = 0.0
                localCost[localCost.size - 1] = top + bottom
            }
            // 3) sprawdź, czy wszystkie są zerowe
            if (localCost.all { it == 0.0 }) break
        }
        return result
    }

    private fun checkValues(list: List<Double>): String {
        var sum = 0.0
        for (value in list) {
            sum += value
        }

        val deviation = abs(sum)
        return if (deviation < 0.01) { // Minimalna tolerancja na błędy zmiennoprzecinkowe
            ""
        } else {
            if (sum > 0) {
                "Niepoprawny Wynik, brakuje ${"%.2f".format(deviation)}zł."
            } else {
                "Niepoprawny Wynik, jest ponad ${"%.2f".format(deviation)}zł."
            }
        }
    }
}
