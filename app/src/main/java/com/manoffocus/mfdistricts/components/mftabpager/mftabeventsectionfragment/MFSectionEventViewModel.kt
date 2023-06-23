package com.manoffocus.mfdistricts.components.mftabpager.mftabeventsectionfragment

import androidx.lifecycle.ViewModel
import com.manoffocus.mfdistricts.utils.Mock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MFSectionEventViewModel @Inject constructor() : ViewModel() {
    private val _mfevents = MutableStateFlow(Mock.getMockEvents())
    private val mfevents : StateFlow<List<MFEvent>> get() = _mfevents.asStateFlow()
    fun getEvents(): StateFlow<List<MFEvent>>{
        return mfevents
    }
}