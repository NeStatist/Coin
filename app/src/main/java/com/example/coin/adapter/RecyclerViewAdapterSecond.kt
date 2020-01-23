package com.example.coin.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.R
import com.example.coin.net.Crypto
import java.util.ArrayList

class RecyclerViewAdapterSecond : RecyclerView.Adapter<RecyclerViewAdapterSecond.ViewHolder>() {
    private var crypto: Crypto? = null
    private var marketList: List<Crypto.Market> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_layout2, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = marketList[position]
        holder.txtMarket.text = market.market
        holder.txtVolume.text = "$" + String.format("%.2f", market.volume!!.toDouble())
        holder.txtPrice.text = "$" + String.format("%.2f", market.price!!.toDouble())
        holder.txtChange.text = "" + String.format("%.2f", crypto?.ticker?.change!!.toDouble())
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    fun setData(data: Crypto) {
        crypto = data
        marketList = crypto?.ticker?.markets ?: listOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtMarket: TextView
        var txtPrice: TextView
        var txtVolume: TextView
        var txtChange: TextView
        var cardView: CardView

        init {
            txtMarket = view.findViewById(R.id.txtMarket)
            txtPrice = view.findViewById(R.id.txtPrice)
            txtVolume = view.findViewById(R.id.txtVolume)
            txtChange = view.findViewById(R.id.txtChange)
            cardView = view.findViewById(R.id.cardView)

        }
    }

    init {
        marketList = ArrayList()
    }

}
