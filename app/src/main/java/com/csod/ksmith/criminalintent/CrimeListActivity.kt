package com.csod.ksmith.criminalintent

import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_twopane.*

class CrimeListActivity : SingleFragmentActivity(), CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_masterdetail
    }

    // region CrimeListFragment.Callbacks
    override fun onCrimeSelected(crime: Crime) {
        if (detail_fragment_container == null) {
            val i = CrimePagerActivity.newIntent(this, crime.id)
            startActivity(i)
        } else {
            val newDetail = CrimeFragment.newInstance(crime.id)
            supportFragmentManager.beginTransaction().replace(R.id.detail_fragment_container,
                    newDetail).commit()
        }
    }
// endregion


    // region CrimeFragment.Callbacks
    override fun onCrimeUpdated(crime: Crime) {
        (supportFragmentManager.findFragmentById(R.id.fragment_container)
                as? CrimeListFragment)?.updateUI()
    }
// endregion

}