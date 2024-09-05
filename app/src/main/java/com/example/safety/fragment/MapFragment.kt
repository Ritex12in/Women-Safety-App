package com.example.safety.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.safety.R
import com.example.safety.activity.CommentActivity
import com.example.safety.adapter.BottomSheetAdapter
import com.example.safety.database.FireBaseClass
import com.example.safety.model.Comment
import com.example.safety.routes.DirectionClass
import com.example.safety.routes.Route
import com.example.safety.utils.Base
import com.example.safety.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.PolyUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(),OnMapReadyCallback {
    private var googleMap:GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude:String? = null
    private var currentLongitude:String? = null
    private var marker:Marker? = null
    private var endLocation:String = ""
    private var allComments : List<Comment>? = null
    private var loadingDialog:Dialog? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val fabComment:FloatingActionButton = view.findViewById(R.id.fab_comment)
        fabComment.setOnClickListener {
            startActivity(Intent(this.context,CommentActivity::class.java))
        }
        val fabRoute:FloatingActionButton = view.findViewById(R.id.fab_route)
        fabRoute.setOnClickListener {
            val startLocation = "$currentLatitude,$currentLongitude"
            if (marker!=null) {
                showRoutes(startLocation, endLocation, getString(R.string.google_maps_api))
            }
            else{
                Base.showToast(view.context,"Please add a marker")
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(gm: GoogleMap) {
        googleMap = gm
        setCurrentLocation()
        googleMap?.setOnMapClickListener {
            if (marker!=null){
                marker?.remove()
                marker = addMarker(it)
            }
            else{
                marker = addMarker(it)
            }
            endLocation = "${it.latitude},${it.longitude}"
        }
        googleMap?.setOnMarkerClickListener {
            showMarkerDetails(it.position)
            false
        }
        googleMap?.setOnMapLongClickListener {
            val geoHashCode = Constants.getGeoHashCode(it.latitude,it.longitude)
            val comment = Comment(
                "124",
                "New name",
                geoHashCode,
                "It's a new demo comment $geoHashCode",
                0,
                it.latitude,
                it.longitude,
                Constants.getCurrentDateAsString(),
                Constants.getCurrentTimeAsString()
            )
            this@MapFragment.context?.let { it1 ->
                FireBaseClass().addComment(comment, it1)
            }
        }
    }

    private fun setCurrentLocation()
    {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            }
            return
        }

        // Enable the My Location layer if permissions are granted
        googleMap?.isMyLocationEnabled = true

        // Start location updates
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                // Add marker for the initial location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                currentLatitude = it.latitude.toString()
                currentLongitude = it.longitude.toString()
                //googleMap?.addMarker(MarkerOptions().position(currentLatLng).title("My Location"))

                // Move camera to the current location
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    private fun addMarker(position: LatLng): Marker? {
        return googleMap?.addMarker(
            MarkerOptions().position(position)
                .title("Marker")
                .snippet("Snippet")
        )
    }

    private fun showRoutes(start:String, end:String, api:String) {
        loadingDialog = Base.showProgressBar(this.requireContext())
        FireBaseClass().getAllComments(object :FireBaseClass.CommentListCallback{
            override fun onCommentListFetched(commentList: List<Comment>) {
                allComments = commentList
                DirectionClass().getRoutes(start,end,api,object : DirectionClass.RouteCallback {
                    override fun onRouteFetched(routes: List<Route>) {
                        loadingDialog?.dismiss()
                        if (routes.isNotEmpty())
                        {
                            Log.d("RouteListSize",routes.size.toString())
                            for (route in routes){
                                val polylineOptions = PolylineOptions().apply {
                                    val decodedPath = PolyUtil.decode(route.overviewPolyline.points)
                                    for(path in decodedPath){
                                        add(path)
                                        color(getPathColor(path))
                                    }
                                    width(10f)
                                }
                                googleMap?.addPolyline(polylineOptions)
                            }
                        }
                        else{
                            Toast.makeText(this@MapFragment.context,"Route List Empty",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        })
    }

    private fun showMarkerDetails(location: LatLng){
        val bottomSheetAdapter = BottomSheetAdapter(location)
        bottomSheetAdapter.show(childFragmentManager,bottomSheetAdapter.tag)
    }

    private fun getPathColor(path:LatLng):Int{
        if (allComments!=null){
            val nearbyComments = Constants.getNearbyComments(allComments!!,path)
            var safetySum = 0.0
            for (i in nearbyComments){
                safetySum+= i.safetyLevel
            }
            if (nearbyComments.isNotEmpty())
                safetySum/= nearbyComments.size

            return if (safetySum<1){
                Color.GREEN
            }
            else if(safetySum<2){
                Color.YELLOW
            }
            else if(safetySum<3){
                Color.MAGENTA
            }
            else{
                Color.RED
            }
        }
        else{
            return Color.GRAY
        }
    }

}