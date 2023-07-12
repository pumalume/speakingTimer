package com.ingilizceevi.studentactivities0104.datacontroller

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ingilizceevi.studentactivities0104.StudentInfo
import com.ingilizceevi.studentactivities0104.dataconnection.MotherConnection
import com.ingilizceevi.studentactivities0104.gameModel
import com.ingilizceevi.vocabularycards0104.R
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PanelController.newInstance] factory method to
 * create an instance of this fragment.
 */
class PanelController : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var main : View
    private val gameBrain: gameModel by activityViewModels()
    private lateinit var namePanel:ElementPanel
    private lateinit var chapterPanel:ElementPanel
    private lateinit var boxForNames : FragmentContainerView
    private lateinit var boxForChapters : FragmentContainerView
    private lateinit var vocabularyButton :Button


//    private lateinit var pictureButton :Button


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
        main = inflater.inflate(R.layout.fragment_data_controller, container, false)
        return main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boxForChapters = main.findViewById(R.id.boxForChapterFragment)
        boxForNames = main.findViewById(R.id.boxForNameFragment)
        vocabularyButton = main.findViewById(R.id.vocabButton)
        vocabularyButton.setOnClickListener(onVocabClick)
    }

    override fun onStart() {
        super.onStart()
        if(gameBrain.studentIsChosen) putChosenStudentIntoBox()
        else {
            if (gameBrain.studentListIsInitialized) {
                putListOfStudentsInBox()
            } else spawnThreadToFetchNameDataFromServer()
        }
        if(gameBrain.chapterIsChosen)putChosenChapterIntoBox()
        else {
            if(gameBrain.chapterListIsInitialized) {
                putListOfChaptersInBox()
            } else {
                fetchChapterDataFromMemory()
                chapterPanelIsReady()
            }
        }
        observerIsEstablishedForChangeName()
    }
    fun putListOfChaptersInBox() {
        val chapterBox = childFragmentManager.findFragmentById(R.id.boxForChapterFragment) as ElementPanel
        if(gameBrain.chapterListIsInitialized)chapterBox.chaptersAreLoadedIntoViewFromGameBrain()
    }
    fun putChosenChapterIntoBox(){
        val chapterBox = childFragmentManager.findFragmentById(R.id.boxForChapterFragment) as ElementPanel
        if(gameBrain.chapterIsChosen)chapterBox.loadOneElement(gameBrain.chosenChapter, gameBrain.chosenChapter )
    }
    fun putListOfStudentsInBox() {
        val studentBox = childFragmentManager.findFragmentById(R.id.boxForNameFragment) as ElementPanel
        studentBox.namesAreLoadedIntoViewFromGameBrain()
    }
    fun putChosenStudentIntoBox(){
        val studentBox = childFragmentManager.findFragmentById(R.id.boxForNameFragment) as ElementPanel
        if(gameBrain.studentIsChosen)studentBox.loadOneElement(gameBrain.concatanateStudentName(), gameBrain.chosenStudent.studentID )
    }
