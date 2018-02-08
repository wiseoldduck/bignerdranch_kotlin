package com.csod.ksmith.criminalintent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_crime_list.*
import kotlinx.android.synthetic.main.list_item_crime.*

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


    private class CrimeHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_crime,
            parent, false)) {

        private val titleTextView:TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView:TextView = itemView.findViewById(R.id.crime_date)
        private var crime:Crime? = null

        fun bind(crime:Crime) {
            this.crime = crime
            titleTextView.setText(crime.title)
            dateTextView.setText(crime.date.toString())
        }
    }

    private class CrimeHolder2(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_crime_police, parent, false)) {

        private val titleTextView:TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView:TextView = itemView.findViewById(R.id.crime_date)
        private var crime:Crime? = null

        fun bind(crime:Crime) {
            this.crime = crime
            titleTextView.setText(crime.title)
            dateTextView.setText(crime.date.toString())
        }
    }

    private inner class CrimeAdapter(val crimes: List<Crime>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(activity)

            when (viewType) {
                0 -> return CrimeHolder(layoutInflater, parent!!) // what to do here
                else -> return CrimeHolder2(layoutInflater, parent!!)
            }

        }

        override fun getItemCount() = crimes.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when (holder?.itemViewType) {
                0 -> (holder as CrimeHolder).bind(crimes.get(position))
                else -> (holder as CrimeHolder2).bind(crimes.get(position))
            }
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