package com.csod.ksmith.criminalintent

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_crime_pager.*
import java.util.*

class CrimePagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as? UUID
        val fm = supportFragmentManager

        crime_view_pager.adapter = object: FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return CrimeLab.crimes.size
            }

            override fun getItem(position: Int): Fragment {
                return CrimeFragment.newInstance(CrimeLab.crimes.get(position).id)
            }
        }

        crime_view_pager.currentItem = CrimeLab.crimes.indexOfFirst {
            it.id == crimeId
        }

    }

    companion object {
        val EXTRA_CRIME_ID = "com.csod.ksmith.criminalintent.crime_id"

        public fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            val i = Intent(packageContext, CrimePagerActivity::class.java)
            i.putExtra(EXTRA_CRIME_ID, crimeId)
            return i
        }
    }
}
