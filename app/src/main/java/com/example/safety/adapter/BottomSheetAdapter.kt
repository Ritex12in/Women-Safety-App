package com.example.safety.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.R
import com.example.safety.database.FireBaseClass
import com.example.safety.model.Comment
import com.example.safety.utils.Constants
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAdapter(private val location: LatLng):BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.place_detail_layout, container, false)
        val recyclerView:RecyclerView = view.findViewById(R.id.rv_markComments)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val geoHashCode = Constants.getGeoHashCode(location.latitude,location.longitude)
        FireBaseClass().getComments(geoHashCode,object : FireBaseClass.CommentListCallback{
            override fun onCommentListFetched(commentList: List<Comment>) {
                val newCommentList = Constants.getNearbyComments(commentList,location)
                recyclerView.adapter = CommentAdapter(newCommentList)
            }
        })

        return view
    }
}