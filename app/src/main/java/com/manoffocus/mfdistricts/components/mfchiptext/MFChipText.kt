package com.manoffocus.mfdistricts.components.mfchiptext

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.components.mfchip.MFChip
import com.manoffocus.mfdistricts.databinding.MfChipTextBinding

class MFChipText : ConstraintLayout {
    enum class MFChipTextSize{
        SMALL,
        MEDIUM,
        LARGE,
        EXTRALARGE
    }
    private lateinit var binding: MfChipTextBinding
    private var mfChipTextValue : String? = null
    private var mfChipTextColor : Int? = null
    private var mfChipTextIcon : Drawable? = null
    private var mfChipTextSize : Float? = null
    private var mfChipTextBgColor : Int? = null
    private var mfChipTextDefaultContentDes : String? = null
    private var mfChipTextReverse : Boolean? = null

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
    private fun loadAttrs(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MFChipText,
            ZERO,
            ZERO
        ).apply {
            mfChipTextValue = getString(R.styleable.MFChipText_mf_chip_text_value)
            mfChipTextColor = getInteger(R.styleable.MFChipText_mf_chip_text_color, Color.WHITE)
            mfChipTextSize = getDimension(R.styleable.MFChipText_mf_chip_text_text_size, -1F)
            mfChipTextBgColor = getInteger(R.styleable.MFChipText_mf_chip_text_background_color, Color.TRANSPARENT)
            mfChipTextReverse = getBoolean(R.styleable.MFChipText_mf_chip_text_reverse,false)
            contentDescription?.let { content ->
                mfChipTextDefaultContentDes = content.toString()
            } ?: kotlin.run {
                mfChipTextDefaultContentDes = EMPTY_STRING
            }

            if (mfChipTextSize == -1F) {
                attrs?.let { at ->
                    at.styleAttribute.let { style ->
                        mfChipTextSize = if (style == 0) {
                            resolveLabelName(MFChipTextSize.MEDIUM)
                        } else {
                            resolveLabelSize(resources.getResourceEntryName(style))
                        }
                    }
                }
            }
            try {
                mfChipTextIcon = getDrawable(R.styleable.MFChipText_mf_chip_text_icon) as Drawable
            } catch (e: Exception) {
                e.localizedMessage
            }
        }
    }
    private fun initView() {
        binding = MfChipTextBinding.inflate(LayoutInflater.from(context), this)
        binding.mfChipTextBox.layoutDirection = LinearLayout.LAYOUT_DIRECTION_RTL
        setChipTextValue(mfChipTextValue)
        setChipIcon(mfChipTextIcon)
        setChipTextColor(mfChipTextColor)
        setChipTextSize(mfChipTextSize)
        setChipTextBackground(mfChipTextBgColor)
        setReverse(mfChipTextReverse)
    }
    private fun setChipTextValue(text: String?) {
        text?.let {
            mfChipTextValue = it
            binding.mfChipTextValue.apply {
                externalSetText(it)
            }
        }
    }
    fun externalSetChipValueText(text: String?) {
        text?.let {
            setChipTextValue(it)
        }
    }
    private fun setChipTextColor(color: Int?) {
        color?.let { c ->
            binding.mfChipTextValue.apply {
                externalSetColor(color)
            }
        }
    }
    fun externalSetValueTextColor(color: Int?) {
        color?.let { c ->
            setChipTextColor(c)
        }
    }
    private fun setChipIcon(icon: Drawable?) {
        icon?.let {
            mfChipTextIcon = it
            binding.mfChipTextIcon.apply {
                visibility = VISIBLE
                externalSetDrawable(icon)
            }
        }
    }
    fun externalSetChipIcon(icon: Drawable?) {
        icon?.let {
            setChipIcon(it)
        }
    }
    private fun setChipIconByInt(icon: Int?) {
        icon?.let { ic ->
            val drawable = ResourcesCompat.getDrawable(resources, ic, null)
            setChipIcon(drawable)
        }
    }
    fun externalSetChipIconByInt(icon: Int?) {
        icon?.let {
            setChipIconByInt(it)
        }
    }
    private fun setChipTextSize(size: Float?) {
        size?.let { s ->
            binding.mfChipTextValue.apply {
                externalSetFontSize(s)
            }
        }
    }
    fun externalSetValueTextSize(size: Float?) {
        size?.let { s ->
            setChipTextSize(s)
        }
    }
    private fun setChipTextBackground(colorbackground: Int?) {
        colorbackground?.let { color ->
            binding.mfChipTextBox.setBackgroundColor(color)
        }
    }
    fun externalSetValueBackground(colorbackground: Int?) {
        colorbackground?.let { color ->
            setChipTextBackground(color)
        }
    }
    private fun setReverse(reverse: Boolean?) {
        reverse?.let { rev ->
            if (rev){
                binding.mfChipTextBox.layoutDirection = LAYOUT_DIRECTION_RTL
            } else {
                binding.mfChipTextBox.layoutDirection = LAYOUT_DIRECTION_LTR
            }
        }
    }
    fun externalSetReverse(reverse: Boolean?) {
        reverse?.let { rev ->
            setReverse(rev)
        }
    }

    private fun resolveLabelSize(style: String): Float{
        return when(style){
            "MFChipText.small" -> resolveLabelName(MFChipTextSize.SMALL)
            "MFChipText.medium" -> resolveLabelName(MFChipTextSize.MEDIUM)
            "MFChipText.large" -> resolveLabelName(MFChipTextSize.LARGE)
            "MFChipText.extralarge" -> resolveLabelName(MFChipTextSize.EXTRALARGE)
            else -> resolveLabelName(MFChipTextSize.MEDIUM)
        }
    }
    private fun resolveLabelName(style: MFChipTextSize): Float{
        return when(style){
            MFChipTextSize.SMALL -> resources.getDimension(R.dimen.mf_chip_text_text_s)
            MFChipTextSize.MEDIUM -> resources.getDimension(R.dimen.mf_chip_text_text_m)
            MFChipTextSize.LARGE -> resources.getDimension(R.dimen.mf_chip_text_text_l)
            MFChipTextSize.EXTRALARGE -> resources.getDimension(R.dimen.mf_chip_text_text_xl)
        }
    }
    companion object {
        const val ZERO = 0
        const val EMPTY_STRING = ""

        @BindingAdapter("mf_chip_title_text")
        @JvmStatic
        fun setChipTextValue(imageView: MFChipText, value: String?) {
            imageView.setChipTextValue(value)
        }
        @BindingAdapter("mf_chip_icon_startIcon")
        @JvmStatic
        fun setChipIcon(imageView: MFChipText, icon: Drawable?) {
            imageView.setChipIcon(icon)
        }
        @BindingAdapter("mf_chip_color_value_text")
        @JvmStatic
        fun setChipTextColor(imageView: MFChipText, color: Int?) {
            imageView.setChipTextColor(color)
        }
        @BindingAdapter("mf_chip_text_text_size")
        @JvmStatic
        fun setChipTextSize(imageView: MFChipText, size: Float?) {
            imageView.setChipTextSize(size)
        }
        @BindingAdapter("mf_chip_text_background_color")
        @JvmStatic
        fun setChipBackground(imageView: MFChipText, color: Int?) {
            imageView.setChipTextBackground(color)
        }
        @BindingAdapter("mf_chip_text_reverse")
        @JvmStatic
        fun reverse(imageView: MFChipText, reverse: Boolean?) {
            imageView.setReverse(reverse)
        }
    }
}