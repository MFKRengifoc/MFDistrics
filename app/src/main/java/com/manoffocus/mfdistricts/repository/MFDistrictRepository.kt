package com.manoffocus.mfdistricts.repository

import android.util.Log
import com.manoffocus.mfdistricts.activities.HomeActivityViewModel
import com.manoffocus.mfdistricts.data.Resource
import com.manoffocus.mfdistricts.models.MFDistrictRequest
import com.manoffocus.mfdistricts.network.PlacesAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class MFDistrictRepository @Inject constructor(
    private val placesAPI: PlacesAPI
) {

    private val _districtRequest = MutableStateFlow<Resource<MFDistrictRequest>>(Resource.Empty())
    private val districtData : StateFlow<Resource<MFDistrictRequest>> = _districtRequest.asStateFlow()

    suspend fun getDistrictDataById(id: Int) : Resource<MFDistrictRequest> {
        return try {
            Resource.Loading(data = true)
            val request = placesAPI.getDistrictsById(id)
            request.let { request ->
                Resource.Loading(data = false)
            }
            Resource.Success(request)
        } catch (socketException: SocketTimeoutException){
            Resource.Error(message = socketException.message.toString())
        } catch (timeOutException: TimeoutException){
            Resource.Error(message = timeOutException.message.toString())
        } catch (exception: Exception){
            Resource.Error(message = exception.message.toString())
        }
    }
    suspend fun getDistrictById(id: Int){
        try {
            when (val response = getDistrictDataById(id)){
                is Resource.Error -> {
                    Log.d(HomeActivityViewModel.TAG, "getDistrictById: ${response.data}")
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        _districtRequest.value = response
                    }
                }
                else -> {

                }
            }
        } catch (ex: Exception){
            Log.d(TAG, "getDistrictById: ${ex}")
        }
    }
    fun getDistrictData() : StateFlow<Resource<MFDistrictRequest>> {
        return districtData
    }
    companion object {
        const val TAG = "MFDistrictRepository"
    }
}