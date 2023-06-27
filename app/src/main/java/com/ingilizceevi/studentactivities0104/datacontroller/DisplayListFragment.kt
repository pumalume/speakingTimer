package com.ingilizceevi.studentactivities0104.datacontroller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import com.ingilizceevi.studentactivities0104.gameModel
import com.ingilizceevi.vocabularycards0104.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [DisplayListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplayListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var main : View
    private lateinit var scroller : LinearLayout
    private var scrollSize:Int = 0
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
        main = inflater.inflate(R.layout.fragment_display_list, container, false)
        scroller = main.findViewById(R.id.scroll_view)
        return main
    }

    override fun onResume() {
        super.onResume()
        if(this.tag == "names"){
            if(gameBrain.studentListIsInitialized)loadNamesIntoView()
        }
    }


    fun viewTagIs(index:Int):Int{
        return scroller.getChildAt(index).tag.toString().toInt()
    }

    fun handleOnScrollElement(index:Int):TextView{
        val textview = scroller.getChildAt(index) as TextView
        return textview
    }
    fun sizeOfScroll():Int{return scrollSize}
    fun addListOfStringsToView(list : MutableList<String>){

        val size = list!!.size
        for(i in 0 until size  ){
            addStringToView(list[i], i.toString())
        }
    }

    fun loadNamesIntoView():Boolean{
        emptyTheScroll()
        val tempList = gameBrain.myStudentList
        for(i in 0 until tempList.size){
            val name = tempList[i].studentFirstName + " " + tempList[i].studentLastName
            val id = tempList[i].studentID
            addStringToView(name, id.toString())
        }
        return true
    }

    fun addStringToView(displayString:String, stringID:String) {
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(15)
        val tempText = TextView(context)
        tempText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tempText.layoutParams = lp
        tempText.setPadding(10)
        tempText.text = displayString
        tempText.tag = stringID
        //tempText.textSize = 30f
        scroller.addView(tempText)
        scrollSize++
    }

    fun emptyTheScroll(){
        scroller.removeAllViews()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DisplayListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}