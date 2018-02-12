package com.csod.ksmith.criminalintent.database

/**
 * Created by ksmith on 2/12/18.
 */
class CrimeDbSchema {

    class CrimeTable {
        companion object {
            val NAME = "crimes"
        }
    }

    class Cols {
        companion object {
            val UUID = "uuid"
            val TITLE = "title"
            val DATE = "date"
            val SOLVED = "solved"
        }
    }
}