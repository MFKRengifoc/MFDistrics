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
import com.manoffocus.mfdistricts.databinding.MfMapsEventBoxMarkerBinding

class MFMapsEventBoxMarker : ConstraintLayout {
    private lateinit var binding :  MfMapsEventBoxMarkerBinding
    private var mfMapsMarkerTitle : String? = null
    private var mfMapsMarkerSubtitle : String? = null
    private var mfMapsMarkerTime : String? = null
    private var mfMapsMarkerPlace : String? = null
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
            R.styleable.MFMapsEventBoxMarker,
            ZERO,
            ZERO,
        ).apply {
            mfMapsMarkerTitle = getString(R.styleable.MFMapsEventBoxMarker_mf_marker_event_box_title)
            mfMapsMarkerTime = getString(R.styleable.MFMapsEventBoxMarker_mf_marker_event_box_time)
            mfMapsMarkerPlace = getString(R.styleable.MFMapsEventBoxMarker_mf_marker_event_box_place)
            recycle()
        }
    }
    private fun initView(){
        binding = MfMapsEventBoxMarkerBinding.inflate(LayoutInflater.from(context), this)
        setTitle(mfMapsMarkerTitle)
        setTime(mfMapsMarkerSubtitle)
        setMarkerWidthHeight(R.dimen.mf_maps_box_event_marker_width_m, R.dimen.mf_maps_box_event_marker_height_m)
    }
    private fun setTitle(title: String?){
        title?.let { t ->
            binding.mfMapsMarkerTitle.externalSetText(t)
        }
    }
    fun externalSetTitle(title: String?){
        title?.let { t ->
            setTitle(t)
        }
    }
    private fun setTime(time: String?){
        time?.let { t ->
            binding.mfMapsMarkerTime.externalSetText(t)
        }
    }
    fun externalSetTime(time: String?){
        time?.let { t ->
            setTime(t)
        }
    }
    private fun setPlace(place: String?){
        place?.let { p ->
            binding.mfMapsMarkerPlace.externalSetText(p)
        }
    }
    fun externalSetPlace(place: String?){
        place?.let { p ->
            setPlace(p)
        }
    }
    fun getBitMap(): Bitmap{
        val bitmap = Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout(left, top, right, bottom)
        measure(measuredWidth, height)
        draw(canvas)
        return bitmap
    }
    private fun setMarkerWidthHeight(newWidth: Int, newHeight: Int){
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
        @BindingAdapter("mf_marker_title")
        @JvmStatic
        fun setTitle(view: MFMapsEventBoxMarker, title: String){
            view.setTitle(title)
        }
        @BindingAdapter("mf_marker_time")
        @JvmStatic
        fun setTime(view: MFMapsEventBoxMarker, time: String){
            view.setTime(time)
        }
        @BindingAdapter("mf_marker_time")
        @JvmStatic
        fun setPlace(view: MFMapsEventBoxMarker, place: String){
            view.setPlace(place)
        }
    }
}