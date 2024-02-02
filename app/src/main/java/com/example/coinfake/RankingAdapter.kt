package com.example.coinfake

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coinfake.dataModel.InfoModel
import com.example.coinfake.dataModel.RankingModel
import com.example.coinfake.dataModel.ownCoinModel
import com.example.coinfake.utils.FBAuth
import com.example.coinfake.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class RankingAdapter(private val userList: List<RankingModel>) : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        Log.d("adf",userList.toString())
        holder.bind(user, position)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankTextView: TextView = itemView.findViewById(R.id.rankTextView)
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val balanceTextView: TextView = itemView.findViewById(R.id.balanceTextView)


        fun bind(user: RankingModel, position: Int) {


            rankTextView.text = (position + 1).toString()
            usernameTextView.text = user.userName
            balanceTextView.text = user.userTotalAsset.toString()
        }
    }
}