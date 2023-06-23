package com.manoffocus.mfdistricts.components.mftabpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manoffocus.mfdistricts.components.mftabpager.mftabeventsectionfragment.MFEvent
import com.manoffocus.mfdistricts.components.mftabpager.mftabeventsectionfragment.MFTabEventSectionFragment
import com.manoffocus.mfdistricts.components.mftabpager.mftabnewsectionfragment.MFNew
import com.manoffocus.mfdistricts.components.mftabpager.mftabnewsectionfragment.MFTabNewsSectionFragment
import com.manoffocus.mfdistricts.databinding.FragmentMfTabPagerBinding

class MFTabPager : Fragment() {

    private var _binding: FragmentMfTabPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMfTabPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageAdapter = MFTabPagerAdapter(childFragmentManager)
        val eventsFragment = MFTabEventSectionFragment.newInstance()
        val newsFragment = MFTabNewsSectionFragment.newInstance()
        pageAdapter.addFragment(eventsFragment, "Events")
        pageAdapter.addFragment(newsFragment, "News")
        binding.mfTabPagerViewpager.adapter = pageAdapter
        binding.mfTabPagerLayout.setupWithViewPager(binding.mfTabPagerViewpager)
    }

    companion object {
        const val TAG = "MFTabPager"
        const val EVENTS_POI = "events_poi"
        const val NEWS_POI = "news_poi"
        @JvmStatic
        fun newInstance() = MFTabPager()
    }
}