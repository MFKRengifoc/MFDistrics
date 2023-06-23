package com.manoffocus.mfdistricts.models

data class MFDistrictRequest(
    val audio: Audio,
    val coordinates: String,
    val events: List<Event>,
    val events_count: Int,
    val free: Boolean,
    val gallery_images: List<GalleryImageX>,
    val id: Int,
    val image: Image,
    val name: String,
    val pois: List<Poi>,
    val pois_count: Int,
    val video: Any
)