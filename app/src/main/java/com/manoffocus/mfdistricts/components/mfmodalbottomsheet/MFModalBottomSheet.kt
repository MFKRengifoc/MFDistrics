package com.manoffocus.mfdistricts.components.mfmodalbottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.components.mfimageview.MFImageView
import com.manoffocus.mfdistricts.components.mflabel.MFLabel
import com.manoffocus.mfdistricts.databinding.MfModalBottomSheetBinding


class MFModalBottomSheet: BottomSheetDialogFragment, View.OnClickListener {
    private var _binding: MfModalBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var modalListener : OnDismissModalListener? = null
    private var inflatedView : Fragment? = null
    private var timed = false
    private var showAcceptButton = true
    private var showCancelButton = true
    private var isClosable = true

    constructor(view: Fragment, timed : Boolean, showAcceptButton: Boolean = true, showCancelButton: Boolean = true, isClosable: Boolean = true){
        this.inflatedView = view
        this.timed = timed
        this.showAcceptButton = showAcceptButton
        this.showCancelButton = showCancelButton
        this.isClosable = isClosable
    }

    private val timer = object : CountDownTimer(CLOSE_TIME_DEFAULT, CLOSE_INTERVAL) {
        override fun onTick(counter: Long) {
            val formatedCounter = Math.nextUp((counter/1000).toDouble()).toInt()
            val counterText : MFLabel = binding.mfModalBottomCounterText
            counterText.externalSetText(formatedCounter.toString())
        }
        override fun onFinish() {
            this@MFModalBottomSheet.cancel()
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        _binding = null
        timer.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MFModalottomSheetDialogTheme)
    }

    override fun getTheme(): Int {
        return R.style.MFModalottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MfModalBottomSheetBinding.inflate(layoutInflater, container, false)
        inflatedView?.let { frag ->
            childFragmentManager.beginTransaction().replace(R.id.mf_modal_bottom_inflated_view, frag).commit()
        }
        binding.fragmentModalBottomSheetAcceptBut.setOnClickListener(this)
        binding.fragmentModalBottomSheetCancelBut.setOnClickListener(this)
        val progressBar : ProgressBar = binding.mfModalBottomProgressbar
        val pro = progressBar.progress
        val anim = ProgressBarAnimation(progressBar, pro.toFloat(), 100F)
        anim.duration = CLOSE_TIME_DEFAULT
        if (timed) {
            timer.start()
            progressBar.startAnimation(anim)
        } else {
            progressBar.visibility = View.INVISIBLE
        }
        if (!showCancelButton) {
            val cancel = binding.fragmentModalBottomSheetCancelBox
            cancel.visibility = View.GONE
        }
        if (!showAcceptButton) {
            val accept = binding.fragmentModalBottomSheetAcceptBox
            accept.visibility = View.GONE
        }
        isCancelable = false
        val minBut : MFImageView = binding.mfModalBottomMinimizeBut
        if (!isClosable) {
            minBut.visibility = View.INVISIBLE
        } else {
            minBut.setOnClickListener(this)
        }
        return binding.root
    }
    fun cancel(){
        modalListener?.let { l ->
            l.onCancel()
        }
    }
    fun setOnDismissModalListener(listener: OnDismissModalListener){
        if (modalListener == null){
            modalListener = listener
        }
    }
    interface OnDismissModalListener{
        fun onAccept()
        fun onCancel()
        fun onClose()
    }
    inner class ProgressBarAnimation(
        private val progressBar: ProgressBar,
        private val from: Float,
        private val to: Float
    ) :
        Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)
            val value = from + (to - from) * interpolatedTime
            progressBar.progress = value.toInt()
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.fragment_modal_bottom_sheet_accept_but -> {
                modalListener?.onAccept()
                dismiss()
            }
            R.id.fragment_modal_bottom_sheet_cancel_but -> {
                modalListener?.onCancel()
                dismiss()
            }
            R.id.mf_modal_bottom_minimize_but -> {
                modalListener?.onClose()
                dismiss()
            }
        }
    }
    companion object {
        const val TAG = "ModalBottomSheet"
        const val CLOSE_TIME_DEFAULT : Long = 15000
        const val CLOSE_INTERVAL : Long = 1000
        fun newInstance(
            viewFragment: Fragment,
            timed : Boolean,
            showAcceptButton: Boolean = true,
            showCancelButton: Boolean = true,
            isClosable: Boolean = true,
            listener: OnDismissModalListener? = null
        ): MFModalBottomSheet {
            val modal = MFModalBottomSheet(viewFragment, timed = timed, showAcceptButton = showAcceptButton, showCancelButton = showCancelButton, isClosable = isClosable)
            listener?.let { list ->
                modal.setOnDismissModalListener(list)
            }
            return modal
        }
    }
}