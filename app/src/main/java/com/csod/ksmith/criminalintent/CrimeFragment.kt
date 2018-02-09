package com.csod.ksmith.criminalintent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_crime.*
import java.util.*

class CrimeFragment : Fragment() {
    private var _crime: Crime? = null
    private val crime: Crime?
        get() {
            if (_crime != null) return _crime

            val crimeId:UUID? = arguments?.getSerializable(ARG_CRIME_ID) as? UUID

            crimeId?.let {
                _crime = CrimeLab.getCrime(it)
            }
            return _crime
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_crime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crime?.let { crime ->
            crime_date.text = crime.date.toString()
            crime_date.isEnabled = false

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

    }

    companion object {

        val ARG_CRIME_ID = "crime_id"

        fun newInstance(crimeId:UUID):CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val f = CrimeFragment()
            f.arguments = args
            return f
        }
    }
}
