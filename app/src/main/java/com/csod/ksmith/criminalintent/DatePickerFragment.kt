package com.csod.ksmith.criminalintent

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_date.*
import kotlinx.android.synthetic.main.dialog_date.view.*
import java.util.*

class DatePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date = arguments?.getSerializable(ARG_DATE) as? Date

        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)
        date?.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)

            v.dialog_date_picker.init(y, m, d, null)
        }

        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    val calendarDate = GregorianCalendar(v.dialog_date_picker.year, v.dialog_date_picker.month,
                            v.dialog_date_picker.dayOfMonth).time
                    sendResult(Activity.RESULT_OK, calendarDate)
                })
                .create()
    }

    private fun sendResult(resultCode:Int, date:Date) {
        targetFragment?.let {
            val intent = Intent()
            intent.putExtra(EXTRA_DATE, date)
            it.onActivityResult(targetRequestCode, resultCode, intent)
        }
    }

    companion object {
        val ARG_DATE = "date"
        val EXTRA_DATE = "com.csod.ksmith.criminalintent.date"

        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}