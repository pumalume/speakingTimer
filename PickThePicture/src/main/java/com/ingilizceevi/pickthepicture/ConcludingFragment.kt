package com.ingilizceevi.pickthepicture


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConcludingFragment : Fragment() {
    private var myTotalTime: String? = null
    private var myTotalClicks: String? = null
    private val gameBrain: LaneViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myTotalTime = it.getString(ARG_PARAM1)
            myTotalClicks = it.getString(ARG_PARAM2)
        }
        // myButton.setOnClickListener(v -> {
        val entryControl = EntryControl(requireActivity(), gameBrain.studentInfo)
        entryControl.execute("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val main = inflater.inflate(R.layout.fragment_concluding, container, false)
        main.findViewById<TextView>(R.id.myTotalTime).text = getString(R.string.totalTime, myTotalTime)
        main.findViewById<TextView>(R.id.myTotalClicks).text = getString(R.string.totalClicks, myTotalClicks)
        return main
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConcludingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}