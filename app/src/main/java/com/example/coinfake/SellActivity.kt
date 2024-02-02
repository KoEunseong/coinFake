package com.example.coinfake

import android.app.AlertDialog
import android.content.DialogInterface
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

class SellActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        intent = intent
        var coinInfo : CurrentPriceResult = intent.getSerializableExtra("item") as CurrentPriceResult
        var coinCount : Double = intent.getDoubleExtra("coinCount",0.0)
        var myAsset : Double = 0.0
        var coinCost : Double = coinInfo.coinInfo.closing_price.toString().toDouble()

        FBRef.personalInfoRef.child(FBAuth.getUid()).child("basic_info").child("cash").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myAsset = dataSnapshot.getValue().toString().toDouble()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        findViewById<TextView>(R.id.currentPrice).text = "1 " + coinInfo.coinName + " = " + coinInfo.coinInfo.closing_price.toString() + "원"
        findViewById<TextView>(R.id.myCoinCount).text = "보유 코인 개수 : " + coinCount.toString()
        var buyButton : Button = findViewById<Button>(R.id.sellBtn)
        var buyingArea : EditText = findViewById(R.id.SellingArea)

        buyButton.setOnClickListener {
            var sellCoinCount  = buyingArea.text.toString().toDouble()
            if(sellCoinCount > coinCount){
                Toast.makeText(this, "최대로 팔 수 있는 코인의 개수는 "+ coinCount + "입니다.",Toast.LENGTH_SHORT).show()
            }
            else {
                var dlg = AlertDialog.Builder(this)

                dlg.setTitle("판매 하시겠습니까?")
                dlg.setMessage("판매 가격 : " + sellCoinCount * coinCost + "원 \n남는 코인 : " + (coinCount - sellCoinCount))

                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this,coinInfo.coinName+ " 코인을"+ coinCount + " 개 판매 하였습니다.",Toast.LENGTH_SHORT).show()
                    myAsset += coinCost * sellCoinCount
                    coinCount -= sellCoinCount


                    FBRef.personalInfoRef.child(FBAuth.getUid()).child("basic_info").setValue(
                        InfoModel(FBAuth.myEmail, FBAuth.myName, myAsset)
                    )
                    FBRef.ownCoinsRef.child(FBAuth.getUid()).child(coinInfo.coinName).setValue(
                        ownCoinModel(coinInfo.coinName,coinCount,coinCost)
                    )
                    if(coinCount == 0.0){
                        FBRef.ownCoinsRef.child(FBAuth.getUid()).child(coinInfo.coinName).removeValue()
                    }
//                    var intent = Intent(this, IntroActivity::class.java)
//                    startActivity(intent)
                    finish()//셀하고는 새로고침
                })
                dlg.setNegativeButton("취소",null)
                dlg.show()
            }
        }
    }
}