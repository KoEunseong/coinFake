package com.example.coinfake

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinfake.dataModel.CurrentPrice
import com.example.coinfake.dataModel.CurrentPriceResult
import com.example.coinfake.repository.NetworkRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ExchangeViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()

    private lateinit var currentPriceResultList : ArrayList<CurrentPriceResult>

    // 데이터 변화를 관찰할 livedata
    private val _currentPriceResult = MutableLiveData<List<CurrentPriceResult>>()
    val currentPriceResult : LiveData<List<CurrentPriceResult>>
        get() = _currentPriceResult
    fun getCurrentCoinList() = viewModelScope.launch {

        val result = networkRepository.getCurrentCoinList()

        currentPriceResultList = ArrayList()

        for(coin in result.data){
            try{
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data.get(coin.key))
                val gsonFromJson = gson.fromJson(gsonToJson,CurrentPrice::class.java)

                val currentPriceResult = CurrentPriceResult(coin.key, gsonFromJson)

                //Log.d("ExchangeViewModel",currentPriceResult.toString())
                currentPriceResultList.add(currentPriceResult)
            }
            catch (e : java.lang.Exception){
                Log.d("ExchangeViewModel","end")
            }

        }
        _currentPriceResult.value = currentPriceResultList

    }
}