package com.example.coinfake.utils

import com.example.coinfake.dataModel.CurrentPriceResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object {
        private lateinit var auth: FirebaseAuth
        lateinit var coinList : List<CurrentPriceResult>
        var myName : String = ""
        var myEmail : String = ""

        fun getUid() : String {
            auth = Firebase.auth
            return auth.uid.toString()
        }

        fun getTime() : String {
            val currentDataTime = Calendar.getInstance().time
            val dataFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDataTime)

            return dataFormat
        }

    }
}