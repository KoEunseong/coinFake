package com.example.coinfake

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.coinfake.dataModel.CurrentPriceResult
import com.example.coinfake.dataModel.ownCoinModel
import com.example.coinfake.utils.FBAuth
import com.example.coinfake.utils.FBRef
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CoinInside : AppCompatActivity() {
    private var TAG = CoinInside::class.java.simpleName
    private var thread: Thread? = null
    private lateinit var lineChart : LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_inside)
//        setChart()
        intent = intent
        var coinInfo : CurrentPriceResult = intent.getSerializableExtra("item") as CurrentPriceResult
        var coinCount = 0.0
        Log.d("CoinInside",coinInfo.toString())
        var coinName = coinInfo.coinName
        var coinPrice = coinInfo.coinInfo.closing_price
        var increse = (coinInfo.coinInfo.closing_price.toDouble() /  coinInfo.coinInfo.opening_price.toDouble() - 1.0) * 100.0
        if(increse > 0.0){
            findViewById<TextView>(R.id.upDown).setTextColor(Color.parseColor("#FF0000"))
        }
        else if(increse < 0.0){
            findViewById<TextView>(R.id.upDown).setTextColor(Color.parseColor("#0000FF"))
        }
        findViewById<TextView>(R.id.upDown).text = String.format("%.2f",increse) + "%"
        findViewById<TextView>(R.id.coinTitle).text = coinName
        findViewById<TextView>(R.id.coinName).text = coinName
        findViewById<TextView>(R.id.Price).text = coinPrice


        var isBookmarked : Boolean = false
        val bookmarkPostListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for(dataModel in dataSnapshot.children){
                    if(coinName.equals(dataModel.key.toString())){
                        //Log.d("favorite",dataModel.key.toString())
                        isBookmarked = true
                        setBookmark(isBookmarked, coinInfo)
                        break
                    }
                }
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.favoriteRef.child(FBAuth.getUid()).addValueEventListener(bookmarkPostListener)
        setBookmark(isBookmarked, coinInfo)

        FBRef.ownCoinsRef.child(FBAuth.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
//                    Log.d(TAG, dataModel.getValue(Coin::class.java)!!.count)
                    if(dataModel.key == coinName){

                        coinCount = dataModel.getValue(ownCoinModel::class.java)!!.count
                        findViewById<TextView>(R.id.coin_count).text = coinCount.toString()
                    }
                }

//                findViewById<TextView>(R.id.coin_count).text =
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        findViewById<Button>(R.id.buyBtn).setOnClickListener {
            var intent = Intent(this,BuyActivity::class.java)
            intent.putExtra("item",coinInfo)
            startActivity(intent)
        }

        findViewById<Button>(R.id.sellBtn).setOnClickListener {
            if(coinCount == 0.0){
                Toast.makeText(this,"해당 코인을 보유하고 있지 않습니다. ",Toast.LENGTH_SHORT).show()
            }
            else{
                var intent = Intent(this,SellActivity::class.java)
                intent.putExtra("item",coinInfo)
                intent.putExtra("coinCount",coinCount)
                startActivity(intent)
            }

        }



    }

    private fun setBookmark(isbookmarked: Boolean , key : CurrentPriceResult) {
        var bookmarked = isbookmarked
        if(bookmarked == true) {
            findViewById<ImageView>(R.id.favoriteBtn).setImageResource(R.drawable.bookmark_color)
            findViewById<ImageView>(R.id.favoriteBtn).setOnClickListener {
                setBookmark(!bookmarked, key)
                Toast.makeText(this, "관심 해제되었습니다.", Toast.LENGTH_SHORT).show()
                FBRef.favoriteRef.child(FBAuth.getUid()).child(key.coinName).removeValue()
            }
        }
        else {
            findViewById<ImageView>(R.id.favoriteBtn).setImageResource(R.drawable.bookmark_white)
            findViewById<ImageView>(R.id.favoriteBtn).setOnClickListener {
                setBookmark(!bookmarked, key)
                Toast.makeText(this, "관심 등록되었습니다.", Toast.LENGTH_SHORT).show()
                FBRef.favoriteRef.child(FBAuth.getUid()).child(key.coinName).setValue(key)
            }
        }

    }
    private fun setChart() {
//        lineChart = findViewById(R.id.lineChart)

        val xAxis: XAxis = lineChart.xAxis



        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 15f
            setDrawGridLines(false)
            setDrawLabels(true)
            isGranularityEnabled = true
            granularity = 1f  // x 축 격자가 몇 분 단위로 표현되는지 설정
            axisMinimum = -9f // x 축 격자 왼쪽 끝을 00:00 으로 설정
            axisMaximum = 15f // x 축 격자 오른쪽 끝을 24:00 으로 설정
            valueFormatter = TimeAxisValueFormat()
            setLabelCount(6,false)

        }
        lineChart.apply {
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 100f // y축 상한선 <-최고가 + 최고가의 10% 정도 주면 적당할듯?

            legend.apply {
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
                isEnabled = true
            }
        }
        val lineData = LineData()
        lineChart.data = lineData

        addEntry(-3f,10f)
        addEntry(-2f,5f)
        addEntry(-1f, 40f)
        addEntry(0f,20f)
        addEntry(1f,30f)
        //date 가 0f 면 면 09:00 시, 1f 는 한시간 단위로
    }

    private fun addEntry( date: Float, temp : Float) {
        val data: LineData = lineChart.data


        data?.let {
            var set : ILineDataSet? = data.getDataSetByIndex(0)

            if(set == null) {
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(Entry(date, temp),0)
            data.notifyDataChanged()
            lineChart.apply{
                notifyDataSetChanged()
                moveViewToX(data.entryCount.toFloat())
                setVisibleXRangeMaximum(6f)
                setPinchZoom(true)
                isDoubleTapToZoomEnabled = true
                description.text = ""
                setBackgroundColor(Color.WHITE)
                description.textSize = 15f
                setExtraOffsets(8f, 16f,8f, 16f)
            }
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "")       // label 입력하면 상단에 코인명 뜸
        set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = Color.BLACK
            setCircleColor(Color.DKGRAY)
            valueTextSize = 15f
            lineWidth = 2f
            circleRadius = 3f
            fillAlpha = 0
            fillColor = Color.DKGRAY
            setDrawValues(true)

        }
        return set
    }
}