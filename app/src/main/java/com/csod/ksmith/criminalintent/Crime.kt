package com.csod.ksmith.criminalintent

import java.util.*


data class Crime(var title:String = "", val date:Date = Date(), var solved:Boolean = false,
                 val requiredPolice: Boolean = false, val id: UUID = UUID.randomUUID()) {

}