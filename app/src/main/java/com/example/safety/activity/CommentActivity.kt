package com.example.safety.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safety.R
import com.example.safety.adapter.CommentAdapter
import com.example.safety.database.FireBaseClass
import com.example.safety.databinding.ActivityCommentBinding
import com.example.safety.model.Comment
import com.example.safety.model.UserModel
import com.example.safety.utils.Base
import com.example.safety.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class CommentActivity : AppCompatActivity() {
    private var binding:ActivityCommentBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var safetyLevel = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding?.rvComments?.layoutManager = LinearLayoutManager(this)
        setComment()
        binding?.btnAddComment?.setOnClickListener {
            addComment()
        }
        binding?.safetyLevelSpinner?.adapter = getSpinnerAdapter(Constants.safetyLevelOption)
        binding?.safetyLevelSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    safetyLevel = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }

            }
    }

    private fun setComment() {
        Constants.getCurrentLocation(this,object :Constants.LocationCallback{
            override fun onLocationFetched(location: Location) {
                val geoHashCode = Constants.getGeoHashCode(location.latitude,location.longitude)
                FireBaseClass().getComments(geoHashCode,object :FireBaseClass.CommentListCallback{
                    override fun onCommentListFetched(commentList: List<Comment>) {
                        val newCommentList = Constants.getNearbyComments(commentList,
                            LatLng(location.latitude,location.longitude))
                        binding?.rvComments?.adapter = CommentAdapter(newCommentList)
                    }
                })
            }

        })
    }

    private fun addComment(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Base.showToast(this@CommentActivity,"Permission not available")
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            val geoHashCode = Constants.getGeoHashCode(it.latitude,it.longitude)
            FireBaseClass().getUserInfo(object :FireBaseClass.UserInfoCallback{
                override fun onUserInfoFetched(userInfo: UserModel?) {
                    val comment = Comment(
                        FireBaseClass().getCurrentUserId(),
                        userInfo?.name!!,
                        geoHashCode,
                        binding?.etComment?.text.toString(),
                        safetyLevel,
                        it.latitude,
                        it.longitude,
                        Constants.getCurrentDateAsString(),
                        Constants.getCurrentTimeAsString()
                    )
                    FireBaseClass().addComment(comment,this@CommentActivity)
                }

            })
        }
    }

    private fun getSpinnerAdapter(list: List<String>): SpinnerAdapter {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}