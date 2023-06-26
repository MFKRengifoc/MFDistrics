package com.manoffocus.mfdistricts.components.mfmaps

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.easywaylocation.draw_path.DirectionUtil
import com.example.easywaylocation.draw_path.PolyLineDataBean
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.manoffocus.mfdistricts.BuildConfig
import com.google.maps.android.SphericalUtil
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.MfMapsFragmentBinding
import com.manoffocus.mfdistricts.models.Event
import com.manoffocus.mfdistricts.models.Poi
import kotlin.math.log10


class MFMaps : Fragment(),
    DirectionUtil.DirectionCallBack,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveCanceledListener,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener,
    MapsContract.MapsPresenter
{
    private var api_key : String? = null
    private var _binding : MfMapsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var gmap : GoogleMap
    private var wayPoints : ArrayList<LatLng> = ArrayList()
    private var basePresenter: MapsContract.Presenter? = null
    private var defaultZoom = 15F
    private var defaultZindex = 0F
    private var markers: ArrayList<Pair<MFMarkerType<Int>, Marker>> = arrayListOf()
    private var selectedMarker: Pair<MFMarkerType<Int>, Marker>? = null
    private var overlayMarker: Pair<MFMarkerType<Int>, Marker>? = null
    private var floatingMarker: Pair<MFMarkerType<Int>, Marker>? = null

    @SuppressLint("PotentialBehaviorOverride")
    private val callback = OnMapReadyCallback { googleMap ->
        gmap = googleMap
        gmap.also { map ->
            basePresenter?.onFragmentMapReady(map)
            map.setOnCameraIdleListener(this)
            map.setOnCameraMoveStartedListener(this)
            map.setOnCameraMoveListener(this)
            map.setOnCameraMoveCanceledListener(this)
            map.setOnMarkerClickListener(this)
        }
        gmap.setOnMapClickListener(this)
        setMapStyle(R.raw.style_night)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MfMapsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            api_key = BuildConfig.GOOGLE_API_KEY
        } catch (e: Exception){
            Log.d(TAG, "onViewCreated: ${e.message}")
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setMapStyle(style: Int) {
        try {
            val success = gmap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), style)
            )
        } catch (e: Resources.NotFoundException) {
            Log.d("MAPAS", "Error ${e}")
        }
    }

    override fun setCameraByLocation(latLng: LatLng, zoom: Float?){
        val finalZoom = zoom?: defaultZoom
        gmap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder().target(latLng).zoom(finalZoom).build()
            )
        )
    }

    override fun clearMap(){
        wayPoints.clear()
        gmap.clear()
    }

    override fun addPoiMarkerToMap(poi: Poi, bitmap: Bitmap) {
        val latLng = LatLng(poi.latitude, poi.longitude)
        markers.add(Pair(MFMarkerType.PoiMarker(poi.id), addMarkerToMapByBitMap(bitmap, latLng, "", defaultZindex)))
    }

    override fun addEventMarkerToMap(event: Event, bitmap: Bitmap) {
        val latLng = LatLng(event.latitude.toDouble(), event.longitude.toDouble())
        markers.add(Pair(MFMarkerType.EventMarker(event.id), addMarkerToMapByBitMap(bitmap, latLng, "", defaultZindex)))
    }

    override fun showInfoMarker(marker: MFMarkerType<Int>, boxBitmap: Bitmap, iconBitmap: Bitmap) {
        val targetMarker = markers.find { pair ->
            pair.first.id == marker.id
        }
        targetMarker?.let { target ->
            selectedMarker?.let { selected ->
                val coors = LatLng(target.second.position.latitude, target.second.position.longitude)
                val newCoors = SphericalUtil.computeOffset(coors, 100.0, 0.0)
                floatingMarker = Pair(marker, addMarkerToMapByBitMap(boxBitmap, newCoors, "", 10F))
                overlayMarker = Pair(marker, addMarkerToMapByBitMap(iconBitmap, coors, "", 10F))
                selected.second.alpha = 0F
            }
        }
    }

    override fun addMarkerToMapByBitMap(bitmap: Bitmap, coors: LatLng, title: String, zindex: Float) : Marker {
        val marker = gmap.addMarker(
            MarkerOptions().position(coors).icon(
                BitmapDescriptorFactory.fromBitmap(bitmap)
            ).title(title)) as Marker
        marker.zIndex = zindex
        return marker
    }

    override fun restoreMarker() {
        selectedMarker?.let { selected ->
            selected.second.alpha = 1F
        }
        floatingMarker?.let { floating ->
            floating.second.remove()
        }
        overlayMarker?.let { overlay ->
            overlay.second.remove()
        }
    }
    override fun drawPolygon(coordinates: List<LatLng>){
        val polyline1: Polyline = gmap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(coordinates)
        )
    }
    override fun moveCameraTo(coordinates: List<LatLng>){
        val builder = LatLngBounds.Builder()
        for (coordinate in coordinates) {
            builder.include(coordinate)
        }
        val bounds = builder.build()
        gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    fun setPresenter(basePresenter: MapsContract.Presenter){
        this.basePresenter = basePresenter
    }

    override fun centerMapByPoints(origin: LatLng, destination: LatLng){
        val yCurrentLatLng = LatLng(origin.latitude, destination.longitude)
        val xCurrentLatLng = LatLng(destination.latitude, origin.longitude)
        val areaPoints : ArrayList<LatLng> = ArrayList()
        areaPoints.add(origin)
        areaPoints.add(yCurrentLatLng)
        areaPoints.add(destination)
        areaPoints.add(xCurrentLatLng)
        val area = SphericalUtil.computeArea(areaPoints) / 1000000

        val zoom : Float = area.let { dist ->
            var z = 15F
            if (area < 1) {
                z+= area.toFloat()
            } else {
                z-=1 + log10(area).toFloat()
            }
            z
        }
        val midPoint = LatLng((origin.latitude + destination.latitude)/2, (origin.longitude + destination.longitude)/2)
        setCameraByLocation(latLng = midPoint, zoom)
    }

    override fun onMapClick(point: LatLng) {
        restoreMarker()
        selectedMarker = null
        floatingMarker = null
        overlayMarker = null
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val target = markers.find { pair ->
            pair.second == marker
        }
        target?.let { tar ->
            selectedMarker?.let { selected ->
                if (selected.first.id != tar.first.id){
                    restoreMarker()
                    selectedMarker = tar
                    basePresenter?.onMarkerClicked(tar.first, false)
                }
            }
            if (selectedMarker == null){
                selectedMarker = tar
                basePresenter?.onMarkerClicked(tar.first, false)
            }
        }
        floatingMarker?.let { pair ->
            if (pair.second == marker){
                basePresenter?.onMarkerClicked(pair.first, true)
            }
        }
        return false
    }

    override fun onCameraMove() {
        gmap.setOnCameraMoveListener {

        }
    }

    override fun pathFindFinish(
        polyLineDetailsMap: HashMap<String, PolyLineDataBean>,
        polyLineDetailsArray: ArrayList<PolyLineDataBean>
    ) {

    }

    override fun onCameraMoveStarted(movementAction: Int) {
        when {

        }
    }

    override fun onCameraMoveCanceled() {
    }

    override fun onCameraIdle() {
    }

    companion object {
        const val TAG = "MapsFragment"
        fun newInstance(): MFMaps {
            return MFMaps()
        }
    }
}
sealed class MFMarkerType<T>(var id: T){
    class PoiMarker<T>(id: T): MFMarkerType<T>(id)
    class EventMarker<T>(id: T) : MFMarkerType<T>(id)
}