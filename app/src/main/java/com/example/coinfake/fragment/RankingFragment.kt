package com.example.coinfake.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinfake.AssetAdpater
import com.example.coinfake.R
import com.example.coinfake.RankingAdapter
import com.example.coinfake.dataModel.InfoModel
import com.example.coinfake.dataModel.RankingModel
import com.example.coinfake.dataModel.ownCoinModel
import com.example.coinfake.databinding.FragmentRankingBinding
import com.example.coinfake.utils.FBAuth
import com.example.coinfake.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class RankingFragment : Fragment() {
    private lateinit var binding : FragmentRankingBinding
    private lateinit var rankingRecyclerView: RecyclerView
    private lateinit var rankingAdapter: RankingAdapter
    private lateinit var rankingList: ArrayList<InfoModel>
    private lateinit var idList: ArrayList<String>

    private var totalrankingList = ArrayList<RankingModel>()
    var coinAsset : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ranking, container, false)
        rankingList = ArrayList()
        idList = ArrayList()
        rankingRecyclerView = binding.rankingRecyclerView
//        var ownRVAdapter = CoinAdapter(requireContext(),coinList)
        var rankingAdapter = RankingAdapter(totalrankingList)


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rankingList.clear()
                idList.clear()
                var num = 0
                for(dataModel in dataSnapshot.children){
                    idList.add(dataModel.key.toString())
                    for(dataUser in dataModel.children){
                        var user = dataUser.getValue(InfoModel::class.java)
                        rankingList.add(user!!)
                    }
                }
//                rankingAdapter.notifyDataSetChanged()
//                Log.d("calm",idList.toString())

//                var num = 0
                for(id in idList){
                    val postListener2 = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            coinAsset = 0.0
                            for(dataModel in dataSnapshot.children){
                                for(data in FBAuth.coinList){
                                    if(data.coinName == dataModel.key.toString()){
//                                        Log.d("calm2",data.coinInfo.toString())
//                                        Log.d("calm2",dataModel.toString())
                                        coinAsset += data.coinInfo.closing_price.toDouble() * dataModel.getValue(
                                            ownCoinModel::class.java)!!.count
                                    }
                                }
                            }
                            var cash = rankingList[num].cash
                            totalrankingList.add(RankingModel(rankingList[num++].name,id,coinAsset + cash))
                            totalrankingList.sortByDescending { it.userTotalAsset }

                            rankingAdapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                        }
                    }
                    FBRef.ownCoinsRef.child(id).addValueEventListener(postListener2)



                }
//                rankingAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.personalInfoRef.addValueEventListener(postListener)



//        favoriteRVAdapter.notifyDataSetChanged()




        rankingRecyclerView.adapter = rankingAdapter
        rankingRecyclerView.layoutManager = LinearLayoutManager(requireContext())



        binding.exchangeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_rankingFragment_to_exchangeFragment)
        }
        binding.favoriteTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_rankingFragment_to_favoriteFragment)
        }
        binding.assetTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_rankingFragment_to_myAssetFragment)
        }
        return binding.root
    }

}