package com.manoffocus.mfdistricts.models

data class Event(
    val address: String,
    val category: Category,
    val description: String,
    val external_link: Any,
    val featured: Boolean,
    val from_datetime: String,
    val gallery_images: List<GalleryImageX>,
    val id: Int,
    val latitude: String,
    val like_it: Boolean,
    val longitude: String,
    val place: String?,
    val poi: Poi,
    val title: String,
    val to_datetime: String
)