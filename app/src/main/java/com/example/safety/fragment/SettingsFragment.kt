package com.example.safety.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.safety.activity.AccountInfoActivity
import com.example.safety.activity.ContactsActivity
import com.example.safety.activity.SignInActivity
import com.example.safety.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding:FragmentSettingsBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        binding?.btnContacts?.setOnClickListener {
            startActivity(Intent(view.context,ContactsActivity::class.java))
        }

        binding?.btnAccountInfo?.setOnClickListener {
            startActivity(Intent(view.context,AccountInfoActivity::class.java))
        }

        binding?.btnLogOut?.setOnClickListener {
            if (auth.currentUser!= null)
            {
                auth.signOut()
                val intent = Intent(this.context,SignInActivity::class.java)
                startActivity(intent)
                this.activity?.finish()
            }
        }
        val sharedPref = requireActivity().applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        binding?.alarmSwitch?.isChecked = sharedPref.getBoolean("allowAlarm",false)
        val editor = sharedPref.edit()
        binding?.alarmSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                editor.putBoolean("allowAlarm",true)
                editor.apply()
            }
            else{
                editor.putBoolean("allowAlarm",false)
                editor.apply()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}