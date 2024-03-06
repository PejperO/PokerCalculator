package com.example.pokercalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val people = mutableListOf<String>()
    private val cost = mutableListOf<Double>()
    private val transferList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextBalance = findViewById<EditText>(R.id.editTextBalance)
        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val balanceText = editTextBalance.text.toString().trim()

            if (name.isNotEmpty() && balanceText.isNotEmpty()) {
                val balance = balanceText.toDoubleOrNull()
                if (balance != null) {
                    people.add(name)
                    cost.add(balance)
                    updateTextView()
                    editTextName.text.clear()
                    editTextBalance.text.clear()
                } else {
                    editTextBalance.error = "Invalid balance"
                }
            }
        }

        buttonCalculate.setOnClickListener {
            val message = checkValues(cost)
            if (message.isNotEmpty()) {
                findViewById<TextView>(R.id.textViewResult).text = message
            } else {
                calculateTransfers()
            }
        }
    }

    private fun calculateTransfers() {
        var loop = true

        while (loop) {
            sort()
            val tmp = cost[0] + cost[cost.size - 1]

            if (tmp >= 0) {
                cost[0] = tmp
                transferList.add("${people[people.size - 1]} daje ${-cost[cost.size - 1]} dla ${people[0]}")
                cost[cost.size - 1] = 0.0
            } else if (tmp < 0) {
                transferList.add("${people[people.size - 1]} daje ${cost[0]} dla ${people[0]}")
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

        // Po obliczeniach wyświetl wyniki
        val result = StringBuilder()
        for (transfer in transferList) {
            result.append(transfer).append("\n")
        }
        findViewById<TextView>(R.id.textViewResult).text = result.toString()
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

    private fun updateTextView() {
        val textViewPlayers = findViewById<TextView>(R.id.textViewPlayers)
        val stringBuilder = StringBuilder()
        for (i in people.indices) {
            stringBuilder.append("${people[i]}: ${"%.2f".format(cost[i])}zł\n")
        }
        textViewPlayers.text = stringBuilder.toString()
    }

    private fun checkValues(list: List<Double>): String {
        var sum = 0.0
        for (value in list) {
            sum += value
        }

        val deviation = Math.abs(sum)
        return if (sum == 0.0) {
            ""
        } else {
            if (sum > 0) {
                "Dane są błędne, brakuje ${"%.2f".format(deviation)} zł."
            } else {
                "Dane są błędne, jest ponad ${"%.2f".format(deviation)} zł."
            }
        }
    }
}
