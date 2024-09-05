package com.example.safety.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.safety.R
import com.example.safety.databinding.ActivityMainBinding
import com.example.safety.fragment.HomeFragment
import com.example.safety.fragment.MapFragment
import com.example.safety.fragment.OtherOptionsFragment
import com.example.safety.fragment.SettingsFragment
import com.example.safety.utils.Permissions

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null
    private val homeFragment = HomeFragment()
    private val mapFragment = MapFragment()
    private val otherOptionsFragment = OtherOptionsFragment()
    private val settingsFragment = SettingsFragment()
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Permissions().getPermissions(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()

        binding?.bottomNav?.setOnItemSelectedListener {menuItem->
            when(menuItem.itemId){
                R.id.nav_home-> {
                    switchFragment(homeFragment)
                    true
                }
                R.id.nav_map->{
                    switchFragment(mapFragment)
                    true
                }
                R.id.nav_more->{
                    switchFragment(otherOptionsFragment)
                    true
                }
                R.id.nav_settings->{
                    switchFragment(settingsFragment)
                    true
                }
                else-> false
            }
        }
    }
    private fun switchFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .commit()
    }
}