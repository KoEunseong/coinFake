package com.example.coinfake

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var logout = findViewById<ImageView>(R.id.logoutBtn)
        logout.setOnClickListener {
            var dlg = AlertDialog.Builder(this)
            dlg.setTitle("로그아웃 하시겠습니까?")
            dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                Firebase.auth.signOut()

                var intent = Intent(this, IntroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
            dlg.setNegativeButton("취소",null)
            dlg.show()
        }


    }
}