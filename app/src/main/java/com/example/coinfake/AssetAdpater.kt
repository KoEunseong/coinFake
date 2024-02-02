package com.example.coinfake

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coinfake.dataModel.CurrentPrice
import com.example.coinfake.dataModel.CurrentPriceResult
import com.example.coinfake.dataModel.ownCoinModel

class AssetAdpater (val context : Context, var mList: List<CurrentPriceResult> , var ownCoinList : List<ownCoinModel>) :
    RecyclerView.Adapter<AssetAdpater.CoinViewHolder>() {

    inner class CoinViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //        val logo: ImageView = itemView.findViewById(R.id.logoIv)
        val titleCoin : TextView = itemView.findViewById(R.id.titleCoin)
        val coinCost = itemView.findViewById<TextView>(R.id.coinCost)
        val increase = itemView.findViewById<TextView>(R.id.increaseRate)

        fun bindItems(item : CurrentPriceResult){
            itemView.setOnClickListener {
                var intent = Intent(itemView.context,CoinInside::class.java)
                intent.putExtra("item", item)
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.asset_item, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
//        holder.logo.setImageResource(mList[position].logo)
        holder.bindItems(mList[position])
        holder.titleCoin.text = ownCoinList[position].count.toString() + " " + ownCoinList[position].coinName
        holder.coinCost.text = (mList[position].coinInfo.closing_price.toDouble() * ownCoinList[position].count).toString()
        var increse = (( mList[position].coinInfo.closing_price.toDouble() / ownCoinList[position].price_at_the_time_of_purchase ) - 1.0) * 100.0
        Log.d("ACTION", mList[position].coinInfo.closing_price.toDouble().toString())
        Log.d("ACTION", ownCoinList[position].price_at_the_time_of_purchase.toString())

        if(increse > 0.0 ){
            holder.coinCost.setTextColor(Color.parseColor("#FF0000"))
        }
        else if(increse < 0.0) {
            holder.coinCost.setTextColor(Color.parseColor("#0000FF"))
        }
        holder.increase.text = String.format("%.2f",increse) + "%"

    }

    override fun getItemCount(): Int {
        return mList.size
    }



}