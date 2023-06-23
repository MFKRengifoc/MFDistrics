package com.manoffocus.mfdistricts.utils

import android.annotation.SuppressLint
import com.manoffocus.mfdistricts.components.mftabpager.mftabeventsectionfragment.MFEvent
import com.manoffocus.mfdistricts.components.mftabpager.mftabnewsectionfragment.MFNew
import java.text.SimpleDateFormat
import java.util.Calendar

class Mock {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getMockEvents(): List<MFEvent>{
            return arrayListOf(
                MFEvent(
                    Utils.getFormattedDate(),
                    "Espacio de trabajo",
                    Utils.getFormattedDate()
                ),
                MFEvent(
                    Utils.getFormattedDate(),
                    "Plaza de la esperanza",
                    Utils.getFormattedDate()
                ),
                MFEvent(
                    Utils.getFormattedDate(),
                    "Carretera de la contratación",
                    Utils.getFormattedDate()
                ),
            )
        }
        fun getMockNews(): List<MFNew>{
            return arrayListOf(
                MFNew(
                    Utils.getFormattedDate(),
                    "Nuevo puesto de trabajo",
                    Utils.getFormattedDate()
                ),
                MFNew(
                    Utils.getFormattedDate(),
                    "Buenas practicas en el mundo Android",
                    Utils.getFormattedDate()
                ),
                MFNew(
                    Utils.getFormattedDate(),
                    "Trabajar duro en mortal",
                    Utils.getFormattedDate()
                ),
            )
        }
    }
}