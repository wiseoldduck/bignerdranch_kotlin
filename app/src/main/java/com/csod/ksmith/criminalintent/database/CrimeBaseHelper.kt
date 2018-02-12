package com.csod.ksmith.criminalintent.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ksmith on 2/12/18.
 */
class CrimeBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {


    companion object {
        val VERSION = 1
        val DATABASE_NAME = "crimeBase.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table ${CrimeDbSchema.CrimeTable.NAME} (_id integer primary key autoincrement," +
                "${CrimeDbSchema.Cols.UUID}, ${CrimeDbSchema.Cols.TITLE}," +
                "${CrimeDbSchema.Cols.DATE}, ${CrimeDbSchema.Cols.SOLVED})")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}