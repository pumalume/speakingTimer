package com.ingilizceevi.studentactivities0104.datacontroller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.ingilizceevi.studentactivities0104.gameModel
import com.ingilizceevi.vocabularycards0104.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DisplaySelectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplaySelectionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var main :View
    lateinit var myView : FrameLayout
    private val gameBrain: gameModel by activityViewModels()

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
        // Inflate the layout for this fragment
        main = inflater.inflate(R.layout.fragment_display_selection, container, false)
        myView = main.findViewById(R.id.myView)
        myView.setOnClickListener(View.OnClickListener {
            gameBrain.changeSelectionSignal.value =  this.tag.toString()
        })
        return main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txt = main.findViewById<TextView>(R.id.mySelectionView)
        txt.text = param1


    }

    fun giveMyString():String{
        return main.findViewById<TextView>(R.id.mySelectionView).text.toString()
    }
    fun giveMyId():String?{
        return param2
    }
    fun setElevation(){
        myView.bringToFront()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DisplaySelectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}