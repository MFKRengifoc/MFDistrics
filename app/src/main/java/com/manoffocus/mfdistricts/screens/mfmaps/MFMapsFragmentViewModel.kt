package com.manoffocus.mfdistricts.screens.mfmaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.LatLng
import com.manoffocus.mfdistricts.data.Resource
import com.manoffocus.mfdistricts.models.MFDistrictRequest
import com.manoffocus.mfdistricts.models.MarkerIcon
import com.manoffocus.mfdistricts.repository.MFDistrictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MFMapsFragmentViewModel @Inject constructor(
    private val mfDistrictRepository: MFDistrictRepository
) : ViewModel() {

    fun getDistrictData(): StateFlow<Resource<MFDistrictRequest>>{
        return mfDistrictRepository.getDistrictData()
    }
    fun filterAround(coorsRaw: String): List<LatLng>{
        val coors = coorsRaw.split(",").toList().windowed(2, 2).map { it.reversed() }
        return coors.map {
            LatLng(it[0].toDouble(), it[1].replace("0.0", "").trim().toDouble())
        }
    }
    companion object {
        const val TAG = "MFMapsFragmentViewModel"
    }
}