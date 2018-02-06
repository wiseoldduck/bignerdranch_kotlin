package com.csod.ksmith.criminalintent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class CrimeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)

        val fm = supportFragmentManager
        fm.findFragmentById(R.id.fragment_container)

        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = CrimeFragment()
            fm.beginTransaction().add(R.id.fragment_container, fragment)
                    .commit()
        }
    }
}
