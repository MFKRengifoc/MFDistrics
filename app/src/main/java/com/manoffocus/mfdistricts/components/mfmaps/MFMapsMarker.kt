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
import com.manoffocus.mfdistricts.databinding.MfMapsMarkerBinding

class MFMapsMarker : ConstraintLayout {
    private lateinit var binding :  MfMapsMarkerBinding
    private var mfMapsMarkerIconUrl : String? = null
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
            R.styleable.MFMapsMarker,
            ZERO,
            ZERO,
        ).apply {
            mfMapsMarkerIconUrl = getString(R.styleable.MFMapsMarker_mf_marker_icon_url)
            recycle()
        }
    }
    private fun initView(){
        binding = MfMapsMarkerBinding.inflate(LayoutInflater.from(context), this)
        setMarkerWidthHeight(R.dimen.mf_maps_marker_width_s, R.dimen.mf_maps_marker_height_s)
    }
    private fun setUrl(imagen: String?){
        imagen?.let { im ->
            binding.mfMapsMarkerIcon.setImageUrl(im)
        }
    }
    fun externalSetUrl(imagen: String?){
        imagen?.let { im ->
           setUrl(imagen)
        }
    }
    fun getBitMap(): Bitmap {
        val bitmap = Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout(left, top, right, bottom)
        measure(measuredWidth, height)
        draw(canvas)
        return bitmap
    }
    fun setMarkerWidthHeight(newWidth: Int, newHeight: Int){
        val cp = ViewGroup.LayoutParams(resources.getDimensionPixelSize(newWidth), resources.getDimensionPixelSize(newHeight))
        binding.root.layoutParams = cp
        layout(left, top, right, bottom)
        measure(cp.width, cp.height)
    }

    fun getBitMapOfUrlImage(imageUrl: String, width: Int, height: Int, callback: (Bitmap?) -> Unit){
        Glide.with(context  )
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>(width, height) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    callback(null)
                }
            })
    }

    companion object {
        private const val ZERO = 0
        private val EMPTY_STRING = ""
        @BindingAdapter("mf_marker_icon_url")
        @JvmStatic
        fun setUrl(view: MFMapsMarker, imageUrl: String){
            view.setUrl(imageUrl)
        }
    }
}