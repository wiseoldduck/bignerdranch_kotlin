package com.csod.ksmith.criminalintent

import java.util.*
import java.util.concurrent.TimeUnit

object CrimeLab {
    var crimes:MutableList<Crime> = mutableListOf()

    fun getCrime(id: UUID):Crime? {
        return crimes.find {
            it.id == id
        }
    }
}