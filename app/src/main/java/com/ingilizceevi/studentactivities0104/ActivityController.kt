package com.ingilizceevi.studentactivities0104

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ingilizceevi.studentactivities0104.dataconnection.FatherConnection
import com.ingilizceevi.studentactivities0104.datacontroller.PanelController
import com.ingilizceevi.vocabularycards0104.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActivityController.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActivityController : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var main:View
    lateinit var activity_container : FragmentContainerView
    private val gameBrain: gameModel by activityViewModels()
    var panelController = PanelController
    lateinit var dashboard_controller : DashboardController
    lateinit var backDoor : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main = inflater.inflate(R.layout.fragment_activity_controller, container, false)
        activity_container = main.findViewById(R.id.container_for_activity)
        backDoor = main.findViewById(R.id.door)
        backDoor.setOnClickListener(backDoorOutOnClickListener)
        observerIsEstablishedQuitActivy()
        observerIsEstablishedStartActivity()
        return main
    }

    fun observerIsEstablishedQuitActivy() {
        val quitActivity = Observer<Boolean> {
            val sInfo = gameBrain.chosenStudent
            if(it == true) {
                val myConnection = FatherConnection(gameBrain.chosenStudent)
                myConnection.execute("nameConnection")
            }

            //puy thid in drprerate function

            childFragmentManager.beginTransaction()
                .replace(activity_container.id, PanelController())
                .commit()
            backDoor.visibility = View.VISIBLE
            backDoor.setOnClickListener(backDoorOutOnClickListener)
            //
        }
        gameBrain.quitSignal.observe(viewLifecycleOwner, quitActivity )
    }

    fun observerIsEstablishedStartActivity() {
        val startActivity = Observer<Boolean> {

            val id = gameBrain.chosenStudent.studentID
            val name = gameBrain.chosenStudent.studentFirstName
            dashboard_controller = DashboardController.newInstance(name, id)
            childFragmentManager.beginTransaction()
                .replace(activity_container.id, dashboard_controller, id)
                .commit()
            backDoor.visibility = View.INVISIBLE
            backDoor.setOnClickListener(null)
        }
        gameBrain.startSignal.observe(viewLifecycleOwner, startActivity )
    }
    val backDoorOutOnClickListener = View.OnClickListener {
        requireActivity().finish()
    }

    val backDoorIn_OnClickListener = View.OnClickListener {
        //dashboard_controller.myDashboard.
        childFragmentManager.beginTransaction()
            .replace(activity_container.id, PanelController())
            .commit()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActivityController().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}