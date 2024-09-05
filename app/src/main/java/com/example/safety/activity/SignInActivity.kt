package com.example.safety.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.safety.R
import com.example.safety.databinding.ActivitySignInBinding
import com.example.safety.utils.Base
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private var binding: ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding?.tvRegister?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        binding?.tvForgotPassword?.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        binding?.btnSignIn?.setOnClickListener {
            signInUser()
        }
    }

    private fun signInUser()
    {
        val email = binding?.etSinInEmail?.text.toString()
        val password = binding?.etSinInPassword?.text.toString()
        if (validateForm(email, password))
        {
            val pb = Base.showProgressBar(this)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful)
                    {
                        pb.cancel()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else
                    {
                        Base.showToast(this,"Can't login currently. Try after sometime")
                        pb.cancel()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding?.tilEmail?.error = "Enter valid email address"
                false
            }
            TextUtils.isEmpty(password)->{
                binding?.tilPassword?.error = "Enter password"
                false
            }
            else -> { true }
        }
    }
}