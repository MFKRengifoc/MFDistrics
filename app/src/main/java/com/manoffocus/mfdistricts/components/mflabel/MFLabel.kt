package com.manoffocus.mfdistricts.components.mflabel

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.MfLabelBinding

class MFLabel : ConstraintLayout {
    private lateinit var binding : MfLabelBinding
    private var mfLabelext : String? = null
    private var mfLabelContentDes : String? = null
    private var mfLabelColor : Int = Color.WHITE
    private var mfLabelSize : Float? = null
    private var mfLabelUseFont = false
    private var mfLabelbold = false
    private var mfLabelAlignText : Int? = null


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
            R.styleable.MFLabel,
            ZERO,
            ZERO,
        ).apply {
            mfLabelext = getString(R.styleable.MFLabel_mf_label_formattedText).orEmpty()
            mfLabelColor = getColor(R.styleable.MFLabel_mf_label_color, Color.WHITE)
            mfLabelSize = getDimension(R.styleable.MFLabel_mf_label_size, MEDIUM_TEXT_SIZE)
            mfLabelUseFont = getBoolean(R.styleable.MFLabel_mf_label_use_custom_font, false)
            mfLabelbold = getBoolean(R.styleable.MFLabel_mf_label_bold, false)
            mfLabelAlignText = getInteger(R.styleable.MFLabel_mf_label_alignment_text, TEXT_ALIGNMENT_TEXT_START)
            contentDescription?.let { content ->
                mfLabelContentDes = content.toString()
            } ?: kotlin.run {
                mfLabelContentDes = EMPTY_STRING
            }
            recycle()
        }
    }
    private fun initView() {
        binding = MfLabelBinding.inflate(LayoutInflater.from(context), this)
        setLabelText(mfLabelext)
        setColor(mfLabelColor)
        setFontSize(mfLabelSize)
        if (mfLabelbold) setBold()
        setAlign(mfLabelAlignText)
        isClickable = true
        if (mfLabelUseFont) {
            setFont()
        }
    }
    private fun setLabelText(mfText: String?) {
        mfText?.let { label ->
            visibility = VISIBLE
            binding.mfLabelText.text = label
            binding.mfLabelText.visibility = VISIBLE
        }
    }
    fun externalSetText(text: String){
        setLabelText(text)
    }
    private fun setColor(mfColor: Int?) {
        binding.mfLabelText.apply {
            mfColor?.let { labelColor ->
                if (mfColor != -1){
                    setTextColor(mfColor)
                } else {
                    setTextColor(Color.WHITE)
                }
            } ?: kotlin.run {
                setTextColor(Color.BLACK)
            }
        }
    }
    fun externalSetColor(mfColor: Int?) {
        mfColor?.let { color ->
            setColor(color)
        }
    }
    private fun setFont(){
        binding.mfLabelText.apply {

        }
    }
    private fun setFontSize(size: Float?){
        size?.let { s ->
            binding.mfLabelText.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
            }
        }
    }
    fun externalSetFontSize(size: Float?){
        size?.let { s ->
            setFontSize(s)
        }
    }
    private fun setTextStyle(style: Typeface = Typeface.DEFAULT_BOLD){
        binding.mfLabelText.apply {
            typeface = style
        }
    }
    fun externalSetTextStyle(style: Typeface = Typeface.DEFAULT_BOLD){
        setTextStyle(style)
    }
    fun setBold(){
        setTextStyle()
    }
    fun setAlign(align: Int?){
        align?.let { a ->
            binding.mfLabelText.textAlignment = a
        }
    }
    override fun setVisibility(visible: Int){
        binding.mfLabelText.visibility = visible
    }
    fun spToPx(sp: Float): Float {
        return sp / resources.displayMetrics.density
    }

    companion object {
        private const val ZERO = 0
        private val EMPTY_STRING = ""
        private val MEDIUM_TEXT_SIZE = 30F
        @BindingAdapter("mf_label_formattedText")
        @JvmStatic
        fun externalSetText(label: MFLabel, title: String) {
            label.externalSetText(title)
        }
        @BindingAdapter("mf_label_color")
        @JvmStatic
        fun setColor(label: MFLabel, color: Int) {
            label.setColor(color)
        }
        @BindingAdapter("mf_label_size")
        @JvmStatic
        fun setFontSize(label: MFLabel, sizeText: Float) {
            label.setFontSize(sizeText)
        }
        @BindingAdapter("mf_use_custom_font")
        @JvmStatic
        fun useCustomFont(label: MFLabel, useCustomFont: Boolean) {
            if (useCustomFont) {
                label.setFont()
            }
        }
        @BindingAdapter("mf_label_bold")
        @JvmStatic
        fun setBold(label: MFLabel, bold: Boolean) {
            if (bold) {
                label.setBold()
            }
        }
        @BindingAdapter("mf_label_alignment_text")
        @JvmStatic
        fun setAlignText(label: MFLabel, align: Int) {
            label.setAlign(align)
        }
    }

    private fun isViewInBounds(x: Int, y: Int): Boolean {
        val outRect = Rect()
        val location = IntArray(2)
        binding.root.getDrawingRect(outRect)
        binding.root.getLocationOnScreen(location)
        outRect.offset(location.get(0), location.get(1))
        return outRect.contains(x, y)
    }
}