package com.manoffocus.mfdistricts.screens.mfpois

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.components.interfaces.OnPoiListener
import com.manoffocus.mfdistricts.components.interfaces.onItemClickAdapter
import com.manoffocus.mfdistricts.components.mfrecycler.MFAdapter
import com.manoffocus.mfdistricts.components.mfrecycler.MFPoiItem
import com.manoffocus.mfdistricts.databinding.FragmentMfPoisBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [MFPoisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MFPoisFragment : Fragment() {

    private var _binding : FragmentMfPoisBinding? = null
    private val binding get() = _binding!!
    private lateinit var mfPoisFragmentViewModel: MFPoisFragmentViewModel
    private var onPoiListener : OnPoiListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMfPoisBinding.inflate(layoutInflater, container, false)
        mfPoisFragmentViewModel = ViewModelProvider(this)[MFPoisFragmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            mfPoisFragmentViewModel.getDistrictData().collect { dataReq ->
                dataReq.data?.let { data ->
                    val pois = data.pois.map {
                        MFPoiItem(it.id, it.image.url, it.name, R.drawable.mf_like_icon, it.likes_count)
                    }
                    binding.mfPoisFragmentRecycler.layoutManager = LinearLayoutManager(requireContext())
                    binding.mfPoisFragmentRecycler.adapter = MFAdapter(pois, object : onItemClickAdapter {
                        override fun onClickItem(idPoi: Int) {
                            onPoiListener?.onChosen(idPoi)
                        }
                    })
                    binding.mfPoisFragmentRecycler.apply {
                        addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                    }
                }
            }
        }
    }

    fun setOnPoiListener(listener: OnPoiListener){
        if (onPoiListener == null){
            onPoiListener = listener
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MFPoisFragment()
    }
}