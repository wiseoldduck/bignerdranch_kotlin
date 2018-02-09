package com.csod.ksmith.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import java.util.*

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }

    companion object {
        private val EXTRA_CRIME_ID = "com.csod.ksmith.criminalintent.crime_id"
        public fun newIntent(packageContext: Context?, crimeId: UUID?): Intent {
            val i = Intent(packageContext, CrimeActivity::class.java)
            i.putExtra(EXTRA_CRIME_ID, crimeId ?: UUID(0, 0))
            return i
        }
    }

}
