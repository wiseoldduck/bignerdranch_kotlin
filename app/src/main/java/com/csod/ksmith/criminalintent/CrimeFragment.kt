package com.csod.ksmith.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
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

    private val pickContact: Intent by lazy {
        val i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
//        i.addCategory(Intent.CATEGORY_HOME) uncomment to test isEnabled on crime_suspect button
        i
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

        crime_report.setOnClickListener({
            var i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            i = Intent.createChooser(i, getString(R.string.send_crime_report))
            startActivity(i)
        })

        crime_suspect.setOnClickListener({
            startActivityForResult(pickContact, REQUEST_CONTACT)
        })

        crime_solved.isChecked = crime.solved
        crime_solved.setOnCheckedChangeListener { _, p1 -> crime.solved = p1 }

        crime_suspect.isEnabled =
                (activity?.packageManager?.resolveActivity(pickContact,
                        PackageManager.MATCH_DEFAULT_ONLY) != null)

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
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            val url = data.data

            val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

            val c = activity?.contentResolver?.query(url, queryFields, null,
                    null, null)

            try {
                // Double-check that you actually got results
                if (c == null || c.getCount() == 0) {
                    return
                }
                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst()
                val suspect = c.getString(0)
                crime.suspect = suspect
                crime_suspect.setText(suspect)
            } finally {
                c?.close()
            }

        }
    }

    fun getCrimeReport(): String {
        val solvedString =
                if (crime.solved) getString(R.string.crime_report_solved)
                else
                    getString(R.string.crime_report_unsolved)

        val dateString = DateFormat.format("EEE, MMM dd", crime.date).toString()

        val suspect =
                if (crime.suspect == null) getString(R.string.crime_report_no_suspect)
                else getString(R.string.crime_report_suspect)

        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspect)
    }

    companion object {

        const val ARG_CRIME_ID = "crime_id"
        const val DIALOG_DATE = "DialogDate"
        const val REQUEST_CONTACT = 1
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
