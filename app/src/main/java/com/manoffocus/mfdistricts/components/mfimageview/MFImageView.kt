package com.manoffocus.mfdistricts.components.mfimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.MfImageViewBinding


class MFImageView : ConstraintLayout {
    private lateinit var binding : MfImageViewBinding
    private var mfImageDraw : Drawable? = null
    private var mfImageUrl: String = ""
    private var mfDefaultContentDes : String? = null
    private var mfImageSize : Float? = null
    private var mfImageTint : Int? = null
    private var placeholder: Drawable? = null
    private var errorImage: Drawable? = null

    constructor(context: Context) : super(context) {
        initialize(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(attrs)
    }
    private fun initialize(attrs: AttributeSet?) {
        loadAttrs(attrs)
        initView()
    }
    private fun loadAttrs(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MFImageView,
            ZERO,
            ZERO
        ).apply {
            contentDescription?.let { content ->
                mfDefaultContentDes = content.toString()
            } ?: kotlin.run {
                mfDefaultContentDes = EMPTY_STRING
            }
            mfImageSize = getDimension(R.styleable.MFImageView_mf_image_size, -1F)
            mfImageTint = getColor(R.styleable.MFImageView_mf_image_tint_color, Color.TRANSPARENT)
            try {
                mfImageDraw = getDrawable(R.styleable.MFImageView_mf_image_draw) as Drawable
            } catch (e: Exception) {
                e.localizedMessage
            }
        }
    }
    private fun initView() {
        binding = MfImageViewBinding.inflate(LayoutInflater.from(context), this)
        setDrawable(mfImageDraw)
        setImageUrl("")
        isClickable = true
        setImageSize(mfImageSize)
        setTintColor(mfImageTint)
    }
    private fun setDrawable(drawableImage: Drawable?) {
        drawableImage?.let { image ->
            binding.mfImageView.setImageDrawable(image)
        }
    }
    fun externalSetDrawable(drawableImage: Drawable){
        setDrawable(drawableImage)
    }

    private fun setImageInt(icon: Int?){
        icon?.let { ic ->
            val drawable = ResourcesCompat.getDrawable(resources, ic, null)
            setDrawable(drawable)
        }
    }

    fun externalSetImageInt(icon: Int?){
        icon?.let { ic ->
            setImageInt(ic)
        }
    }

    fun setImageUrl(imageUrl: String) {
        if (this.mfImageUrl != imageUrl) {
            this.mfImageUrl = imageUrl
            val builder: RequestBuilder<Drawable> = Glide.with(context).load(imageUrl)
                .error(errorImage ?: getDefaultErrorDrawable())
                .placeholder(placeholder ?: getDefaultPlaceholderDrawable())
                .apply(RequestOptions().override(width, height))
            builder.into(binding.mfImageView)
        }
    }
    fun setImgUrl(imgUrl: String){
        setImageUrl(imgUrl)
    }
    fun setImageSize(size: Float?){
        if (size != -1F){
            val lp = binding.mfImageView.layoutParams
            lp.width = spToPx(size!!)
            lp.height = spToPx(size!!)
            binding.mfImageView.layoutParams = lp
        }
    }

    private fun setTintColor(color: Int?){
        color?.let { c ->
            binding.mfImageView.setColorFilter(c)
        }
    }

    fun externalSetTintColor(color: Int?){
        color?.let { c ->
            setTintColor(c)
        }
    }

    private fun getDefaultErrorDrawable() =
        ResourcesCompat.getDrawable(resources, R.drawable.mf_imageview_bg_error_loading_min, context.theme)
    private fun getDefaultPlaceholderDrawable() =
        ResourcesCompat.getDrawable(resources, R.drawable.mf_imageview_loading_min, context.theme)

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

    fun spToPx(sp: Float): Int {
        return (sp / resources.displayMetrics.density).toInt()
    }

    companion object {
        private const val ZERO = 0
        private val EMPTY_STRING = ""

        @BindingAdapter("mf_image_url")
        @JvmStatic
        fun setImageUrl(imageView: MFImageView, url: String) {
            imageView.setImgUrl(url)
        }
        @BindingAdapter("mf_image_size")
        @JvmStatic
        fun setImageSize(imageView: MFImageView, size: Float?) {
            imageView.setImageSize(size)
        }
        @BindingAdapter("mf_image_draw")
        @JvmStatic
        fun setDrawable(imageView: MFImageView, drawableImage: Drawable?) {
            imageView.setDrawable(drawableImage)
        }
        @BindingAdapter("mf_image_tint_color")
        @JvmStatic
        fun setTintColor(imageView: MFImageView, color: Int?) {
            imageView.setTintColor(color)
        }

    }
}