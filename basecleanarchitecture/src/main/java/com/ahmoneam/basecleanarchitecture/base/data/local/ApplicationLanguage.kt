package com.ahmoneam.basecleanarchitecture.base.data.local

import androidx.annotation.StringDef
import com.ahmoneam.basecleanarchitecture.base.data.local.ApplicationLanguage.Companion.ARABIC
import com.ahmoneam.basecleanarchitecture.base.data.local.ApplicationLanguage.Companion.ENGLISH

@Retention(AnnotationRetention.SOURCE)
@StringDef(ARABIC, ENGLISH)
annotation class ApplicationLanguage {
    companion object {
        const val ARABIC = "ar"
        const val ENGLISH = "en"
    }
}