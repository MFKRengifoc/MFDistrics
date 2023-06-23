package com.manoffocus.mfdistricts.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.manoffocus.mfdistricts.data.Resource
import com.manoffocus.mfdistricts.models.MFDistrictRequest
import com.manoffocus.mfdistricts.repository.MFDistrictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel  @Inject constructor(
    private val mfDistrictRepository: MFDistrictRepository
) : ViewModel() {
    fun getDistrictById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            mfDistrictRepository.getDistrictById(id)
        }
    }

    fun getDistrictData(): StateFlow<Resource<MFDistrictRequest>>{
        return mfDistrictRepository.getDistrictData()
    }

    fun splitTitle(rawTitle: String) : String? {
        val patron = "- (.*)\$".toRegex()
        val matchResult = patron.find(rawTitle)
        return matchResult?.groupValues?.get(1)
    }
    companion object {
        const val TAG = "HomeActivityViewModel"
    }
}