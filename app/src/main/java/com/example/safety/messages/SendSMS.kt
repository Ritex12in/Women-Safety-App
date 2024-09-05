package com.example.safety.messages

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast

object SendSMS {
    fun sendSms(phoneNumbers: ArrayList<String>, message: String,context: Context) {
        val smsManager: SmsManager = SmsManager.getDefault()

        for (phoneNumber in phoneNumbers){
            val sentIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"),
                PendingIntent.FLAG_IMMUTABLE)
            val deliveredIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"),
                PendingIntent.FLAG_IMMUTABLE)

            try {
                smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent)
                Toast.makeText(context, "SMS sent to $phoneNumber", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}