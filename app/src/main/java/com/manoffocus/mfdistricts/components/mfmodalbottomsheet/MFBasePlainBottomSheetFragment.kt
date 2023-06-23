package com.manoffocus.mfdistricts.components.mfmodalbottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_RESID = "resid"

/**
 * A simple [Fragment] subclass.
 * Use the [MFBasePlainBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MFBasePlainBottomSheetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var resId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            resId = it.getInt(ARG_RESID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(resId!!, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param resId Parameter 1.
         * @return A new instance of fragment MFBasePlainBottomSheetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(resId: Int) =
            MFBasePlainBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_RESID, resId)
                }
            }
    }
}