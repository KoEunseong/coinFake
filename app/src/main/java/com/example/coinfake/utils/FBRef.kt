package com.example.coinfake.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object{
        val database = Firebase.database
        val personalInfoRef = database.getReference("personal_info")
        val favoriteRef = database.getReference("favorite")
        val ownCoinsRef = database.getReference("ownCoin")
    }
}