package com.rekoj134.gradientdotindicator.indicator

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import com.rekoj134.gradientdotindicator.R

class GradientDotIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseDotsIndicator(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_WIDTH_FACTOR = 2.5f
    }

    private lateinit var linearLayout: LinearLayout
    private var dotsWidthFactor: Float = 0f
    private var progressMode: Boolean = false
    private var dotsElevation: Float = 0f

    var selectedDotColorStart: Int = 0
        set(value) {
            field = value
            refreshDotsColors()
        }

    var selectedDotColorEnd: Int = 0
        set(value) {
            field = value
            refreshDotsColors()
        }

    var isTypeCircleDot: Boolean = false
        set(value) {
            field = value
            refreshDotsColors()
        }

    private val listColor = ArrayList<Int>()
    fun setListColor(listColor: List<Int>) {
        this.listColor.clear()
        this.listColor.addAll(listColor)
        refreshDotsColors()
    }

    private val argbEvaluator = ArgbEvaluator()

    init {
        init(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.CENTER_VERTICAL
        addView(linearLayout, WRAP_CONTENT, WRAP_CONTENT)

        dotsWidthFactor = DEFAULT_WIDTH_FACTOR

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.DotsIndicator)

            selectedDotColorStart = a.getColor(R.styleable.DotsIndicator_selectedDotColorStart, DEFAULT_POINT_COLOR)
            selectedDotColorEnd = a.getColor(R.styleable.DotsIndicator_selectedDotColorEnd, DEFAULT_POINT_COLOR)

            isTypeCircleDot = a.getBoolean(R.styleable.DotsIndicator_isTypeCircleDot, false)

            dotsWidthFactor = a.getFloat(R.styleable.DotsIndicator_dotsWidthFactor, 2.5f)
            if (dotsWidthFactor < 1) {
                Log.w(
                    "DotsIndicator",
                    "The dotsWidthFactor can't be set under 1.0f, please set an higher value"
                )
                dotsWidthFactor = 1f
            }

            progressMode = a.getBoolean(R.styleable.DotsIndicator_progressMode, false)

            dotsElevation = a.getDimension(R.styleable.DotsIndicator_dotsElevation, 0f)

            a.recycle()
        }

        if (isInEditMode) {
            addDots(5)
            refreshDots()
        }

    }

    override fun addDot(index: Int) {
        val dot = LayoutInflater.from(context).inflate(R.layout.dot_layout, this, false)
        val imageView = dot.findViewById<ImageView>(R.id.dot)
        val params = imageView.layoutParams as LayoutParams

        dot.layoutDirection = View.LAYOUT_DIRECTION_LTR

        params.height = dotsSize.toInt()
        params.width = params.height
        params.setMargins(dotsSpacing.toInt(), 0, dotsSpacing.toInt(), 0)
        val background = DotsGradientDrawable()
        background.cornerRadius = dotsCornerRadius
        if (isTypeCircleDot) background.cornerRadius = 99f
        background.orientation = GradientDrawable.Orientation.LEFT_RIGHT

        if (pager!!.currentItem == index) {
            background.colors = intArrayOf(selectedDotColorStart, selectedDotColorEnd)
        } else {
            background.setColor(dotsColor)
        }

        imageView.background = background

        dot.setOnClickListener {
            if (dotsClickable && index < (pager?.count ?: 0)) {
                pager!!.setCurrentItem(index, true)
            }
        }

        dot.setPaddingHorizontal((dotsElevation * 0.8f).toInt())
        dot.setPaddingVertical((dotsElevation * 2).toInt())
        imageView.elevation = dotsElevation

        dots.add(imageView)
        linearLayout.addView(dot)
    }

    override fun removeDot() {
        linearLayout.removeViewAt(linearLayout.childCount - 1)
        dots.removeAt(dots.size - 1)
    }

    override fun buildOnPageChangedListener(): OnPageChangeListenerHelper {
        return object : OnPageChangeListenerHelper() {
            override fun onPageScrolled(
                selectedPosition: Int,
                nextPosition: Int,
                positionOffset: Float
            ) {
                val selectedDot = dots[selectedPosition]
                // Selected dot
                val selectedDotWidth =
                    (dotsSize + dotsSize * (dotsWidthFactor - 1) * (1 - positionOffset)).toInt()
                selectedDot.setWidth(selectedDotWidth)
                if (isTypeCircleDot) selectedDot.setHeight(selectedDotWidth)

                if (dots.isInBounds(nextPosition)) {
                    val nextDot = dots[nextPosition]

                    val nextDotWidth =
                        (dotsSize + dotsSize * (dotsWidthFactor - 1) * positionOffset).toInt()
                    nextDot.setWidth(nextDotWidth)
                    if (isTypeCircleDot) nextDot.setHeight(nextDotWidth)

                    val selectedDotBackground = selectedDot.background as DotsGradientDrawable
                    val nextDotBackground = nextDot.background as DotsGradientDrawable

                    selectedDotBackground.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                    nextDotBackground.orientation = GradientDrawable.Orientation.LEFT_RIGHT

                    if (listColor.isEmpty()) {
                        if (selectedDotColorStart != dotsColor) {
                            val selectedColor = argbEvaluator.evaluate(
                                positionOffset, selectedDotColorStart,
                                dotsColor
                            ) as Int

                            val nextColor = argbEvaluator.evaluate(
                                positionOffset, dotsColor,
                                selectedDotColorStart
                            ) as Int

                            val nextColorFinal = if (nextColor == dotsColor) intArrayOf(dotsColor, dotsColor) else intArrayOf(selectedDotColorStart, selectedDotColorEnd)
                            val selectedColorFinal = if (selectedColor == dotsColor) intArrayOf(dotsColor, dotsColor) else intArrayOf(selectedDotColorStart, selectedDotColorEnd)

                            nextDotBackground.colors = nextColorFinal

                            if (progressMode && selectedPosition == pager!!.currentItem) {
                                selectedDotBackground.colors = intArrayOf(selectedDotColorStart, selectedDotColorEnd)
                            } else {
                                selectedDotBackground.colors = selectedColorFinal
                            }
                        }
                    } else {
                        if (selectedDotColorStart != dotsColor) {
                            val selectedColor = argbEvaluator.evaluate(
                                positionOffset, listColor[selectedPosition],
                                dotsColor
                            ) as Int

                            val nextColor = argbEvaluator.evaluate(
                                positionOffset, dotsColor,
                                listColor[nextPosition]
                            ) as Int

                            nextDotBackground.color = ColorStateList.valueOf(nextColor)

                            if (progressMode && selectedPosition == pager!!.currentItem) {
                                selectedDotBackground.color = ColorStateList.valueOf(selectedColor)
                            } else {
                                selectedDotBackground.color = ColorStateList.valueOf(selectedColor)
                            }
                        }
                    }
                }

                invalidate()
            }

            override fun resetPosition(position: Int) {
                dots[position].setWidth(dotsSize.toInt())
                refreshDotColor(position)
            }

            override val pageCount: Int
                get() = dots.size
        }
    }

    override fun refreshDotColor(index: Int) {
        val elevationItem = dots[index]
        val background = elevationItem.background as? DotsGradientDrawable?
        background?.orientation = GradientDrawable.Orientation.LEFT_RIGHT

        background?.let {
            if (index == pager!!.currentItem) {
                if (listColor.isEmpty()) {
                    background.colors = intArrayOf(selectedDotColorStart, selectedDotColorEnd)
                } else {
                    background.color = ColorStateList.valueOf(listColor[index])
                }
            } else {
                background.setColor(dotsColor)
            }
        }

        elevationItem.setBackgroundCompat(background)
        elevationItem.invalidate()
    }

    override val type get() = Type.DEFAULT
}