package com.csod.ksmith.criminalintent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_crime_list.*

class CrimeListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_crime_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crime_recycler_view.layoutManager = LinearLayoutManager(activity)
        crime_recycler_view.adapter = CrimeAdapter(CrimeLab.crimes)
    }

    private fun updateUI() {
        // BNR set up the crime_cycler_view.adapter here: I set the adapter in onViewCreated
        // (because that sure seems more right in .kt at least)
    }


    private class CrimeHolder(type: Int, inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(
            if (type == 0) R.layout.list_item_crime else R.layout.list_item_crime_police,
            parent, false)) {

        private val titleTextView: TextView? = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView? = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView? = itemView.findViewById(R.id.crime_solved)

        fun bind(crime: Crime) {
            titleTextView?.setText(crime.title)
            dateTextView?.setText(crime.date.toString())
            solvedImageView?.visibility = if (crime.solved) View.VISIBLE else View.INVISIBLE
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
}