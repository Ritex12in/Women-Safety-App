package com.example.safety.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.R
import com.example.safety.adapter.EmailListAdapter
import com.example.safety.adapter.PhoneListAdapter
import com.example.safety.database.ContactApplication
import com.example.safety.model.Email
import com.example.safety.model.PhoneNumber
import com.example.safety.viewModel.EmailViewModel
import com.example.safety.viewModel.EmailViewModelFactory
import com.example.safety.viewModel.PhoneViewModel
import com.example.safety.viewModel.PhoneViewModelFactory

class ContactsActivity : AppCompatActivity() {

    private val phoneViewModel:PhoneViewModel by viewModels{
        PhoneViewModelFactory((application as ContactApplication).phoneRepository)
    }
    private val emailViewModel:EmailViewModel by viewModels {
        EmailViewModelFactory((application as ContactApplication).emailRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val phoneRecyclerView:RecyclerView = findViewById(R.id.rv_phone_number)
        val phoneAdapter = PhoneListAdapter()
        phoneRecyclerView.adapter = phoneAdapter
        phoneRecyclerView.layoutManager = LinearLayoutManager(this)
        phoneAdapter.setOnClickListener(object :PhoneListAdapter.OnClickListener{
            override fun onClick(id: Int) {
                phoneViewModel.deleteById(id)
            }

        })

        phoneViewModel.allPhoneNumbers.observe(this, Observer { phones->
            phones?.let {
                phoneAdapter.submitList(it)
            }
        })


        val etPhoneNumber:EditText = findViewById(R.id.et_add_phone)
        val btnAddPhone:Button = findViewById(R.id.btn_add_phone)
        btnAddPhone.setOnClickListener {
            val phone = etPhoneNumber.text.toString()
            if(phone.isEmpty()){
                Toast.makeText(this,"Phone Number is empty",Toast.LENGTH_SHORT).show()
            }
            else{
                val phoneNumber = PhoneNumber(phoneNumber = phone)
                phoneViewModel.insert(phoneNumber)
                etPhoneNumber.setText("")
            }
        }

        val emailRecyclerView:RecyclerView = findViewById(R.id.rv_email_address)
        val emailAdapter = EmailListAdapter()
        emailRecyclerView.adapter = emailAdapter
        emailRecyclerView.layoutManager = LinearLayoutManager(this)
        emailAdapter.setOnClickListener(object :EmailListAdapter.OnClickListener{
            override fun onClick(id: Int) {
                emailViewModel.deleteById(id)
            }

        })

        emailViewModel.allEmails.observe(this, Observer {emails ->
            emails?.let {
                emailAdapter.submitList(it)
            }
        })
        val etEmail:EditText = findViewById(R.id.et_add_email)
        val btnAddEmail:Button = findViewById(R.id.btn_add_email)
        btnAddEmail.setOnClickListener {
            val email = etEmail.text.toString()
            if (email.isEmpty()){
                Toast.makeText(this,"Email Address is empty",Toast.LENGTH_SHORT).show()
            }
            else{
                emailViewModel.insert(Email(email = email))
                etEmail.setText("")
            }
        }
    }
}