package com.example.safety.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.safety.model.Comment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Constants {
    const val user = "USER"
    const val comment = "COMMENTS"
    val safetyLevelOption = listOf("Safe","Unsafe at night","Unsafe even at day time","Danger")
    const val precision = 5

    private const val BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz"
    private val BITS = intArrayOf(16, 8, 4, 2, 1)

    fun getGeoHashCode(latitude: Double, longitude: Double): String {
        var (minLat, maxLat) = Pair(-90.0, 90.0)
        var (minLon, maxLon) = Pair(-180.0, 180.0)
        val geohash = StringBuilder()
        var bits = 0
        var hashCharIndex = 0

        while (geohash.length < precision) {
            var bit = 0
            for (i in BITS.indices) {
                val mid = (minLat + maxLat) / 2
                if (hashCharIndex % 2 != 0) {
                    if (latitude > mid) {
                        bit = bit or BITS[i]
                        minLat = mid
                    } else {
                        maxLat = mid
                    }
                } else {
                    val midLon = (minLon + maxLon) / 2
                    if (longitude > midLon) {
                        bit = bit or BITS[i]
                        minLon = midLon
                    } else {
                        maxLon = midLon
                    }
                }
                hashCharIndex++
            }
            geohash.append(BASE32[bit])
            if (bits < 4) {
                bits = if (bits == 0) 4 else bits + 1
            } else {
                bits = 0
            }
        }
        return geohash.toString()
    }

    fun getCurrentDateAsString(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }
    fun getCurrentTimeAsString(): String {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return timeFormat.format(calendar.time)
    }

    fun getCurrentLocation(context: Context,callback: LocationCallback)
    {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
             callback.onLocationFetched(it)
        }
    }

    fun getNearbyComments(list:List<Comment>, pos:LatLng):List<Comment>
    {
        val result = arrayListOf<Comment>()
        for (i in list){
            if (getDistance(LatLng(i.lat,i.lng),pos)<500){
                result.add(i)
            }
        }

        return result
    }

    fun getDistance(l1:LatLng,l2:LatLng):Float
    {
        val result = FloatArray(1)
        Location.distanceBetween(
            l1.latitude,
            l1.longitude,
            l2.latitude,
            l2.longitude,
            result
        )
        return result[0]
    }

    interface LocationCallback{
        fun onLocationFetched(location: Location)
    }
}