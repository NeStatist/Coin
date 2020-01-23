package com.example.coin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.R
import com.example.coin.net.NameCrypto
import java.util.ArrayList

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val names: MutableList<NameCrypto>
        private var onClick: MyItemClick? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_layout, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtName.text = names[position].name
            holder.itemView.setOnClickListener{onClick?.onClick(names[position])}
        }

    fun setListener(myItemClick: MyItemClick){
        onClick = myItemClick
    }

        override fun getItemCount(): Int {
            return names.size
        }

        fun setData(data: List<NameCrypto>) {
            names.addAll(data)
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var txtName: TextView = view.findViewById(R.id.txtName)
        }

        init {
            names = ArrayList()
        }
    interface MyItemClick {
        fun onClick(code:NameCrypto)
    }
}
