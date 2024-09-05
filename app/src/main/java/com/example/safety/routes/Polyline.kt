package com.example.safety.routes

import com.google.gson.annotations.SerializedName

data class Polyline(
    @SerializedName("points") val points: String
)
