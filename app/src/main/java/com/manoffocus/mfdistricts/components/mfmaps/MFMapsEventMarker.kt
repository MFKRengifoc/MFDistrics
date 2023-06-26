package com.manoffocus.mfdistricts.components.mfmaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.MfMapsEventMarkerBinding

class MFMapsEventMarker : ConstraintLayout {
    private lateinit var binding :  MfMapsEventMarkerBinding
    private var mfMapsMarkerDay : String? = null
    private var mfMapsMarkerMonth : String? = null
    constructor (context: Context): super(context){
        initialize(null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs)
    }
    private fun initialize(attrs: AttributeSet?) {
        loadAttrs(attrs)
        initView()
    }
    private fun loadAttrs(attrs: AttributeSet?){
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MFMapsEventMarker,
            ZERO,
            ZERO,
        ).apply {
            mfMapsMarkerDay = getString(R.styleable.MFMapsEventMarker_mf_marker_event_day)
            mfMapsMarkerMonth = getString(R.styleable.MFMapsEventMarker_mf_marker_event_month)
            recycle()
        }
    }
    private fun initView(){
        binding = MfMapsEventMarkerBinding.inflate(LayoutInflater.from(context), this)
        setMarkerWidthHeight(R.dimen.mf_maps_event_marker_width_s, R.dimen.mf_maps_event_marker_height_s)
    }
    private fun setDay(day: String?){
        day?.let { d ->
            binding.mfEventMarkerDay.externalSetText(d)
        }
    }
    fun externalSetDay(day: String?){
        day?.let { d ->
            setDay(d)
        }
    }
    private fun setMonth(month: String?){
        month?.let { m ->
            binding.mfEventMarkerMonth.externalSetText(m)
        }
    }
    fun externalSetMonth(month: String?){
        month?.let { m ->
            setMonth(m)
        }
    }
    private fun setMarkerWidthHeight(newWidth: Int, newHeight: Int){
        val cp = ViewGroup.LayoutParams(resources.getDimensionPixelSize(newWidth), resources.getDimensionPixelSize(newHeight))
        binding.root.layoutParams = cp
        layout(left, top, right, bottom)
        measure(cp.width, cp.height)
    }
    fun getBitMap(): Bitmap {
        val bitmap = Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout(left, top, right, bottom)
        measure(measuredWidth, height)
        draw(canvas)
        return bitmap
    }
    companion object {
        private const val ZERO = 0
        private val EMPTY_STRING = ""
        @BindingAdapter("mf_marker_event_day")
        @JvmStatic
        fun setUrl(view: MFMapsEventMarker, day: String?){
            view.setDay(day)
        }
        @BindingAdapter("mf_marker_event_month")
        @JvmStatic
        fun setMonth(view: MFMapsEventMarker, month: String?){
            view.setMonth(month)
        }
    }
}