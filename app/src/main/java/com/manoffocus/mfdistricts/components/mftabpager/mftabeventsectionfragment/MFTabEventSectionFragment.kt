package com.manoffocus.mfdistricts.components.mftabpager.mftabeventsectionfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.manoffocus.mfdistricts.databinding.FragmentMfTabSectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MFTabEventSectionFragment : Fragment() {

    private var _binding: FragmentMfTabSectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var mfTabEventViewModel: MFSectionEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMfTabSectionBinding.inflate(inflater, container, false)
        mfTabEventViewModel = ViewModelProvider(this)[MFSectionEventViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            mfTabEventViewModel.getEvents().collect { data ->
                data?.let { d ->
                    binding.mfTabSectionRecycler.layoutManager = LinearLayoutManager(requireContext())
                    binding.mfTabSectionRecycler.adapter = MFTabEventsAdapter(d)
                    binding.mfTabSectionRecycler.apply {
                        addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MFTabEventSectionFragment"
        fun newInstance(): MFTabEventSectionFragment {
            return MFTabEventSectionFragment()
        }
    }
}