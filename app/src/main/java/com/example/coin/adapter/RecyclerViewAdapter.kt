package com.example.coin.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.net.Crypto
import com.example.coin.R
import java.util.ArrayList

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private val marketList: MutableList<Crypto.Market>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = marketList[position]
        holder.txtCoin.text = market.coinName
        holder.txtMarket.text = market.market
        holder.txtChange.text = "$" + String.format("%.2f", market.volume!!.toDouble())
        holder.txtPrice.text = "$" + String.format("%.2f", market.price!!.toDouble())
        if (market.coinName.equals("Ethereum", ignoreCase = true)) {
            holder.cardView.setCardBackgroundColor(Color.GRAY)
        } else if(market.coinName.equals("Litecoin", ignoreCase = true)){
            holder.cardView.setCardBackgroundColor(Color.RED)
        }
        else if(market.coinName.equals("Ripple", ignoreCase = true)){
            holder.cardView.setCardBackgroundColor(Color.YELLOW)
        }
        else if(market.coinName.equals("Monero", ignoreCase = true)){
            holder.cardView.setCardBackgroundColor(Color.TRANSPARENT)
        }else {
            holder.cardView.setCardBackgroundColor(Color.GREEN)

        }
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    fun setData(data: List<Crypto.Market>) {
        marketList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtCoin: TextView
        var txtMarket: TextView
        var txtPrice: TextView
        var txtChange: TextView
        var cardView: CardView

        init {
            txtCoin = view.findViewById(R.id.txtCoin)
            txtMarket = view.findViewById(R.id.txtMarket)
            txtPrice = view.findViewById(R.id.txtPrice)
            txtChange = view.findViewById(R.id.txtChange)
            cardView = view.findViewById(R.id.cardView)

        }
    }

    init {
        marketList = ArrayList()
    }
}
