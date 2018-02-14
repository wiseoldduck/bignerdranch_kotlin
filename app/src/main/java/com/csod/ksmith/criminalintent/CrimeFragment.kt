package com.csod.ksmith.criminalintent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csod.ksmith.criminalintent.thirdparty.safeLet
import kotlinx.android.synthetic.main.fragment_crime.*
import java.io.File
import java.util.*

class CrimeFragment : Fragment() {

    interface Callbacks {
        fun onCrimeUpdated(crime: Crime)
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
                updateCrime()
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
        crime_solved.setOnCheckedChangeListener { _, isChecked ->
            crime.solved = isChecked
            updateCrime()
        }

        crime_suspect.isEnabled =
                (activity?.packageManager?.resolveActivity(pickContact,
                        PackageManager.MATCH_DEFAULT_ONLY) != null)

        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        crime_camera.isEnabled = (captureImage.resolveActivity(activity?.packageManager) != null)
        photoFile = CrimeLab.getPhotoFile(crime)
        crime_camera.setOnClickListener({
            val activity = activity
            val photoFile = photoFile
            if (activity != null && photoFile != null) {

                val uri = FileProvider.getUriForFile(activity, "com.csod.ksmith.criminalintent.fileprovider",
                        photoFile)

                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                val cameraActivities = activity.packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)


                for (cameraActivity in cameraActivities) {
                    activity.grantUriPermission(cameraActivity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }

                startActivityForResult(captureImage, REQUEST_PHOTO)
            }

        })
        updatePhotoView()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_crime, container, false)
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
                updateCrime()
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
                updateCrime()
                crime_suspect.text = suspect
            } finally {
                c?.close()
            }

        } else if (requestCode == REQUEST_PHOTO) {
            safeLet(activity, photoFile) { activity, photoFile ->
                val uri = FileProvider.getUriForFile(activity, "com.csod.ksmith.criminalintent.fileprovider",
                        photoFile)

                activity.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                updateCrime()
                updatePhotoView()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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

    fun updatePhotoView() {

        val photoFile = photoFile

        if (photoFile == null || !photoFile.exists()) {
            crime_photo.setImageDrawable(null)
        } else {
            val bitmap = PictureUtils.getScaledBitmap(photoFile.path, activity!!)
            crime_photo.setImageBitmap(bitmap)
        }
    }

    private val crime: Crime by lazy {

        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID

        CrimeLab.getCrime(crimeId)!!
    }

    private var photoFile: File? = null

    private val pickContact: Intent by lazy {
        val i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
//        i.addCategory(Intent.CATEGORY_HOME) uncomment to test isEnabled on crime_suspect button
        i
    }

    private var callbacks: CrimeFragment.Callbacks? = null

    private fun updateCrime() {
        CrimeLab.updateCrime(crime)
        callbacks?.onCrimeUpdated(crime)
    }

    companion object {

        const val ARG_CRIME_ID = "crime_id"
        const val DIALOG_DATE = "DialogDate"
        const val REQUEST_DATE = 0
        const val REQUEST_CONTACT = 1
        const val REQUEST_PHOTO = 2

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val f = CrimeFragment()
            f.arguments = args
            return f
        }
    }
}
