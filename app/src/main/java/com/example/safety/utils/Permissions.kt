package com.example.safety.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class Permissions() {

    @RequiresApi(Build.VERSION_CODES.S)
    fun getPermissions(context: Context){
        Dexter.withContext(context).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECORD_AUDIO

        ).withListener(object : MultiplePermissionsListener {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report.let {
                    if (report!!.areAllPermissionsGranted())
                    {
                        Toast.makeText(context,"All permissions granted",Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(context,"Permissions not granted",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }

        }).withErrorListener{
            Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
        }.onSameThread().check()
    }
}