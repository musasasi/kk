package com.job.ebursary.fragments


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.job.ebursary.R
import com.job.ebursary.commoners.AdapterImageSlider
import com.job.ebursary.model.Image
import kotlinx.android.synthetic.main.include_drawer_content.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    private lateinit var runnable: Runnable
    private lateinit var viewPager: ViewPager
    private lateinit var adapterImageSlider: AdapterImageSlider

    private var handler = Handler()



    private val array_image_place = intArrayOf(
        R.drawable.b1,
        R.drawable.b2,
        R.drawable.b3,
        R.drawable.b4,
        R.drawable.b5
    )

    private val array_title_place = arrayOf(
        "Nandi Governor Stephen Sang giving out County bursary",
        "It Is Early Christmas For Students As County Issues BursariesKwale County",
        "Global Hope Foundation Programme gives bursary ",
        "Embu: County disburses Sh163million bursary",
        "Nairobi county executive is yet again on the spot over bursaries"
    )

    private fun getDetails(i: Int): String{
        return when(i){
            0 -> getString(R.string.b1)
            1 -> getString(R.string.b2)
            2 -> getString(R.string.b3)
            3 -> getString(R.string.b4)
            4 -> getString(R.string.b5)
            else -> " "
        }
    }

    private val array_brief_place =
        arrayOf("Nandi Hill", "Kwale County", "River Forest", "Embu County", "Nairobi County")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponent()

    }

    override fun onDestroyView() {
        if (runnable != null) handler.removeCallbacks(runnable)
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    private fun initComponent() {
        layout_dots as LinearLayout
        viewPager = pager as ViewPager
        adapterImageSlider = AdapterImageSlider(activity, ArrayList<Image>())

        val items = mutableListOf<Image>()
        for (i in 0 until array_image_place.size) {
            val obj = Image()
            obj.image = array_image_place[i]
            obj.imageDrw = resources.getDrawable(obj.image)
            obj.name = array_title_place[i]
            obj.brief = array_brief_place[i]
            obj.des = getDetails(i)
            items.add(obj)
        }

        adapterImageSlider.setItems(items)
        viewPager.adapter = adapterImageSlider

        // displaying selected image first
        viewPager.currentItem = 0
        addBottomDots(layout_dots, adapterImageSlider.count, 0)
        (activity?.findViewById(R.id.title) as TextView).setText(items[0].name)
        (activity?.findViewById(R.id.brief) as TextView).setText(items[0].brief)
        (activity?.findViewById(R.id.description) as TextView).setText(items[0].des)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(pos: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(pos: Int) {
                (activity?.findViewById(R.id.title) as TextView).setText(items.get(pos).name)
                (activity?.findViewById(R.id.brief) as TextView).setText(items.get(pos).brief)
                (activity?.findViewById(R.id.description) as TextView).setText(items.get(pos).des)
                addBottomDots(layout_dots, adapterImageSlider.count, pos)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        startAutoSlider(adapterImageSlider.count)
    }

    private fun addBottomDots(layout_dots: LinearLayout, size: Int, current: Int) {
        val dots = arrayOfNulls<ImageView>(size)

        layout_dots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(activity)
            val width_height = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]?.layoutParams = params
            dots[i]?.setImageResource(R.drawable.shape_circle_outline)
            layout_dots.addView(dots[i])
        }

        if (dots.size > 0) {
            dots[current]?.setImageResource(R.drawable.shape_circle)
        }
    }

    private fun startAutoSlider(count: Int) {
        runnable = Runnable {
            var pos = viewPager.currentItem
            pos += 1
            if (pos >= count) pos = 0
            viewPager.currentItem = pos
            handler.postDelayed(runnable, 3000)
        }
        handler.postDelayed(runnable, 3000)
    }


}
