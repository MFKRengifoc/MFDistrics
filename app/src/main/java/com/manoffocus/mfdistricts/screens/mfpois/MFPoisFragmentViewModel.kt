package com.manoffocus.mfdistricts.screens.mfpois

import androidx.lifecycle.ViewModel
import com.manoffocus.mfdistricts.data.Resource
import com.manoffocus.mfdistricts.models.MFDistrictRequest
import com.manoffocus.mfdistricts.repository.MFDistrictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MFPoisFragmentViewModel @Inject constructor(
    private val mfDistrictRepository: MFDistrictRepository
): ViewModel() {
    fun getDistrictData(): StateFlow<Resource<MFDistrictRequest>> {
        return mfDistrictRepository.getDistrictData()
    }
    companion object {
        const val TAG = "MFPoisFragmentViewModel"
    }
}