package com.csod.ksmith.criminalintent

import java.util.*
import java.util.concurrent.TimeUnit

object CrimeLab {
    var crimes:List<Crime> = MutableList(100, {
        Crime(title = "Crime # $it", requiredPolice = (it % 2 == 0), date =
        Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((Math.random() * 365).toLong())));
    })

    fun getCrime(id: UUID):Crime? {
        return crimes.find {
            it.id == id
        }
    }
}