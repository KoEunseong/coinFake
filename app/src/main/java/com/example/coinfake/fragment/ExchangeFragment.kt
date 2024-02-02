package com.example.coinfake.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinfake.CoinAdapter
import com.example.coinfake.ExchangeViewModel
import com.example.coinfake.R
import com.example.coinfake.dataModel.CurrentPrice
import com.example.coinfake.dataModel.CurrentPriceResult
import com.example.coinfake.databinding.FragmentExchangeBinding
import com.example.coinfake.utils.FBAuth
import java.util.*
import kotlin.collections.ArrayList


class ExchangeFragment : Fragment() {
    private lateinit var binding : FragmentExchangeBinding
    private val viewModel : ExchangeViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var mList : List<CurrentPriceResult>
    private lateinit var adapter: CoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exchange, container, false)
        recyclerView = binding.recyclerView
        searchView = binding.searchView
        viewModel.getCurrentCoinList()

        viewModel.currentPriceResult.observe(viewLifecycleOwner, Observer {
//            Log.d("Exchange",it.toString())
            mList = it
            FBAuth.coinList = it
            Log.d("Exchange",FBAuth.coinList.toString())
            adapter = CoinAdapter(requireContext() , mList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        })



        recyclerView.setHasFixedSize(true)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        binding.favoriteTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_exchangeFragment_to_favoriteFragment)
        }
        binding.assetTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_exchangeFragment_to_myAssetFragment)
        }
        binding.rankgingTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_exchangeFragment_to_rankingFragment)
        }
        return binding.root
    }

    private fun filterList(query : String?) {
        if (query != null) {
            val filteredList = ArrayList<CurrentPriceResult>()
            for (i in mList) {
                if (i.coinName.contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "데이터를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }else{
                adapter.setFilteredList(filteredList)
            }

        }
    }

}