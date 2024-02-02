package com.example.coinfake.repository

import com.example.coinfake.network.Api
import com.example.coinfake.network.RetrofitInstance

class NetworkRepository {
    private val client = RetrofitInstance.getInstance().create(Api::class.java)

    suspend fun getCurrentCoinList() = client.getCurrentCoinList()

    suspend fun getHourCoinList() = client.get24hoursCoinData()
}