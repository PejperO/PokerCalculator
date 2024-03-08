package com.example.pokercalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(private val players: MutableList<String>, private val balances: MutableList<Double>) :
    RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position], balances[position])
    }

    override fun getItemCount(): Int {
        return players.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val editTextPlayerName: EditText = itemView.findViewById(R.id.editTextPlayerName)
        private val editTextPlayerBalance: EditText = itemView.findViewById(R.id.editTextPlayerBalance)
        private val buttonEditPlayer: Button = itemView.findViewById(R.id.buttonEdit)

        init {
            buttonEditPlayer.setOnClickListener {
                val playerName = editTextPlayerName.text.toString()
                val balanceText = editTextPlayerBalance.text.toString()
                val balance = if (balanceText.isEmpty()) 0.0 else balanceText.toDouble()
                updatePlayer(adapterPosition, playerName, balance)
            }

            editTextPlayerBalance.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val balanceText = editTextPlayerBalance.text.toString()
                    val balance = if (balanceText.isEmpty()) 0.0 else balanceText.toDouble()
                    balances[adapterPosition] = balance
                }
            }
        }

        fun bind(player: String, balance: Double) {
            editTextPlayerName.setText(player)
            //editTextPlayerBalance.setText(String.format("%.2f", balance))
            editTextPlayerBalance.setText(balance.toString())
        }
    }

    private fun updatePlayer(position: Int, playerName: String, balance: Double) {
        players[position] = playerName
        balances[position] = balance
        notifyItemChanged(position)
    }
}
