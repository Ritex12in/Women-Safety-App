package com.example.safety.database

import android.content.Context
import android.util.Log
import com.example.safety.model.Comment
import com.example.safety.model.UserModel
import com.example.safety.utils.Base
import com.example.safety.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class FireBaseClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(userInfo: UserModel) {
        mFireStore.collection(Constants.user)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge())
            .addOnSuccessListener { Log.d("UserCreated", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("UserCreated", "Error writing document", e) }
    }

    fun getCurrentUserId(): String {
        val currentUser = Firebase.auth.currentUser
        var currentUserId = ""
        if (currentUser != null)
            currentUserId = currentUser.uid
        return currentUserId
    }

    fun addComment(comment:Comment,context: Context){
        mFireStore.collection(Constants.comment).add(comment)
            .addOnSuccessListener { Base.showToast(context,"Comment added successfully") }
            .addOnFailureListener { Base.showToast(context,"Unable to add comment") }
    }

    fun getUserInfo(callback: UserInfoCallback){
        mFireStore.collection(Constants.user).document(getCurrentUserId()).get()
            .addOnSuccessListener {
                val userInfo = it.toObject(UserModel::class.java)
                callback.onUserInfoFetched(userInfo)
            }
    }

    fun getComments(geoCode:String,callBack:CommentListCallback){
        val commentList = arrayListOf<Comment>()
        mFireStore.collection(Constants.comment)
            .whereEqualTo("geoCode",geoCode)
            .get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    val comment = document.toObject(Comment::class.java)
                    commentList.add(comment)
                    Log.d("CommentFetch", "Comment successfully fetched")
                }
                callBack.onCommentListFetched(commentList)
            }
            .addOnFailureListener { e -> Log.w("CommentList", "Error in  comment fetch", e) }
    }
    fun getAllComments(callBack:CommentListCallback){
        val commentList = arrayListOf<Comment>()
        mFireStore.collection(Constants.comment).get()
            .addOnSuccessListener {documents->
                for (document in documents){
                    val comment = document.toObject(Comment::class.java)
                    commentList.add(comment)
                    Log.d("CommentFetch", "Comment successfully fetched")
                }
                callBack.onCommentListFetched(commentList)
            }
            .addOnFailureListener { e -> Log.w("CommentList", "Error in  comment fetch", e) }
    }

    interface UserInfoCallback {
        fun onUserInfoFetched(userInfo: UserModel?)
    }
    interface CommentListCallback{
        fun onCommentListFetched(commentList:List<Comment>)
    }

}