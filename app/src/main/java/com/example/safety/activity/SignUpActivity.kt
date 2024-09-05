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
import com.example.safety.database.FireBaseClass
import com.example.safety.databinding.ActivitySignUpBinding
import com.example.safety.model.UserModel
import com.example.safety.utils.Base
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private var binding: ActivitySignUpBinding? = null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding?.tvLoginPage?.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding?.btnSignUp?.setOnClickListener { registerUser() }
    }

    private fun registerUser()
    {
        val name = binding?.etSinUpName?.text.toString()
        val email = binding?.etSinUpEmail?.text.toString()
        val password = binding?.etSinUpPassword?.text.toString()
        if (validateForm(name,email,password))
        {
            val pb = Base.showProgressBar(this)
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful)
                    {
                        val userId = task.result.user?.uid
                        val userInfo = UserModel(name = name, id = userId!!, emailId = email)
                        FireBaseClass().registerUser(userInfo)
                        Base.showToast(this,"User Id created successfully")
                        pb.cancel()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else {
                        Base.showToast(this, "User Id not created. Try again later")
                        pb.cancel()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name)->{
                binding?.tilName?.error = "Enter name"
                false
            }
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