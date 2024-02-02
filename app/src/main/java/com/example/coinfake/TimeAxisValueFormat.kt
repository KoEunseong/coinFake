package com.example.coinfake

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimeAxisValueFormat : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {


        var valueToMinutes = TimeUnit.HOURS.toMillis(value.toLong()) // 조작 할것이 시간, HOURS 를 MINUTES 로 바꾸면 1f 당 1분 변경
        var timeMinutes = Date(valueToMinutes)
        var formatMinutes = SimpleDateFormat("HH:mm")
        return formatMinutes.format(timeMinutes)
    }
}