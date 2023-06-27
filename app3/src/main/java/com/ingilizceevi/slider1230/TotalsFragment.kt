package com.ingilizceevi.slider1230

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TotalsFragment : Fragment() {
    private var myTotalTime: String? = null
    private var myTotalClicks: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myTotalTime = it.getString(ARG_PARAM1)
            myTotalClicks = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val main = inflater.inflate(R.layout.fragment_totals, container, false)
        main.findViewById<TextView>(R.id.myTotalTime).text = myTotalTime
        main.findViewById<TextView>(R.id.myTotalClicks).text = myTotalClicks
        return main
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TotalsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}