//        val onPictureButtonClick = View.OnClickListener {
//        val intent = Intent(requireContext(), com.ingilizceevi.studentactivities0104.pickpicture.PictureActivity::class.java)
//        intent.putExtra("studentID", gameBrain.studentInfo.studentID)
//        intent.putExtra("chapter", gameBrain.chapterSelection)
//        startActivityForResult(intent, 0)
//        //startActivity(intent)
//   }

    val onVocabClick = View.OnClickListener {
        if(gameBrain.chapterIsChosen and gameBrain.studentIsChosen)
                gameBrain.startSignal.value = true
        }

    val myOnNameClickListener = View.OnClickListener{
        val t = it as TextView
        val name = t.text.toString()
        val id = t.tag.toString()
        gameBrain.setStudent(id,name)
        putChosenStudentIntoBox()
    }


    fun setListenerForOnNameClick(signal:Boolean){
        val box = childFragmentManager.findFragmentById(R.id.boxForNameFragment) as ElementPanel
        box.setOnElementClicker({
            if(signal==false){
                val t = it as TextView
                val name = t.text.toString()
                val id = t.tag.toString()
                gameBrain.setStudent(id, name)
            }
            else gameBrain.studentIsChosen = false
            gameBrain.changeNameSignal.value = signal
        })
    }
    fun onChapterClickIsSetToListen(){
        val box = childFragmentManager.findFragmentById(R.id.boxForChapterFragment) as ElementPanel
        box.setOnElementClicker({
            val t = it as TextView
            val chapter = t.text.toString()
            val id = t.tag.toString()
            gameBrain.setChapter(chapter)
            gameBrain.changeChapterSignal.value = false
        })
    }

    fun getChapterCount(): Array<String>? {
        //val myPath = Environment.getExternalStorageDirectory().path + "/Documents/MySounds/" + myChapter + "/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/MyImages/"
        val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/"
        val myfile = File(myPath)
        return myfile.list()
    }
    fun spawnThreadToFetchNameDataFromServer(){
        val myConnection = MotherConnection(gameBrain)
        myConnection.execute("nameConnection")
        observerIsEstablishedForConnectionComplete()
    }
    fun spawnThreadToFetchChapterData(){
        observerIsEstablishedForChapterSummaryComplete()
        val thread = Thread{ fetchChapterDataFromMemory() }
        thread.start()

    }

    fun fetchChapterDataFromMemory() {
        val array = getChapterCount()
        val myList = array!!.toMutableList()
        for (i in 0 until myList.size) myList[i] = myList[i].removePrefix("chapter")
        myList.sort()
        gameBrain.myChapterList = myList
        if(gameBrain.myChapterList.isNotEmpty()){
            gameBrain.chapterListIsInitialized = true
            gameBrain.chapterDataIsLoaded.value = true
        }
    }



    fun chosenNameIsReady(signal:Boolean){
        putChosenStudentIntoBox()
        setListenerForOnNameClick(true)
    }
    fun namePanelIsReady(){
        putListOfStudentsInBox()
        setListenerForOnNameClick(false)
    }

    fun chosenChapterIsReady(){
        putChosenChapterIntoBox()
       // resetTheChapterListener()
    }
    fun chapterPanelIsReady(){
        putListOfChaptersInBox()
        onChapterClickIsSetToListen()
    }

    fun fillTempList(){
        val temp1 = StudentInfo("-1", "Mario Bros")
        val temp2 = StudentInfo("-2", "Luigi Bros")
        val temp3 = StudentInfo("-3", "Bowser Boss Monster")
        val temp4 = StudentInfo("-4", "Princess Peach")
        val temp5 = StudentInfo("-5", "Toad Guy")
        val mutable_list = mutableListOf<StudentInfo>(temp1,temp2,temp3, temp4, temp5)
        gameBrain.myStudentList = mutable_list
    }
    fun observerIsEstablishedForConnectionComplete() {
        val dataIsLoadedAction = Observer<Boolean> {
            if(it != true)fillTempList()
            gameBrain.studentListIsInitialized = true
            namePanelIsReady()
        }
        gameBrain.dataIsLoaded.observe(viewLifecycleOwner, dataIsLoadedAction)
    }
    fun observerIsEstablishedForChapterSummaryComplete(){
        val chapterDataIsLoadedAction = Observer<Boolean> {
            chapterPanelIsReady()
        }
        gameBrain.chapterDataIsLoaded.observe(viewLifecycleOwner, chapterDataIsLoadedAction)
    }
    fun observerIsEstablishedForChangeName(){
        val nameIsChanged = Observer<Boolean> {
            if(it == true)namePanelIsReady()
            else chosenNameIsReady(false)
        }
        gameBrain.changeNameSignal.observe(viewLifecycleOwner, nameIsChanged)
    }

    fun observerIsEstablishedForChangeChapter(){
        val chapterIsChanged = Observer<Boolean> {
            if(it == true)putListOfChaptersInBox()
            else putChosenChapterIntoBox()
        }
        gameBrain.changeChapterSignal.observe(viewLifecycleOwner, chapterIsChanged)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PanelController().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}