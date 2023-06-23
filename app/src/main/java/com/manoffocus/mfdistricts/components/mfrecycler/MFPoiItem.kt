package com.manoffocus.mfdistricts.components.mfrecycler

import android.graphics.drawable.Drawable

data class MFPoiItem(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val icon: Int,
    val likes: Int
)