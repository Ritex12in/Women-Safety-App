package com.example.safety.utils

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import com.example.safety.R

object Base {
    fun showProgressBar(context: Context): Dialog
    {
        val dialogue = Dialog(context)
        dialogue.setContentView(R.layout.loading_progress_bar)
        dialogue.show()

        return dialogue
    }

    fun showToast(context: Context, msg:String)
    {
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

}