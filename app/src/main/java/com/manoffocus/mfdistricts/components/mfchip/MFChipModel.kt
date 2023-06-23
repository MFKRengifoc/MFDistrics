package com.manoffocus.mfdistricts.components.mfchip

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat

data class MFChipModel(
    val title_text: String? = "Titulo",
    val value_text: String? = "Valor",
    val icon: Int? = null,
    val contentDescription : String = "MFChip Accesible"
) {
    val accessibilityListener: (AccessibilityEvent) -> String = {
        val message: String = when (it.eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                "Pulsado $contentDescription"
            }
            AccessibilityEvent.TYPE_VIEW_FOCUSED ->  "Seleccionado $contentDescription"
            else ->  ""

        }
        message
    }
    companion object {
        fun getDrawableFromResInt(ctx: Context, res: Int): Drawable?{
            return ContextCompat.getDrawable(ctx, res)
        }
    }
}