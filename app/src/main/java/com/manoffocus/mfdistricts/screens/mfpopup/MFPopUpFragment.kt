package com.manoffocus.mfdistricts.screens.mfpopup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.components.mfmaps.MapsContract
import com.manoffocus.mfdistricts.components.mfmaps.MapsPresenter
import com.manoffocus.mfdistricts.components.mfplayer.MFPlayer
import com.manoffocus.mfdistricts.components.mftabpager.MFTabPager
import com.manoffocus.mfdistricts.databinding.FragmentMfPopupBinding
import com.manoffocus.mfdistricts.models.Poi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MFPopUpFragment : DialogFragment(),
    View.OnClickListener,
    MapsContract.CompleteView
{
    private var _binding: FragmentMfPopupBinding? = null
    private val binding get() = _binding!!
    private var idPoi : Int = -1
    private var popUpListener : OnDismissPopUpListener? = null
    private lateinit var mfPopUpFragmentViewModel: MFPopUpFragmentViewModel
    private var mapsBasePresenter: MapsContract.BasePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idPoi = it.getInt(ID_POI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMfPopupBinding.inflate(inflater, container, false)
        mfPopUpFragmentViewModel = ViewModelProvider(this)[MFPopUpFragmentViewModel::class.java]
        mapsBasePresenter = MapsPresenter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mfPopupCloseBut.setOnClickListener(this)
        mfPopUpFragmentViewModel.getDistrictDataById(idPoi){ poi ->
            poi?.let { p ->
                binding.mfPopupTitle.externalSetText(p.name.uppercase())
                binding.mfPopupIcon.setImageUrl(p.category.marker_icon.url)
                binding.mfPopupImagePlaceholder.setImageUrl(p.image.url)
                binding.mfFragmentPopupDesc.externalSetText(p.description)
                binding.mfFragmentPopupChip.externalSetChipValueText("${p.likes_count}")
                listEventsAndNews(p)
                initPlayer(p)
            }
        }
    }

    private fun listEventsAndNews(poi: Poi){
        val pagerFragment = MFTabPager.newInstance()
        val fm = childFragmentManager.beginTransaction()
        fm.replace(R.id.mf_tab_pager_fragment, pagerFragment)
        fm.commit()
    }

    private fun initPlayer(poi: Poi){
        val playerFragment = MFPlayer.newInstance(poi.audio.url)
        val fm = childFragmentManager.beginTransaction()
        fm.replace(R.id.mf_player_box, playerFragment)
        fm.commit()
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    fun setOnDismissModalListener(listener: OnDismissPopUpListener){
        if (popUpListener == null){
            popUpListener = listener
        }
    }
    interface OnDismissPopUpListener{
        fun onClose()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.mf_popup_close_but -> {
                popUpListener?.onClose()
            }
        }
    }

    override val fragmentMan: FragmentManager
        get() = childFragmentManager
    override val ctx: Context
        get() = requireContext()

    override fun onFragmentMapReady(googleMap: GoogleMap) {
        mfPopUpFragmentViewModel.getDistrictDataById(idPoi) { poi ->
            poi?.let { p ->
                mapsBasePresenter?.addPoisTopMap(listOf(p))
                mapsBasePresenter?.setCameraByLocation(LatLng(p.latitude, p.longitude), null)
            }
        }
    }

    override fun onMarkerClicked(idPoi: Int) {
    }
    override fun openPoi(idPoi: Int) {
    }

    companion object {
        const val TAG = "MFPopUpFragment"
        const val ID_POI = "id_poi"
        fun newInstance(idPoi: Int): MFPopUpFragment{
            return MFPopUpFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID_POI, idPoi)
                }
            }
        }
    }
}