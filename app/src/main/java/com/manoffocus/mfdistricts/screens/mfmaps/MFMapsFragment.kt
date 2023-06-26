package com.manoffocus.mfdistricts.screens.mfmaps

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.components.interfaces.OnPoiListener
import com.manoffocus.mfdistricts.components.mfmaps.MFMapsBoxMarker
import com.manoffocus.mfdistricts.components.mfmaps.MFMapsEventBoxMarker
import com.manoffocus.mfdistricts.components.mfmaps.MFMapsEventMarker
import com.manoffocus.mfdistricts.components.mfmaps.MFMapsMarker
import com.manoffocus.mfdistricts.components.mfmaps.MFMarkerType
import com.manoffocus.mfdistricts.components.mfmaps.MapsContract
import com.manoffocus.mfdistricts.components.mfmaps.MapsPresenter
import com.manoffocus.mfdistricts.databinding.FragmentMfMapsBinding
import com.manoffocus.mfdistricts.models.MFDistrictRequest
import com.manoffocus.mfdistricts.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MFMapsFragment : Fragment(),
    MapsContract.CompleteView {
    private var _binding: FragmentMfMapsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var mapsBasePresenter: MapsContract.BasePresenter? = null
    private lateinit var mfMapsFragmentViewModel: MFMapsFragmentViewModel
    
    private lateinit var districtData : MutableState<MFDistrictRequest>
    private var onPoiListener : OnPoiListener? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMfMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mfMapsFragmentViewModel = ViewModelProvider(this)[MFMapsFragmentViewModel::class.java]
        mapsBasePresenter = MapsPresenter(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            mfMapsFragmentViewModel.getDistrictData().collect { dataReq ->
                dataReq.data?.let { data ->
                    districtData = mutableStateOf(data)
                    val coors = mfMapsFragmentViewModel.filterAround(data.coordinates)
                    mapsBasePresenter?.moveCameraTo(coors)
                    districtData.value.pois.map { poi ->
                        val icon = MFMapsMarker(ContextThemeWrapper(requireContext(), R.style.MFMapsMarker))
                        icon.getBitMapOfUrlImage(poi.category.marker.url, icon.layoutParams.width, icon.layoutParams.height){ bitmap ->
                            bitmap?.let {
                                mapsBasePresenter?.addPoiMarkerToMap(poi, it)
                            }
                        }
                    }
                    districtData.value.events.map { event ->
                        val icon = MFMapsEventMarker(ContextThemeWrapper(requireContext(), R.style.MFMapsMarker))
                        val day = Utils.getDay(event.from_datetime)
                        val month = Utils.getPartialMonthName(event.from_datetime)
                        icon.externalSetDay("${day}")
                        icon.externalSetMonth(month)
                        val bitmap = icon.getBitMap()
                        mapsBasePresenter?.addEventMarkerToMap(event, bitmap)
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val fragmentMan: FragmentManager
        get() = childFragmentManager
    override val ctx: Context
        get() = requireContext()

    override fun onMarkerClicked(marker: MFMarkerType<Int>, selected: Boolean) {
        if (selected){
            when(marker){
                is MFMarkerType.PoiMarker -> {
                    openPoi(marker.id)
                }
                is MFMarkerType.EventMarker -> {

                }
            }
        } else {
            when(marker){
                is MFMarkerType.PoiMarker -> {
                    val poi = districtData.value.pois.find { poi ->
                        poi.id == marker.id
                    }
                    poi?.let { p ->
                        val boxIcon = MFMapsBoxMarker(ContextThemeWrapper(requireContext(), R.style.MFMapsMarker))
                        boxIcon.externalSetTitle(p.name)
                        val boxBitmap = boxIcon.getBitMap()
                        val icon = MFMapsMarker(ContextThemeWrapper(requireContext(), R.style.MFMapsMarker))
                        icon.getBitMapOfUrlImage(p.category.icon.url, icon.layoutParams.width, icon.layoutParams.height){ bitmap ->
                            bitmap?.let {
                                mapsBasePresenter?.showInfoMarker(marker, boxBitmap, it)
                            }
                        }
                    }
                }
                is MFMarkerType.EventMarker -> {
                    val event = districtData.value.events.find { event ->
                        event.id == marker.id
                    }
                    event?.let { ev ->
                        val boxIcon = MFMapsEventBoxMarker(ContextThemeWrapper(requireContext(), R.style.MFMapsMarker))
                        boxIcon.externalSetTitle(ev.title)
                        val hour = Utils.getHours(ev.from_datetime)
                        boxIcon.externalSetTime(hour)
                        event.place?.let { pl ->
                            boxIcon.externalSetPlace(ev.place)
                        } ?: run {
                            boxIcon.externalSetPlace(ev.address)
                        }
                        val boxBitmap = boxIcon.getBitMap()
                        val icon = MFMapsEventMarker(ContextThemeWrapper(requireContext(), R.style.MFMapsMarker))
                        val day = Utils.getDay(event.from_datetime)
                        val month = Utils.getPartialMonthName(event.from_datetime)
                        icon.externalSetDay("${day}")
                        icon.externalSetMonth(month)
                        val bitmap = icon.getBitMap()
                        mapsBasePresenter?.showInfoMarker(marker, boxBitmap, bitmap)
                    }
                }
            }
        }
    }
    override fun openPoi(idPoi: Int) {
        onPoiListener?.onChosen(idPoi)
    }

    override fun openEvent(eventId: Int) {

    }
    override fun onFragmentMapReady(googleMap: GoogleMap) {}
    fun setOnPoiListener(listener: OnPoiListener){
        if (onPoiListener == null){
            onPoiListener = listener
        }
    }
    companion object {
        private const val TAG = "MFMapsFragment"
    }
}