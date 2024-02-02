package com.example.coinfake.dataModel

data class CurrentPriceResult (
    val coinName : String,
    val coinInfo : CurrentPrice
) : java.io.Serializable