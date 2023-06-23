package com.manoffocus.mfdistricts.screens.mfmaps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.manoffocus.mfdistricts.components.interfaces.OnPoiListener
import com.manoffocus.mfdistricts.components.mfmaps.MapsContract
import com.manoffocus.mfdistricts.components.mfmaps.MapsPresenter
import com.manoffocus.mfdistricts.databinding.FragmentMfMapsBinding
import com.manoffocus.mfdistricts.models.MFDistrictRequest
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
    
    private lateinit var districtData : MFDistrictRequest
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
                    districtData = data
                    val coors = mfMapsFragmentViewModel.filterAround(data.coordinates)
                    mapsBasePresenter?.moveCameraTo(coors)
                    mapsBasePresenter?.addPoisTopMap(data.pois)
                    mapsBasePresenter?.addEventsTopMap(data.events)
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

    override fun onMarkerClicked(idPoi: Int) {
        val pois = districtData.pois
        val poisByCoors = pois.find { it.id == idPoi }
        poisByCoors?.let {
            mapsBasePresenter?.addMarkerToMapById(it.id)
        }
    }
    override fun openPoi(idPoi: Int) {
        onPoiListener?.onChosen(idPoi)
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