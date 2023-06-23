package com.manoffocus.mfdistricts.components.mfbuttontexticon

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.manoffocus.mfdistricts.R
import com.manoffocus.mfdistricts.databinding.MfButtonBinding

class MFButtonTextIcon : ConstraintLayout, OnTouchListener {
    private lateinit var binding : MfButtonBinding
    private var mfButtonText : String = ""
    private var mfButtonBg : Drawable? = null
    private var mfButtonTintColor : Int? = null
    private var mfButtonIcon : Drawable? = null
    private var mfButtonTextSize : Float? = null
    private var mfButtonColorText : Int = Color.BLACK
    private var mfButtonAccessible = false
    private var mfButtonContentDescription = ""
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs = attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(attrs = attrs)
    }
    private fun initialize(
        ctx: Context = context,
        attrs: AttributeSet?) {
        loadAttrs(ctx = ctx, attrs = attrs)
        initView()
    }
    private fun loadAttrs(
        ctx: Context = context,
        attrs: AttributeSet?,
        defStyleAttr: IntArray = R.styleable.MFButtonTextIcon) {
        ctx.theme.obtainStyledAttributes(
            attrs,
            defStyleAttr,
            ZERO,
            ZERO
        ).apply {
            contentDescription?.let { content ->
                mfButtonContentDescription = content.toString()
            } ?: kotlin.run {
                mfButtonContentDescription = EMPTY_STRING
            }
            try {
                mfButtonText = getString(R.styleable.MFButtonTextIcon_mf_button_text).orEmpty()
                mfButtonBg = getDrawable(R.styleable.MFButtonTextIcon_mf_button_bg)
                mfButtonTintColor = getInteger(R.styleable.MFButtonTextIcon_mf_button_tint_color, -1)
                mfButtonIcon = getDrawable(R.styleable.MFButtonTextIcon_mf_button_icon)
                mfButtonTextSize = getDimension(R.styleable.MFButtonTextIcon_mf_button_text_size, DEFAULT_MEDIUM_TEXT_SIZE)
                mfButtonColorText = getInteger(R.styleable.MFButtonTextIcon_mf_button_color_text, Color.BLACK)
            } catch (e: Exception) {
                e.localizedMessage
            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        binding = MfButtonBinding.inflate(LayoutInflater.from(context), this)
        setTextButton(mfButtonText)
        setCustomBackground(mfButtonBg)
        setTintColor(mfButtonTintColor)
        setButtonIcon(mfButtonIcon)
        setTextSize(mfButtonTextSize)
        setColorText(mfButtonColorText)
        isClickable = true
        binding.mfButtonLabel.setOnTouchListener(this)
        binding.mfButtonIcon.setOnTouchListener(this)
        binding.mfButtonBox.setOnTouchListener(this)
    }
    private fun setTextButton(text: String?){
        text?.let { t ->
            binding.mfButtonLabel.externalSetText(t)
        }
    }
    fun externalSetText(text: String?){
        text?.let { t ->
            setTextButton(t)
        }
    }
    private fun setCustomBackground(bg: Drawable?){
        bg?.let { b ->
            binding.mfButtonBox.background = b
        }
    }
    fun externalSetCustomBackground(bg: Drawable?){
        bg?.let { b ->
            setCustomBackground(b)
        }
    }
    private fun setButtonIcon(icon: Drawable?){
        icon?.let { i ->
            binding.mfButtonIcon.externalSetDrawable(icon)
        }
    }
    fun externalSetButtonIcon(icon: Drawable?){
        icon?.let { i ->
            setButtonIcon(i)
        }
    }
    private fun setTextSize(size: Float?){
        size?.let { s ->
            if (s != -1F){
                binding.mfButtonLabel.externalSetFontSize(size)
            }
        }
    }
    fun externalSetTextSize(size: Float?){
        size?.let { s ->
            setTextSize(s)
        }
    }
    private fun setColorText(color: Int?){
        color?.let { c ->
            binding.mfButtonLabel.externalSetColor(color)
        }
    }
    fun externalSetColorText(color: Int?){
        color?.let { c ->
            setColorText(c)
        }
    }
    private fun setTintColor(color: Int?){
        color?.let { c ->
            if (c != -1){
                binding.mfButtonBox.backgroundTintList = ColorStateList.valueOf(color)
            }
        }
    }
    fun externalSetTintColor(color: Int?){
        color?.let { c ->
            if (c != -1){
                binding.mfButtonBox.backgroundTintList = ColorStateList.valueOf(color)
            }
        }
    }
    companion object {
        private const val ZERO = 0
        private val TAG = "MFButtonTextIcon"
        private val EMPTY_STRING = ""
        private val DEFAULT_MEDIUM_TEXT_SIZE = 41F
        @BindingAdapter("mf_button_text")
        @JvmStatic
        fun setTextButton(button: MFButtonTextIcon, text: String) {
            button.setTextButton(text)
        }
        @BindingAdapter("mf_button_bg")
        @JvmStatic
        fun setCustomBackground(button: MFButtonTextIcon, bg: Drawable) {
            button.setCustomBackground(bg)
        }
        @BindingAdapter("mf_button_icon")
        @JvmStatic
        fun setIcon(button: MFButtonTextIcon, icon: Drawable) {
            button.setButtonIcon(icon)
        }
        @BindingAdapter("mf_button_text_size")
        @JvmStatic
        fun setTextSize(button: MFButtonTextIcon, size: Float?) {
            button.setTextSize(size)
        }
        @BindingAdapter("mf_button_color_text")
        @JvmStatic
        fun setTextColor(button: MFButtonTextIcon, color: Int?) {
            button.setColorText(color)
        }
        @BindingAdapter("mf_button_tint_color")
        @JvmStatic
        fun setTintColor(button: MFButtonTextIcon, color: Int) {
            button.setTintColor(color)
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

    override fun onTouch(v: View?, m: MotionEvent?): Boolean {
        var res = true
        val x = m?.rawX?.toInt() ?: run { 0 }
        val y = m?.rawY?.toInt() ?: run { 0 }
        when(m?.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isViewInBounds(x, y)) {
                    isPressed = false
                }
            }
            MotionEvent.ACTION_UP -> {
                isPressed = false
                if (isViewInBounds(x, y)) {
                    Log.d(TAG, "clicked: $v")
                    performClick()
                }
                res = false
            }
        }
        return res
    }
}