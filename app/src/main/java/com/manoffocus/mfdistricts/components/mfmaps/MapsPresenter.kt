    package com.manoffocus.mfdistricts.components.mfmaps

    import android.graphics.Bitmap
    import android.util.Log
    import com.google.android.gms.maps.GoogleMap
    import com.google.android.gms.maps.model.*
    import com.manoffocus.mfdistricts.R
    import com.manoffocus.mfdistricts.models.Event
    import com.manoffocus.mfdistricts.models.Poi


    class MapsPresenter internal constructor(var mView: MapsContract.CompleteView, resId: Int = R.id.map) :
        MapsContract.Presenter {
        private lateinit var mapsFragment : MFMaps
        init {
            try {
                mapsFragment = mView.fragmentMan.findFragmentById(resId) as MFMaps
                mapsFragment.setPresenter(this)
            } catch (e: Exception) {
                Log.d(TAG, "ERROR: ${e.message}")
            }
        }

        override fun addPoiMarkerToMap(poi: Poi, bitmap: Bitmap) {
            mapsFragment.addPoiMarkerToMap(poi, bitmap)
        }

        override fun addEventMarkerToMap(event: Event, bitmap: Bitmap) {
            mapsFragment.addEventMarkerToMap(event, bitmap)
        }

        override fun drawPolygon(coordinates: List<LatLng>) {
            if (coordinates.size < 4) return
            mapsFragment.drawPolygon(coordinates)
        }

        override fun moveCameraTo(coordinates: List<LatLng>) {
            mapsFragment.moveCameraTo(coordinates)
        }

        override fun onMarkerClicked(marker: MFMarkerType<Int>, selected: Boolean) {
            mView.onMarkerClicked(marker, selected)
        }

        override fun showInfoMarker(marker: MFMarkerType<Int>, boxBitmap: Bitmap, iconBitmap: Bitmap) {
            mapsFragment.showInfoMarker(marker, boxBitmap, iconBitmap)
        }

        override fun onFragmentMapReady(googleMap: GoogleMap) {
            mView.onFragmentMapReady(googleMap = googleMap)
        }

        override fun openPoi(idPoi: Int) {
            mView.openPoi(idPoi)
        }

        override fun openEvent(eventId: Int) {
            mView.openEvent(eventId)
        }

        override fun setCameraByLocation(latLng: LatLng, zoom: Float?) {
            mapsFragment.setCameraByLocation(latLng, zoom)
        }

        override fun centerMapByPoints(origin: LatLng, destination: LatLng) {
            mapsFragment.centerMapByPoints(origin, destination)
        }
        override fun clearMap() {
            mapsFragment.clearMap()
        }
        companion object {
            const val TAG = "MapsPresenter"
        }
    }