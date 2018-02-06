package com.csod.ksmith.criminalintent

import java.util.*

/**
 * Created by ksmith on 2/5/18.
 */

data class Crime(var title:String = "", var date:String = "", var solved:Boolean = false,
                 val id: UUID = UUID.randomUUID()) {

}