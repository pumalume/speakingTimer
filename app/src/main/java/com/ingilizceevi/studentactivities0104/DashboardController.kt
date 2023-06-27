package com.ingilizceevi.studentactivities0104

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.ingilizceevi.studentactivities0104.dataconnection.FatherConnection
import com.ingilizceevi.studentactivities0104.pickpicture.LaneViewModel
import com.ingilizceevi.vocabularycards0104.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardController : Fragment() {
    // TODO: Rename and change types of parameters
    private var studentName: String? = null
    private var studentId: String? = null
    lateinit var myDashboard: Dashboard
    lateinit var name:String
    lateinit var main:View
    private val gameBrain: gameModel by activityViewModels()
    lateinit var cancelButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentName = it.getString(ARG_PARAM1)
            studentId = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        main = inflater.inflate(R.layout.fragment_dashboard_controller, container, false)
        return main
    }

    override fun onResume() {
        super.onResume()
        myDashboard = childFragmentManager.findFragmentById(R.id.container_for_dashboard) as Dashboard
        if(studentName!=null)myDashboard.updateName(studentName!!)
        myDashboard.thisIsTheStartButton().setOnClickListener(myOnClickStartListener)
        myDashboard.thisIsTheStopButton().setOnClickListener(myOnClickStopListener)
        cancelButton = main.findViewById(R.id.cancelDashboard)
        cancelButton.setOnClickListener(myCancelOnClick)
    }
    val myOnClickStartListener = View.OnClickListener {
        val b = it as Button
        myDashboard.startButton()

    }

    val myCancelOnClick = View.OnClickListener {
        gameBrain.quitSignal.value = false
    }
    fun timerActivityIsStopped(){
        myDashboard.stopTime()
        gameBrain.quitSignal.value = true
    }
    val myOnClickStopListener = View.OnClickListener {
        val b = myDashboard.stopButton()
        gameBrain.quitSignal.value = true
        //b.setOnClickListener(myOnClickQuitListener)
        }

    val myOnClickQuitListener = View.OnClickListener {

        val b = it as Button
        gameBrain.quitSignal.value = true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardController.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardController().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}