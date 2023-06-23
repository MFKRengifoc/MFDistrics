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
        fun addPoisTopMap(listOfPois: List<Poi>)
        fun addEventsTopMap(listOfEvents: List<Event>)
        fun addMarkerToMapById(idPoi: Int)
        fun drawPolygon(coordinates: List<LatLng>)
        fun moveCameraTo(coordinates: List<LatLng>)
        fun setCameraByLocation(latLng: LatLng, zoom: Float?)
        fun centerMapByPoints(origin: LatLng, destination: LatLng)
        fun clearMap()
    }
    interface Presenter : BasePresenter, MapDrawing, MapsListener {
    }
    interface MapsPresenter : BasePresenter {
        fun addMarkerToMapByBitMap(bitmap: Bitmap, coors: LatLng, title: String) : Marker
        fun restoreMarker(idPoi: Int)
    }
    interface MapDrawing {
        fun onFragmentMapReady(googleMap: GoogleMap)
    }
    interface MapsListener {
        fun onMarkerClicked(idPoi: Int)
        fun openPoi(idPoi: Int)
    }
    interface CompleteView: MapDrawing, MapsListener {
        val fragmentMan: FragmentManager
        val ctx: Context
    }
}