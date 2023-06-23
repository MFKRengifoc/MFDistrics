package com.manoffocus.mfdistricts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.manoffocus.mfdistricts.activities.HomeActivityViewModel
import com.manoffocus.mfdistricts.network.PlacesAPI
import com.manoffocus.mfdistricts.repository.MFDistrictRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MFDistrictNetworkTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var mfPlacesAPI: PlacesAPI
    private lateinit var mfDistrictRepository: MFDistrictRepository

    lateinit var homeActivityViewModel : HomeActivityViewModel

    @Before
    fun setup(){
        hiltAndroidRule.inject()
        mfDistrictRepository = MFDistrictRepository(mfPlacesAPI)
        homeActivityViewModel = HomeActivityViewModel(mfDistrictRepository)
    }

    @Test
    fun loadRequestDistrict(){
        runBlocking(Dispatchers.IO) {
            val res = mfDistrictRepository.getDistrictDataById(2)
        }
    }
}