package com.example.safety.activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.safety.R
import com.example.safety.databinding.ActivityAccountInfoBinding

class AccountInfoActivity : AppCompatActivity() {

    private var binding:ActivityAccountInfoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPref = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name","")
        val phone = sharedPref.getString("phone","")
        val email = sharedPref.getString("email","")
        val password = sharedPref.getString("password","")
        binding?.etName?.setText(name)
        binding?.etPhone?.setText(phone)
        binding?.etEmail?.setText(email)
        binding?.etEmailPassword?.setText(password)

        binding?.buttonSaveAccount?.setOnClickListener {
            val editor = sharedPref.edit()
            editor.putString("name",binding?.etName?.text.toString())
            editor.putString("phone",binding?.etPhone?.text.toString())
            editor.putString("email",binding?.etEmail?.text.toString())
            editor.putString("password",binding?.etEmailPassword?.text.toString())
            editor.apply()
            Toast.makeText(this,"Account Updated",Toast.LENGTH_SHORT).show()
        }
    }
}