package com.example.coinfake

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.coinfake.dataModel.CurrentPriceResult
import com.example.coinfake.dataModel.InfoModel
import com.example.coinfake.dataModel.ownCoinModel
import com.example.coinfake.utils.FBAuth
import com.example.coinfake.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BuyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying )
        intent = intent
        var coinInfo : CurrentPriceResult = intent.getSerializableExtra("item") as CurrentPriceResult
        var myAsset : Double = 0.0
        var coinCost : Double = coinInfo.coinInfo.closing_price.toString().toDouble()
//        var currentCoinAsset : Double = 0.0

        findViewById<TextView>(R.id.currentPrice).text = "1 " + coinInfo.coinName + " = " + coinInfo.coinInfo.closing_price.toString() + "원"
        var buyButton : Button = findViewById<Button>(R.id.buyBtn)
        var buyingArea : EditText = findViewById(R.id.BuyingArea)
//        FBRef.personalInfoRef.child(FBAuth.getUid()).child("basic_info").child("coinAsset").addValueEventListener(object :
//            ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                currentCoinAsset = dataSnapshot.getValue().toString().toDouble()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
////                Log.w(TAG, "Failed to read value.", error.toException())
//            }
//        })


        FBRef.personalInfoRef.child(FBAuth.getUid()).child("basic_info").child("cash").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                Log.d("hwang",dataSnapshot.getValue().toString())
                myAsset = dataSnapshot.getValue().toString().toDouble()
//                val decimalFormat = DecimalFormat("###,###원")
//                val formattedAsset = decimalFormat.format(initialAsset)
//                val totalAssetsTextView: TextView = binding.TotalAsset
//                totalAssetsTextView.setText(formattedAsset)
                findViewById<TextView>(R.id.myAsset).text = "내자산 : " + myAsset.toString() + " 원"
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


        buyButton.setOnClickListener {
            var coinCount = findViewById<EditText>(R.id.BuyingArea).text.toString().toDouble()
//            buyButton.text = (coinCost * coinCount).toString()

            if(coinCount * coinCost > myAsset){
                Toast.makeText(this,"자산이 부족합니다.",Toast.LENGTH_SHORT).show()
            }
            else {

                var dlg = AlertDialog.Builder(this)

                dlg.setTitle("구매하시겠습니까?")
                dlg.setMessage("구매 가격 : " + coinCount * coinCost + "원 ")
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this,coinInfo.coinName+ " 코인을"+ coinCount +" 개 구매하였습니다.",Toast.LENGTH_SHORT).show()
                    myAsset -= coinCost * coinCount

                    FBRef.personalInfoRef.child(FBAuth.getUid()).child("basic_info").setValue(
                        InfoModel(FBAuth.myEmail, FBAuth.myName, myAsset) // 추가 구매가 지금 구현이 안된 상태입니다.
                    )
                    FBRef.ownCoinsRef.child(FBAuth.getUid()).child(coinInfo.coinName).setValue(
                        ownCoinModel(coinInfo.coinName,coinCount,coinCost)
                    )
//                    var intent = Intent(this, IntroActivity::class.java)
//                    startActivity(intent)
                    finish()
                })
                dlg.setNegativeButton("취소",null)
                dlg.show()
                //여기 다이어로그로 물어보고 사는 가격알려주기

            }
        }
    }
}