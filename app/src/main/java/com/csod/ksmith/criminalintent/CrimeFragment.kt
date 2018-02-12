package com.csod.ksmith.criminalintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_crime.*
import java.util.*

class CrimeFragment : Fragment() {
    private val crime: Crime by lazy {

        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID

        CrimeLab.getCrime(crimeId)!!
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crime_date.text = crime.date.toString()
        crime_date.setOnClickListener({
            val dialog = DatePickerFragment.newInstance(crime.date)
            dialog.setTargetFragment(this, REQUEST_DATE)
            dialog.show(fragmentManager, DIALOG_DATE)
        })

        crime_title.setText(crime.title)

        crime_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime.title = p0.toString()
            }

        })

        crime_solved.isChecked = crime.solved
        crime_solved.setOnCheckedChangeListener { _, p1 -> crime.solved = p1 }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DATE) {
            val d = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as? Date

            d?.let {
                crime.date = it
                crime_date.text = d.toString()
            }
        }
    }

    companion object {

        const val ARG_CRIME_ID = "crime_id"
        const val DIALOG_DATE = "DialogDate"

        const val REQUEST_DATE = 0

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val f = CrimeFragment()
            f.arguments = args
            return f
        }
    }
}
