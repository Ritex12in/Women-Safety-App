package com.example.safety.routes

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DirectionClass {

    fun getRoutes(origin:String,destination:String,api:String,callback:RouteCallback){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DirectionsApiService::class.java)

        val call = service.getDirections(origin, destination, api)
        call.enqueue(object : Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                if (response.isSuccessful) {
                    val directionsResponse = response.body()
                    val routes = directionsResponse?.routes
                    // Parse response and draw route on map
                    callback.onRouteFetched(routes!!)
                } else {
                    // Handle unsuccessful response
                    Log.d("Route Error","onResponse unsuccessful")
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                // Handle failure
                Log.d("Route Failure","onFailure Called")
            }
        })
    }

    interface RouteCallback{
        fun onRouteFetched(routes: List<Route>)
    }
}