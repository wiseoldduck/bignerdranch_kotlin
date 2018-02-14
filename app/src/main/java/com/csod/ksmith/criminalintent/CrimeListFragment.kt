package com.csod.ksmith.criminalintent

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_crime_list.*
import java.text.SimpleDateFormat
import java.util.*


class CrimeListFragment : Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crime:Crime)
    }

    var subtitleVisible: Boolean = false
    var callbacks: Callbacks? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_crime_list, container, false)

        CrimeLab.initInstance(activity!!)

        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crime_recycler_view.layoutManager = LinearLayoutManager(activity)
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_crime_list, menu)

        menu?.findItem(R.id.show_subtitle)?.setTitle(if (subtitleVisible) R.string.hide_subtitle else R.string.show_subtitle)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab.crimes.add(crime)
                updateUI()
                callbacks?.onCrimeSelected(crime)
                true
            }
            R.id.show_subtitle -> {
                subtitleVisible = !subtitleVisible
                activity?.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        (context as? Callbacks)?.let {
            callbacks = it
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateSubtitle() {
        val count = CrimeLab.crimes.count()
        val subtitle = if (subtitleVisible) {
            getString(R.string.subtitle_format, count)
        } else {
            null
        }

        (activity as AppCompatActivity).getSupportActionBar()?.subtitle = subtitle
    }

    public fun updateUI() {
        if (crime_recycler_view.adapter != null) {
            crime_recycler_view.adapter.notifyDataSetChanged()
        } else {
            crime_recycler_view.adapter = CrimeAdapter(CrimeLab.crimes)
        }
        updateSubtitle()
    }

    private inner class CrimeHolder(type: Int, inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(
            if (type == 0) R.layout.list_item_crime else R.layout.list_item_crime_police,
            parent, false)) {

        private val titleTextView: TextView? = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView? = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView? = itemView.findViewById(R.id.crime_solved)
        private var crime: Crime? = null

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView?.setText(crime.title)

            dateTextView?.setText(SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US).format(crime.date))
            solvedImageView?.visibility = if (crime.solved) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener {
                activity?.let {
                    callbacks?.onCrimeSelected(crime)
                }
            }

        }
    }

    private inner class CrimeAdapter(val crimes: List<Crime>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(activity)

            return CrimeHolder(viewType, layoutInflater, parent!!) // what to do here

        }

        override fun getItemCount() = crimes.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            (holder as CrimeHolder).bind(crimes.get(position))
        }

        override fun getItemViewType(position: Int): Int {
            if (crimes.get(position).requiredPolice) {
                return 1
            } else {
                return 0
            }
        }

    }

    companion object {
        val SAVED_SUBTITLE_VISIBLE = "subtitle"
    }
}