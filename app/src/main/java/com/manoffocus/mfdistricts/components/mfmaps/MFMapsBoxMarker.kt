package com.manoffocus.mfdistricts.components.mfmaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.MfMapsBoxMarkerBinding
import com.manoffocus.mfdistricts.databinding.MfMapsEventBoxMarkerBinding

class MFMapsBoxMarker : ConstraintLayout {
    private lateinit var binding : MfMapsBoxMarkerBinding
    private var mfMarkerBoxTitle: String? = null
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
            R.styleable.MFMapsBoxMarker,
            ZERO,
            ZERO,
        ).apply {
            mfMarkerBoxTitle = getString(R.styleable.MFMapsBoxMarker_mf_marker_box_title)
            recycle()
        }
    }
    private fun initView(){
        binding = MfMapsBoxMarkerBinding.inflate(LayoutInflater.from(context), this)
        setMarkerWidthHeight(R.dimen.mf_maps_box_marker_width_m, R.dimen.mf_maps_box_marker_height_m)
        setTitle(mfMarkerBoxTitle)
    }
    private fun setTitle(title: String?){
        title?.let { t->
            binding.mfMapsMarkerTitle.externalSetText(t)
        }
    }
    fun externalSetTitle(title: String?){
        title?.let { t->
            setTitle(t)
        }
    }
    private fun setMarkerWidthHeight(newWidth: Int, newHeight: Int){
        val cp = ViewGroup.LayoutParams(resources.getDimensionPixelSize(newWidth), resources.getDimensionPixelSize(newHeight))
        binding.root.layoutParams = cp
        layout(left, top, right, bottom)
        measure(cp.width, cp.height)
    }
    fun getBitMap(): Bitmap{
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
        @BindingAdapter("mf_marker_box_title")
        @JvmStatic
        fun setTitle(view: MFMapsBoxMarker, title: String){
            view.setTitle(title)
        }
    }
}