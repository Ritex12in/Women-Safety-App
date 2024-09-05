package com.example.safety.fragment

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.example.safety.DAO.PhoneNumberDao
import com.example.safety.R
import com.example.safety.database.ContactApplication
import com.example.safety.databinding.FragmentHomeBinding
import com.example.safety.messages.Email
import com.example.safety.messages.SendSMS
import com.example.safety.model.PhoneNumber
import com.example.safety.repository.PhoneNumberRepository
import com.example.safety.services.SoundService
import com.example.safety.services.SpeechRecognition
import com.example.safety.utils.Base
import com.example.safety.utils.Constants
import com.example.safety.viewModel.EmailViewModel
import com.example.safety.viewModel.EmailViewModelFactory
import com.example.safety.viewModel.PhoneViewModel
import com.example.safety.viewModel.PhoneViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var binding:FragmentHomeBinding? = null
    private lateinit var audioManager: AudioManager
    private val phoneViewModel:PhoneViewModel by viewModels{
        PhoneViewModelFactory((requireActivity().application as ContactApplication).phoneRepository)
    }
    private val emailViewModel:EmailViewModel by viewModels {
        EmailViewModelFactory((requireActivity().application as ContactApplication).emailRepository)
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding?.root
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name","")
        val allowAlarm = sharedPref.getBoolean("allowAlarm",false)
        val email = sharedPref.getString("email","")
        val password = sharedPref.getString("password","")
        val phoneNumbers = arrayListOf<String>()
        val sub = "SOS situation for $name"
        var msg = "Hello!, my name is $name and I am in an emergency situation. Please send help as soon as possible." +
                "Here is coordinate of my current location:\n(latitude,longitude):"
        phoneViewModel.allPhoneNumbers.observe(viewLifecycleOwner) { it ->
            it?.let {
                for (i in it) {
                    phoneNumbers.add(i.phoneNumber)
                }
            }
        }
        val emailList = arrayListOf<String>()
        emailViewModel.allEmails.observe(viewLifecycleOwner){it->
            it?.let {
                for (i in it){
                    emailList.add(i.email)
                }
            }
        }
        binding?.btnSos?.setOnClickListener {
            if(allowAlarm){
                setMaxMediaVolume()
                startAlarm()
            }
            Constants.getCurrentLocation(view.context,object :Constants.LocationCallback{
                override fun onLocationFetched(location: Location) {
                    msg+= "(${location.latitude},${location.latitude})"
                    if (phoneNumbers.isNotEmpty()){
                        SendSMS.sendSms(phoneNumbers,msg,view.context)
                    }
                    else{
                        Base.showToast(requireContext(),"Phone number list is empty")
                    }
                    if (emailList.isNotEmpty()){
                        Email.sendEmail(emailList,email!!,password!!,sub, msg)
                    }
                    else{
                        Base.showToast(requireContext(),"Email list is empty")
                    }
                }

            })
        }
        binding?.btnNotify?.setOnClickListener {
            stopAlarm()
        }
        binding?.btnAlert?.setOnClickListener {
            showAlertDialog()
        }
    }


    private fun startAlarm(){
        requireContext().startService(Intent(requireContext(),SoundService::class.java))
    }
    private fun stopAlarm(){
        requireContext().stopService(Intent(requireContext(),SoundService::class.java))
    }
    private fun setMaxMediaVolume() {
        // Get the maximum volume index for the STREAM_MUSIC stream
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        // Set the media volume to the maximum
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }

    private fun showAlertDialog(){
        val dialog = Dialog(requireContext())
        val speechRecognition = SpeechRecognition(requireActivity(),object :SpeechRecognition.onMatched{
            override fun onMatched() {
                setMaxMediaVolume()
                startAlarm()
                dialog.dismiss()
            }
        })
        speechRecognition.startListening()
        dialog.setContentView(R.layout.alert_dialog_layout)
        dialog.setCancelable(false)
        dialog.findViewById<Button>(R.id.btn_close_alert).setOnClickListener {
            dialog.dismiss()
            speechRecognition.stopListening()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}