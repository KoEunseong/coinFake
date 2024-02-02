package com.example.coinfake.network

import com.example.coinfake.dataModel.HourCoinDataResult
import com.example.coinfake.network.model.CurrentPriceList
import retrofit2.http.GET

interface Api {

    @GET("public/ticker/ALL_KRW")
    suspend fun getCurrentCoinList() : CurrentPriceList

    @GET("public/candlestick/BTC_KRW/1h")
    suspend fun get24hoursCoinData() : HourCoinDataResult



}