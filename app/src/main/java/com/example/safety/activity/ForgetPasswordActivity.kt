package com.example.safety.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.safety.R
import com.example.safety.databinding.ActivityForgetPasswordBinding
import com.example.safety.utils.Base
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetPasswordActivity : AppCompatActivity() {
    private var binding: ActivityForgetPasswordBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding?.btnForgotPasswordSubmit?.setOnClickListener { resetPassword() }
    }

    private fun validateForm(email: String): Boolean {
        return when {
            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding?.tilEmailForgetPassword?.error = "Enter valid email address"
                false
            }
            else -> true
        }

    }

    private fun resetPassword()
    {
        val email = binding?.etForgotPasswordEmail?.text.toString()
        if (validateForm(email))
        {
            val pb = Base.showProgressBar(this)
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if (task.isSuccessful)
                {
                    pb.cancel()
                    binding?.tilEmailForgetPassword?.visibility = View.GONE
                    binding?.tvSubmitMsg?.visibility = View.VISIBLE
                    binding?.btnForgotPasswordSubmit?.visibility = View.GONE
                }
                else
                {
                    pb.cancel()
                    Base.showToast(this,"Can not reset your password. Try after sometime")
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}