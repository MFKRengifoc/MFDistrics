package com.manoffocus.mfdistricts.components.mfmaps

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.easywaylocation.draw_path.DirectionUtil
import com.example.easywaylocation.draw_path.PolyLineDataBean
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
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
    private var directionUtil : DirectionUtil? = null
    private var basePresenter: MapsContract.Presenter? = null
    private var defaultZoom = 15F
    private var poiMarkersMap: ArrayList<Pair<Marker, Poi>> = arrayListOf()
    private var eventMarkersMap: ArrayList<Pair<Marker, Event>> = arrayListOf()
    private var selectedMarker: Pair<Marker, Poi>? = null

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
            api_key = getString(R.string.google_maps_key)
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

    override fun addPoisTopMap(listOfPois: List<Poi>) {
        listOfPois.map {  poi ->
            val coors = LatLng(poi.latitude, poi.longitude)
            val icon = poi.category.marker
            val width = icon.width.toInt()
            val height = icon.height.toInt()
            getBitMapOfUrlImage(icon.url, width, height){ bitmap ->
                bitmap?.let {
                    val marker = addMarkerToMapByBitMap(bitmap, coors, poi.name)
                    poiMarkersMap.add(Pair(marker, poi))
                }
            }
        }
    }

    override fun addEventsTopMap(listOfEvents: List<Event>) {
        listOfEvents.map {  event ->
            val coors = LatLng(event.latitude.toDouble(), event.longitude.toDouble())
            val icon = event.category.icon
            val width = event.category.icon.width.toInt()
            val height = event.category.icon.height.toInt()
            getBitMapOfUrlImage(icon.url, width, height){ bitmap ->
                bitmap?.let {
                    val marker = addMarkerToMapByBitMap(bitmap, coors, event.title)
                    eventMarkersMap.add(Pair(marker, event))
                }
            }
        }
    }

    override fun addMarkerToMapById(idPoi: Int) {
        val poi = poiMarkersMap.find { it.second.id == idPoi }
        val indexed = poiMarkersMap.indexOf(poi)
        poi?.let { p ->
            val markerUrl = if (selectedMarker == null) p.second.category.icon.url else poi.second.category.marker.url
            val w = p.second.category.marker.width.toInt()
            val h = p.second.category.marker.height.toInt()
            val coors = LatLng(p.second.latitude, p.second.longitude)
            getBitMapOfUrlImage(markerUrl, w, h){ bitmap ->
                bitmap?.let {
                    val marker = addMarkerToMapByBitMap(it, coors, p.second.name)
                    poi.first.remove()
                    poiMarkersMap[indexed] = Pair(marker, poi.second)
                    selectedMarker = Pair(marker, poi.second)
                }
            }
        }

    }

    override fun addMarkerToMapByBitMap(bitmap: Bitmap, coors: LatLng, title: String) : Marker {
        return gmap.addMarker(
            MarkerOptions().position(coors).icon(
                BitmapDescriptorFactory.fromBitmap(bitmap)
            ).title(title)) as Marker
    }

    override fun restoreMarker(idPoi: Int) {
        val poi = poiMarkersMap.find { it.second.id == idPoi }
        val indexed = poiMarkersMap.indexOf(poi)
        poi?.let { p ->
            val markerUrl = p.second.category.marker.url
            val w = p.second.category.icon.width.toInt()
            val h = p.second.category.icon.height.toInt()
            val coors = LatLng(p.second.latitude, p.second.longitude)
            getBitMapOfUrlImage(markerUrl, w, h){ bitmap ->
                bitmap?.let {
                    val marker = addMarkerToMapByBitMap(it, coors, p.second.name)
                    poi.first.remove()
                    poiMarkersMap[indexed] = Pair(marker, poi.second)
                }
            }
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
        Log.d(TAG, "onMapClick: ${point}")
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val targetMarker = poiMarkersMap.find { it.first == marker }
        var consume = false
        targetMarker?.let { target ->
            selectedMarker.let { selected ->
                if (selected != null) {
                    if (selected.first != target.first){
                        restoreMarker(selected.second.id)
                        selectedMarker = null
                    } else {
                        basePresenter?.openPoi(target.second.id)
                    }
                }
            }
            if (selectedMarker == null){
                consume = true
            }
        }
        if (consume){
            basePresenter?.onMarkerClicked(targetMarker!!.second.id)
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
        directionUtil?.drawPath(WAYPOINTS_TAG)
    }

    override fun onCameraMoveStarted(movementAction: Int) {
        when {

        }
    }

    override fun onCameraMoveCanceled() {
    }

    override fun onCameraIdle() {
    }

    private fun getBitMapOfUrlImage(imageUrl: String, width: Int, height: Int, callback: (Bitmap?) -> Unit){
        Glide.with(requireContext())
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>(width, height) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    callback(null)
                }
            })
    }
    companion object {
        const val TAG = "MapsFragment"
        const val INIT_LAT = "init_lat"
        const val INIT_LONG = "init_long"
        val WAYPOINTS_TAG = TAG.lowercase() + "_waypoints_tag"
        fun newInstance(latLng: LatLng): MFMaps {
            val bundle = Bundle()
            bundle.putDouble(INIT_LAT, latLng.latitude)
            bundle.putDouble(INIT_LONG, latLng.longitude)
            val fragment = MFMaps()
            fragment.arguments = bundle
            return fragment
        }
    }
}