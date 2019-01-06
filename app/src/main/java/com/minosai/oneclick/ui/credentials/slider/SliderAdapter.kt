package com.minosai.oneclick.ui.credentials.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.minosai.oneclick.R
import kotlinx.android.synthetic.main.item_slider.view.*


class SliderAdapter(val context: Context, val titles: List<String>, val subTitles: List<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount() = titles.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_slider, null)

        view.text_slider_title.text = titles[position]
        view.text_slider_subtitle.text = subTitles[position]
        view.image_slider.setImageResource(when(position) {
            0 -> R.drawable.onboard_1
            1 -> R.drawable.onboard_2
            2 -> R.drawable.onboard_3
            else -> R.drawable.onboard_4
        })

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}