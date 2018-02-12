package com.csod.ksmith.criminalintent

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.csod.ksmith.criminalintent.database.CrimeBaseHelper
import java.util.*
import java.util.concurrent.TimeUnit

object CrimeLab {
    var crimes:MutableList<Crime> = mutableListOf()
    var database:SQLiteDatabase? = null


    fun createDatabase(context: Context) {
        database = CrimeBaseHelper(context).writableDatabase
    }

    fun getCrime(id: UUID):Crime? {
        return crimes.find {
            it.id == id
        }
    }
}