package com.example.pokercalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(
    private val players: MutableList<String>,
    private val balances: MutableList<Double>,
    private val rebuys: MutableList<Int>,
    private var useRebuy: Boolean  // Nowa zmienna do kontrolowania trybu
) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position], balances[position], rebuys[position])
    }

    override fun getItemCount(): Int {
        return players.size
    }

    fun setUseRebuy(newUseRebuy: Boolean) {
        if (useRebuy != newUseRebuy) {
            useRebuy = newUseRebuy
            notifyItemRangeChanged(0, players.size) // Odśwież tylko istniejące elementy
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val editTextPlayerName: EditText = itemView.findViewById(R.id.editTextPlayerName)
        private val editTextPlayerBalance: EditText = itemView.findViewById(R.id.editTextPlayerBalance)
        private val editTextPlayerRebuy: EditText = itemView.findViewById(R.id.editTextPlayerRebuy)
        //private val buttonEditPlayer: Button = itemView.findViewById(R.id.buttonEdit)

        init {
//            buttonEditPlayer.setOnClickListener {
//                val playerName = editTextPlayerName.text.toString()
//                val balanceText = editTextPlayerBalance.text.toString()
//                val rebuyText = editTextPlayerRebuy.text.toString()
//                val balance = if (balanceText.isEmpty()) 0.0 else balanceText.toDouble()
//                val rebuy = if (!useRebuy) 0 else if (rebuyText.isEmpty()) 0 else rebuyText.toInt()
//                updatePlayer(adapterPosition, playerName, balance, rebuy)
//            }

            editTextPlayerBalance.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val balanceText = editTextPlayerBalance.text.toString()
                    val balance = if (balanceText.isEmpty()) 0.0 else balanceText.toDouble()
                    balances[adapterPosition] = balance
                }
            }

            editTextPlayerRebuy.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val rebuyText = editTextPlayerRebuy.text.toString()
                    val rebuy = if (rebuyText.isEmpty()) 0 else rebuyText.toInt()
                    rebuys[adapterPosition] = rebuy
                }
            }
        }

        fun bind(player: String, balance: Double, rebuy: Int) {
            editTextPlayerName.setText(player)
            editTextPlayerBalance.setText(balance.toString())

            // Pokazuje lub ukrywa pole rebuy w zależności od trybu
            if (useRebuy) {
                editTextPlayerRebuy.visibility = View.VISIBLE
                editTextPlayerRebuy.setText(rebuy.toString())
            } else {
                editTextPlayerRebuy.visibility = View.GONE
                editTextPlayerRebuy.setText("0") // Ustawienie rebuy na 0
            }
        }
    }

    private fun updatePlayer(position: Int, playerName: String, balance: Double, rebuy: Int) {
        players[position] = playerName
        balances[position] = balance
        rebuys[position] = rebuy
        notifyItemChanged(position)
    }
}
