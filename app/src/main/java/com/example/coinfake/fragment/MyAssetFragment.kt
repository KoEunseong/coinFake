package com.example.coinfake.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinfake.AssetAdpater
import com.example.coinfake.CoinAdapter
import com.example.coinfake.R
import com.example.coinfake.dataModel.CurrentPriceResult
import com.example.coinfake.dataModel.ownCoinModel
import com.example.coinfake.databinding.FragmentMyAssetBinding
import com.example.coinfake.utils.FBAuth
import com.example.coinfake.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat

class MyAssetFragment : Fragment() {
    private lateinit var binding : FragmentMyAssetBinding
    private var TAG = MyAssetFragment::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var coinList = arrayListOf<CurrentPriceResult>()
    var ownCoinList = ArrayList<ownCoinModel>()
    var totalAsset : Double = 0.0
    var coinAsset : Double = 0.0
    var initialAsset : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_asset, container, false)

        var ownCoin = binding.coinOwnRV
//        var ownRVAdapter = CoinAdapter(requireContext(),coinList)
        var ownRVAdapter = AssetAdpater(requireContext(),coinList, ownCoinList)
        ownCoin.adapter = ownRVAdapter
        ownCoin.layoutManager = LinearLayoutManager(requireContext())
        totalAsset = 0.0

        FBRef.personalInfoRef.child(FBAuth.getUid()).child("basic_info").child("cash").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                initialAsset = dataSnapshot.getValue().toString().toDouble()
                totalAsset += initialAsset
                binding.cashMoney.text = initialAsset.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        FBRef.ownCoinsRef.child(FBAuth.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                coinList.clear()
                ownCoinList.clear()
                coinAsset = 0.0
                for(dataModel in dataSnapshot.children){
                    for(data in FBAuth.coinList){
//                        Log.d("Favorite1",dataModel.key.toString())
//                        Log.d("Favorite2",data.coinName)

                        if(data.coinName == dataModel.key.toString()){
//                            Log.d("Favorite",dataModel.key.toString())
                            coinList.add(data)
                            ownCoinList.add(dataModel.getValue(ownCoinModel::class.java)!!)
                            coinAsset += data.coinInfo.closing_price.toDouble() * dataModel.getValue(ownCoinModel::class.java)!!.count

                        }
                    }
                }
                binding.coinMoney.text = coinAsset.toString()
                binding.TotalAsset.text = (coinAsset + initialAsset).toString()
                Log.d("TAG",totalAsset.toString())
                ownRVAdapter.notifyDataSetChanged()


            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


//        for (coin in coinList) {
//            initialAsset += coin.coinInfo.opening_price.toDouble() * 500
//        }

        val decimalFormat = DecimalFormat("###,###원")
        val formattedAsset = decimalFormat.format(totalAsset)
        val totalAssetsTextView: TextView = binding.TotalAsset
        totalAssetsTextView.setText(formattedAsset)






        binding.exchangeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_myAssetFragment_to_exchangeFragment)
        }
        binding.favoriteTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_myAssetFragment_to_favoriteFragment)
        }
        binding.rankgingTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_myAssetFragment_to_rankingFragment)
        }
        return binding.root
    }
}