package com.manoffocus.mfdistricts.components.mftabpager.mftabnewsectionfragment

import androidx.lifecycle.ViewModel
import com.manoffocus.mfdistricts.utils.Mock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MFSectionNewsViewModel @Inject constructor() : ViewModel() {
    private val _mfnews = MutableStateFlow(Mock.getMockNews())
    private val mfnews : StateFlow<List<MFNew>> get() = _mfnews.asStateFlow()
    fun getNews(): StateFlow<List<MFNew>>{
        return mfnews
    }
}