package com.csod.ksmith.criminalintent

import java.util.*


data class Crime(var title:String = "", var date:Date = Date(), var solved:Boolean = false,
                 val requiredPolice: Boolean = false, val id: UUID = UUID.randomUUID()) {

}