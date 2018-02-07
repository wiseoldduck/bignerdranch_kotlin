package com.csod.ksmith.criminalintent

import java.util.*

object CrimeLab {
    var crimes:List<Crime> = MutableList(100, {
        Crime(title = "Crime # $it", requiredPolice = (it % 2 == 0))
    })

    fun getCrime(id: UUID):Crime? {
        return crimes.find {
            it.id == id
        }
    }
}