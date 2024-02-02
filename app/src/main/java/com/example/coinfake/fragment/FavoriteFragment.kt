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
import com.example.coinfake.CoinAdapter
import com.example.coinfake.R
import com.example.coinfake.dataModel.CurrentPrice
import com.example.coinfake.dataModel.CurrentPriceResult
import com.example.coinfake.databinding.FragmentFavoriteBinding
import com.example.coinfake.utils.FBAuth
import com.example.coinfake.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FavoriteFragment : Fragment() {
    private lateinit var binding : FragmentFavoriteBinding
    private lateinit var mList : List<CurrentPriceResult>
    private lateinit var tempList : ArrayList<CurrentPriceResult>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite, container, false )

        var favoriteRV = binding.favoriteCoinRV

        tempList = ArrayList()
        mList = tempList
        var favoriteRVAdapter = CoinAdapter(requireContext(),mList)
        favoriteRV.adapter = favoriteRVAdapter
        favoriteRV.layoutManager = LinearLayoutManager(requireContext())

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                mList = listOf()
                tempList.clear()
                for(dataModel in dataSnapshot.children){
                    for(data in FBAuth.coinList){
//                        Log.d("Favorite1",dataModel.key.toString())
//                        Log.d("Favorite2",data.coinName)

                        if(data.coinName == dataModel.key.toString()){
//                            Log.d("Favorite",dataModel.key.toString())
                            tempList.add(data)
                        }
                    }
                }
                mList = tempList
                favoriteRVAdapter.notifyDataSetChanged()
                Log.d("Favorite!!!",mList.toString())

            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.favoriteRef.child(FBAuth.getUid()).addValueEventListener(postListener)
        favoriteRVAdapter.notifyDataSetChanged()

        binding.exchangeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_favoriteFragment_to_exchangeFragment)
        }
        binding.assetTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_favoriteFragment_to_myAssetFragment)
        }
        binding.rankgingTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_favoriteFragment_to_rankingFragment)
        }

        return binding.root
    }
}