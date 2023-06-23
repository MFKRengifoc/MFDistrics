package com.manoffocus.mfdistricts.components.mfmodalbottomsheet

data class MFTripInfoModel(
    var originName: String,
    var destinationName: String,
    var distance: Double,
    var time: Double,
    var minMaxPrices: DoubleArray
){
    companion object {
        const val ORIGIN_NAME_TAG = "origin_name_tag"
        const val DESTINATION_NAME_TAG = "destination_tag"
        const val DISTANCE_TAG = "distance_tag"
        const val TIME_TAG = "time_tag"
        const val MIN_MAX_PRICES_TAG = "min_max_prices_tag"
    }
}
