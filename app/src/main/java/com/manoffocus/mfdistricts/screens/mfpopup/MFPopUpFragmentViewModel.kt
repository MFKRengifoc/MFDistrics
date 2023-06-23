package com.manoffocus.mfdistricts.screens.mfpopup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manoffocus.mfdistricts.data.Resource
import com.manoffocus.mfdistricts.models.MFDistrictRequest
import com.manoffocus.mfdistricts.models.Poi
import com.manoffocus.mfdistricts.repository.MFDistrictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MFPopUpFragmentViewModel @Inject constructor(
    private val mfDistrictRepository: MFDistrictRepository
) : ViewModel(){
    fun getDistrictDataById(idPoi: Int, callBack: (Poi?) -> Unit) {
        viewModelScope.launch {
            mfDistrictRepository.getDistrictData().collect { dataReq ->
                dataReq.data?.let { data ->
                    val poi = data.pois.find { it.id == idPoi }
                    callBack(poi)
                }
            }
        }
    }
}