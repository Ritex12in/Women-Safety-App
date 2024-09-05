package com.example.safety.model

data class Comment(
    val id:String = "",
    val name:String = "",
    val geoCode:String = "",
    val comment:String = "",
    val safetyLevel:Int = 0,
    val lat:Double = 0.0,
    val lng:Double = 0.0,
    val date:String = "",
    val time:String = ""
)
