package com.rekoj134.gradientdotindicator

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter

class SliderAdapter(
    private var context: Context
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    private var isUsingAnim = true

    fun setUsingAnim(isUsing: Boolean) {
        this.isUsingAnim = isUsing
    }

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater!!.inflate(R.layout.item_view_pager, container, false)
        when (position) {
            0 -> view.rootView.setBackgroundColor(Color.parseColor("#33FF5D5D"))
            1 -> view.rootView.setBackgroundColor(Color.parseColor("#3322B3D3"))
            else -> view.rootView.setBackgroundColor(Color.parseColor("#3368B36B"))
        }
        val itemName = view.findViewById<TextView>(R.id.tvItemName)
        itemName.text = (position + 1).toString()
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}