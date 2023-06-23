package com.manoffocus.mfdistricts.components.mfchip

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.MfChipBinding

class MFChip: ConstraintLayout {
    enum class MFChipStyles{
        SMALL,
        MEDIUM,
        LARGE,
        EXTRALARGE
    }
    private lateinit var binding : MfChipBinding
    private var mfChipTitletext : String? = null
    private var mfChipValuetext : String? = null
    private var mfChipdefaultContentDes : String? = null
    private var mfChipstartIcon : Drawable? = null
    private var mfChipValueColorText : Int? = null
    private var mfChipTextSize : Float? = null
    private var mfChipTBackgroundColor : Int? = null
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
            R.styleable.MFChip,
            ZERO,
            ZERO
        ).apply {
            mfChipTitletext = getString(R.styleable.MFChip_mf_chip_title_text)
            mfChipValuetext = getString(R.styleable.MFChip_mf_chip_value_text)
            mfChipValueColorText = getInteger(R.styleable.MFChip_mf_chip_color_value_text, Color.WHITE)
            mfChipTextSize = getDimension(R.styleable.MFChip_mf_chip_text_size, -1F)
            mfChipTBackgroundColor = getInteger(R.styleable.MFChip_mf_chip_background_color, Color.BLACK)
            contentDescription?.let { content ->
                mfChipdefaultContentDes = content.toString()
            } ?: kotlin.run {
                mfChipdefaultContentDes = EMPTY_STRING
            }

            if (mfChipTextSize == -1F) {
                attrs?.let { at ->
                    at.styleAttribute.let { style ->
                        mfChipTextSize = if (style == 0) {
                            resolveLabelName(MFChipStyles.MEDIUM)
                        } else {
                            resolveLabelSize(resources.getResourceEntryName(style))
                        }
                    }
                }
            }
            try {
                mfChipstartIcon = getDrawable(R.styleable.MFChip_mf_chip_icon_startIcon) as Drawable
            } catch (e: Exception) {
                e.localizedMessage
            }
        }
    }
    private fun initView() {
        binding = MfChipBinding.inflate(LayoutInflater.from(context), this)
        setChipTitleText(mfChipTitletext)
        setChipValueText(mfChipValuetext)
        setStartIcon(mfChipstartIcon)
        setValueTextColor(mfChipValueColorText)
        setValueTextSize(mfChipTextSize)
        setValueBackground(mfChipTBackgroundColor)
    }
    private fun setStartIcon(icon: Drawable?) {
        icon?.let {
            mfChipstartIcon = it
            binding.mfChipImageView.apply {
                visibility = VISIBLE
                externalSetDrawable(icon)
            }
        }
    }
    fun externalSetStartIcon(icon: Drawable?) {
        icon?.let {
            setStartIcon(it)
        }
    }
    private fun setChipTitleText(text: String?) {
        text?.let {
            mfChipTitletext = it
            binding.mfChipLabelTitle.apply {
                externalSetText(it)
                setBold()
            }
        }
    }
    fun externalSetChipTitleText(text: String?) {
        text?.let {
            setChipTitleText(it)
        }
    }
    private fun setChipValueText(text: String?) {
        text?.let {
            mfChipValuetext = it
            binding.mfChipLabelValue.apply {
                externalSetText(it)
            }
        }
    }
    fun externalSetChipValueText(text: String?) {
        text?.let {
            setChipValueText(it)
        }
    }
    private fun setValueTextColor(color: Int?) {
        color?.let { c ->
            binding.mfChipLabelValue.apply {
                externalSetColor(color)
            }
            binding.mfChipLabelTitle.apply {
                externalSetColor(color)
            }
        }
    }
    fun externalSetValueTextColor(color: Int?) {
        color?.let { c ->
            setValueTextColor(c)
        }
    }
    private fun setValueTextSize(size: Float?) {
        size?.let { s ->
            binding.mfChipLabelValue.apply {
                externalSetFontSize(s)
            }
            binding.mfChipLabelTitle.apply {
                externalSetFontSize(s + 10F)
            }
        }
    }
    fun externalSetValueTextSize(size: Float?) {
        size?.let { s ->
            setValueTextSize(s)
        }
    }
    private fun setValueBackground(colorbackground: Int?) {
        colorbackground?.let { color ->
            binding.mfChipBox.setBackgroundColor(color)
        }
    }
    fun externalSetValueBackground(colorbackground: Int?) {
        colorbackground?.let { color ->
            setValueBackground(color)
        }
    }

    private fun resolveLabelSize(style: String): Float{
        return when(style){
            "MFChip.small" -> resolveLabelName(MFChipStyles.SMALL)
            "MFChip.medium" -> resolveLabelName(MFChipStyles.MEDIUM)
            "MFChip.large" -> resolveLabelName(MFChipStyles.LARGE)
            "MFChip.extralarge" -> resolveLabelName(MFChipStyles.EXTRALARGE)
            else -> resolveLabelName(MFChipStyles.MEDIUM)
        }
    }
    private fun resolveLabelName(style: MFChipStyles): Float{
        return when(style){
            MFChipStyles.SMALL -> resources.getDimension(R.dimen.mf_chip_text_s)
            MFChipStyles.MEDIUM -> resources.getDimension(R.dimen.mf_chip_text_m)
            MFChipStyles.LARGE -> resources.getDimension(R.dimen.mf_chip_text_l)
            MFChipStyles.EXTRALARGE -> resources.getDimension(R.dimen.mf_chip_text_xl)
        }
    }



    companion object {
        private const val ZERO = 0
        private val EMPTY_STRING = ""
        private val MEDIUM_TEXT_SIZE = 30F

        @BindingAdapter("mf_chip_title_text")
        @JvmStatic
        fun setChipTitleText(imageView: MFChip, title: String?) {
            imageView.setChipTitleText(title)
        }
        @BindingAdapter("mf_chip_value_text")
        @JvmStatic
        fun setChipValueText(imageView: MFChip, valueText: String?) {
            imageView.setChipValueText(valueText)
        }
        @BindingAdapter("mf_chip_icon_startIcon")
        @JvmStatic
        fun setStartIcon(imageView: MFChip, icon: Drawable?) {
            imageView.setStartIcon(icon)
        }
        @BindingAdapter("mf_chip_color_value_text")
        @JvmStatic
        fun setValueColor(imageView: MFChip, color: Int?) {
            imageView.setValueTextColor(color)
        }
        @BindingAdapter("mf_chip_text_size")
        @JvmStatic
        fun setValueTextSize(imageView: MFChip, size: Float?) {
            imageView.setValueTextSize(size)
        }
        @BindingAdapter("mf_chip_background_color")
        @JvmStatic
        fun setValueBackground(imageView: MFChip, color: Int?) {
            imageView.setValueBackground(color)
        }
    }
}