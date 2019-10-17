package com.job.ebursary.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.job.ebursary.R
import com.job.ebursary.commoners.AdapterListNews
import com.job.ebursary.commoners.DataGenerator
import kotlinx.android.synthetic.main.fragment_bursary.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BursaryFragment : Fragment() {

    private lateinit var mAdapter: AdapterListNews
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bursary, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initComponent()
    }

    private fun initComponent() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        val items = DataGenerator.getNewsData(activity, 10)

        //set data and list adapter
        mAdapter = AdapterListNews(activity, items, R.layout.item_news_light)
        recyclerView.setAdapter(mAdapter)

        // on item list clicked
        mAdapter.setOnItemClickListener { view, obj, _ ->
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "Item " + obj.title + " clicked",
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

}
