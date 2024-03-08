package com.example.pokercalculator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val people = mutableListOf<String>()
    private val cost = mutableListOf<Double>()
    private val transferList = mutableListOf<String>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextBalance = findViewById<EditText>(R.id.editTextBalance)
        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        recyclerView = findViewById(R.id.recyclerViewPlayers)
        adapter = PlayerAdapter(people, cost)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val balanceText = editTextBalance.text.toString().trim()

            if (name.isNotEmpty() && balanceText.isNotEmpty()) {
                val balance = balanceText.toDoubleOrNull()
                if (balance != null) {
                    people.add(name)
                    cost.add(balance)
                    adapter.notifyDataSetChanged()
                    editTextName.text.clear()
                    editTextBalance.text.clear()
                } else {
                    editTextBalance.error = "Niepoprawny Wynik"
                }
            }
        }

        buttonCalculate.setOnClickListener {
            val message = checkValues(cost)
            if (message.isNotEmpty()) {
                textViewResult.text = message
            } else {
                calculateTransfers()
                val result = StringBuilder()
                for (transfer in transferList) {
                    result.append(transfer).append("\n")
                }
                textViewResult.text = result.toString()
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

    private fun calculateTransfers() {
        transferList.clear()
        var loop = true

        while (loop) {
            sort()
            val tmp = cost[0] + cost[cost.size - 1]

            if (tmp >= 0) {
                cost[0] = tmp
                transferList.add("${people[people.size - 1]} daje ${"%.2f".format(-cost[cost.size - 1])}zł dla ${people[0]}")
                cost[cost.size - 1] = 0.0
            } else if (tmp < 0) {
                transferList.add("${people[people.size - 1]} daje ${"%.2f".format(cost[0])}zł dla ${people[0]}")
                cost[0] = 0.0
                cost[cost.size - 1] = tmp
            }

            loop = false
            for (i in 0 until cost.size - 1) {
                if (cost[i] != 0.0) {
                    loop = true
                    break
                }
            }
        }
    }

    private fun sort() {
        for (i in 0 until cost.size - 1) {
            for (j in 0 until cost.size - i - 1) {
                if (cost[j] < cost[j + 1]) {
                    Collections.swap(cost, j, j + 1)
                    Collections.swap(people, j, j + 1)
                }
            }
        }
    }

    private fun checkValues(list: List<Double>): String {
        var sum = 0.0
        for (value in list) {
            sum += value
        }

        val deviation = abs(sum)
        return if (sum == 0.0) {
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
