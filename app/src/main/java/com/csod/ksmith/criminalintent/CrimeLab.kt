package com.csod.ksmith.criminalintent

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.csod.ksmith.criminalintent.database.CrimeBaseHelper
import java.io.File
import java.util.*

object CrimeLab {
    lateinit var filesDir:File
    lateinit var database:SQLiteDatabase

    var crimes:MutableList<Crime> = mutableListOf()

    fun initInstance(context:Context) {
        filesDir = context.filesDir
        database = CrimeBaseHelper(context).writableDatabase
    }

    fun getCrime(id: UUID):Crime? {
        return crimes.find {
            it.id == id
        }
    }

    fun getPhotoFile(crime:Crime):File {
        return File(filesDir, crime.photoFilename)
    }

    fun updateCrime(crime: Crime) {
        // no-op without database 🤷‍
    }

}