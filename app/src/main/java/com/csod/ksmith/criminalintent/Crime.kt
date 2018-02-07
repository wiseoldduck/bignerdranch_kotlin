package com.csod.ksmith.criminalintent

import java.util.*


data class Crime(var title:String = "", var date:String = "", var solved:Boolean = false,
                 var requiredPolice: Boolean = false, val id: UUID = UUID.randomUUID()) {

}