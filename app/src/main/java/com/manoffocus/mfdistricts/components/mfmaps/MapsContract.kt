package com.manoffocus.mfdistricts.components.mfmaps

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.manoffocus.mfdistricts.models.Event
import com.manoffocus.mfdistricts.models.Poi


interface MapsContract {
    interface BasePresenter {
        fun addPoiMarkerToMap(poi: Poi, bitmap: Bitmap)
        fun addEventMarkerToMap(event: Event, bitmap: Bitmap)
        fun showInfoMarker(marker: MFMarkerType<Int>, boxBitmap: Bitmap, iconBitmap: Bitmap)
        fun drawPolygon(coordinates: List<LatLng>)
        fun moveCameraTo(coordinates: List<LatLng>)
        fun setCameraByLocation(latLng: LatLng, zoom: Float?)
        fun centerMapByPoints(origin: LatLng, destination: LatLng)
        fun clearMap()
    }
    interface Presenter : BasePresenter, MapDrawing, MapsListener {
    }
    interface MapsPresenter : BasePresenter {
        fun addMarkerToMapByBitMap(bitmap: Bitmap, coors: LatLng, title: String, zindex: Float) : Marker
        fun restoreMarker()
    }
    interface MapDrawing {
        fun onFragmentMapReady(googleMap: GoogleMap)
    }
    interface MapsListener {
        fun onMarkerClicked(marker: MFMarkerType<Int>, selected: Boolean)
        fun openPoi(idPoi: Int)
        fun openEvent(eventId: Int)
    }
    interface CompleteView: MapDrawing, MapsListener {
        val fragmentMan: FragmentManager
        val ctx: Context
    }
}