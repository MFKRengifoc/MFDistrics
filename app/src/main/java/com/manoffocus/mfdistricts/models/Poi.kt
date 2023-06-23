package com.manoffocus.mfdistricts.models

data class Poi(
    val audio: Audio,
    val category: CategoryX,
    val description: String,
    val events: List<Event>,
    val events_count: Int,
    val gallery_images: List<GalleryImageX>,
    val id: Int,
    val image: Image,
    val latitude: Double,
    val like_it: Boolean,
    val likes_count: Int,
    val longitude: Double,
    val name: String,
    val news: List<Event>,
    val news_count: Int,
    val premium: Boolean,
    val video: Any
)