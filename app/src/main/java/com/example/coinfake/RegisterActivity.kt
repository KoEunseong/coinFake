package com.example.coinfake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.coinfake.dataModel.InfoModel
import com.example.coinfake.databinding.ActivityRegisterBinding
import com.example.coinfake.utils.FBAuth
import com.example.coinfake.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityRegisterBinding
    lateinit var email : String
    var TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.joinBtn.setOnClickListener {
            email = binding.joinEmailArea.text.toString()
            val password = binding.joinPW.text.toString()
            val passwordCheck = binding.joinPWCheck.text.toString()
            val name = binding.joinNameArea.text.toString()
            if(!password.equals(passwordCheck)){
                Toast.makeText(this, "비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            Log.d(TAG, user?.uid.toString())

                            FBRef.personalInfoRef.child(user?.uid.toString()).child("basic_info").setValue(
                                InfoModel(email, name, 5000000.0)
                            )
                            FBAuth.myName = name
                            FBAuth.myEmail = email


                            val intent = Intent(this, IntroActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)

                            Toast.makeText(baseContext, "Authentication failed.",Toast.LENGTH_SHORT,).show()
                        }
                    }
            }
        }



    }
}
