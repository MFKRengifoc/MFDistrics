package com.manoffocus.mfdistricts.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.components.interfaces.OnPoiListener
import com.manoffocus.mfdistricts.components.mfmaps.MFMapsMarker
import com.manoffocus.mfdistricts.databinding.ActivityHomeBinding
import com.manoffocus.mfdistricts.screens.mfmaps.MFMapsFragment
import com.manoffocus.mfdistricts.screens.mfpois.MFPoisFragment
import com.manoffocus.mfdistricts.screens.mfpopup.MFPopUpFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private var showMaps = true
    private lateinit var mfMapsFragment: MFMapsFragment
    private lateinit var mfPoisFragment: MFPoisFragment


    private var onPoiListener = object : OnPoiListener{
        override fun onChosen(idPoi: Int) {
            showPopupWindow(idPoi)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeActivityViewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]
        homeActivityViewModel.getDistrictById(2)
        mfMapsFragment = supportFragmentManager.findFragmentById(R.id.mf_maps_fragment) as MFMapsFragment
        mfMapsFragment.setOnPoiListener(onPoiListener)
        mfPoisFragment = supportFragmentManager.findFragmentById(R.id.mf_pois_fragment) as MFPoisFragment
        mfPoisFragment.setOnPoiListener(onPoiListener)
        showAndHideFragment(mfMapsFragment, mfPoisFragment)
        binding.homeActivityListBut.setOnClickListener(this)
        lifecycleScope.launch {
            homeActivityViewModel.getDistrictData().collect { dataReq ->
                dataReq.data?.let { data ->
                    val title = homeActivityViewModel.splitTitle(data.name)
                    val poisCount = data.pois_count
                    changeTitleName(title)
                    changePoisCount(poisCount)
                }
            }
        }
    }
    private fun hideFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.hide(fragment)
        fragmentTransaction.commit()
    }
    private fun showFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.show(fragment)
        fragmentTransaction.commit()
    }

    private fun showAndHideFragment(show: Fragment, hide: Fragment){
        showFragment(show)
        hideFragment(hide)
    }

    private fun changeTitleName(newTitle: String?){
        newTitle?.let { title ->
            binding.homeActivityPlaceName.externalSetText(title)
        }
    }
    private fun changePoisCount(poisCount: Int){
        binding.homeActivityPointsCount.externalSetChipValueText("$poisCount")
    }

    private fun showPopupWindow(idPoi: Int) {
        val mfPopUpFragment = MFPopUpFragment.newInstance(idPoi)
        mfPopUpFragment.setOnDismissModalListener(object : MFPopUpFragment.OnDismissPopUpListener {
            override fun onClose() {
                mfPopUpFragment.dismiss()
            }
        })
        mfPopUpFragment.show(supportFragmentManager, MFPopUpFragment.TAG)
    }

    private fun updateListBox(){
        if (!showMaps){
            binding.homeActivityListBox.setBackgroundColor(ContextCompat.getColor(this, R.color.home_activity_list_deactive_bg))
        } else {
            binding.homeActivityListBox.setBackgroundColor(ContextCompat.getColor(this, R.color.home_activity_list_active_bg))
        }
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.home_activity_list_but -> {
                updateListBox()
                if (showMaps){
                    showAndHideFragment(mfPoisFragment, mfMapsFragment)
                    showMaps = false
                } else {
                    showAndHideFragment(mfMapsFragment, mfPoisFragment)
                    showMaps = true
                }
            }
        }
    }
    companion object {
        const val TAG = "HomeActivity"
    }
}