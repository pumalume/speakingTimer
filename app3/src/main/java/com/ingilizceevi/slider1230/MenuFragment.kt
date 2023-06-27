package com.ingilizceevi.slider1230

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import java.io.File


class MenuFragment : Fragment() {

    val myListOfChapters: MutableList<String> = ArrayList(0)
    lateinit var myVerticalView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getChapterList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val myMainView = inflater.inflate(R.layout.fragment_menu, container, false)
        myVerticalView = myMainView.findViewById<LinearLayout>(R.id.myVerticalLayout)
        addChapterList()
        return myMainView
    }

    fun addChapterList(){

        val myInt = myListOfChapters.size
        var myID:Int = 123

        for(i in 0 until myInt) {
            val myFragContainer = FragmentContainerView(requireContext())
            myFragContainer.id =myID
            val myTextCard :TextcardFragment = TextcardFragment
                .newInstance(myListOfChapters[i], "IngilizceEvi")
            childFragmentManager.beginTransaction()
                .add(myID, myTextCard, myListOfChapters[i])
                .commit()
            myVerticalView.addView(myFragContainer)
            myID++
        }
        val childCount = myVerticalView.childCount
        for(i in 0 until childCount) {
            myVerticalView.getChildAt(i).setOnClickListener(myOnClickListener)
        }
    }

    val myOnClickListener = View.OnClickListener{
        val textView = it.findViewById<TextView>(R.id.cardText)
        val myText = textView.text.toString()
        val intent = Intent(requireActivity(), GameActivity::class.java)
            .apply { putExtra("key", myText) }
        requireActivity().startActivity(intent)
    }
    fun getChapterList(){
        val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/MyImages/"
        val myList: MutableList<String> = ArrayList(0)
        File(myPath).walkTopDown().forEach {
            if (it.isDirectory) {
                val chapter = it.toString().substringAfterLast("/")
                myListOfChapters.add(chapter)
            }
        }
        myListOfChapters.removeAt(0)
        myListOfChapters.sort()
    }
}

/*
    fun myClickedText(v:FragmentContainerView) {
        val tempView = v.findViewById<TextView>(R.id.cardText)
        val myString = "chapter" + tempView.text.toString()
        val myTable = TableFragment.newInstance(myString, "EnglishHouse")
        viewModel.myChapter = myString
        viewModel.fillMyArray()
        viewModel.fillAudioMap()
        parentFragmentManager.beginTransaction()
            .replace(R.id.myMainFragmentContainerView, myTable, "Table")
            .commit()

        //   val intent = Intent(requireActivity(), GameActivity::class.java)
        //      .apply {
        //          putExtra("chapter", "chapter03")
        //     }
        // requireActivity().startActivity(intent)
    }


 */
