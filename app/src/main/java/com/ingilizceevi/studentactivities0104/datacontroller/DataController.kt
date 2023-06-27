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
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DataController.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataController : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var main : View
    private val gameBrain: gameModel by activityViewModels()
    private lateinit var studentListFragment:DisplayListFragment
    private lateinit var chapterListFragment:DisplayListFragment
    private lateinit var chapterSelectionFragment:DisplaySelectionFragment
    private lateinit var nameSelectionFragment:DisplaySelectionFragment
    private lateinit var topNameView : FragmentContainerView
    private lateinit var bottomChapterView : FragmentContainerView
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
        bottomChapterView = main.findViewById(R.id.bottomViewForChapter)
        studentListFragment = childFragmentManager.findFragmentById(R.id.topViewForNames) as DisplayListFragment
        chapterListFragment = childFragmentManager.findFragmentById(R.id.bottomViewForChapter) as DisplayListFragment
        vocabularyButton = main.findViewById(R.id.vocabButton)
        //pictureButton = main.findViewById(R.id.picButton)
        //pictureButton.setOnClickListener(onPictureButtonClick)
        vocabularyButton.setOnClickListener(onVocabClick)
        observerIsEstablishedForChangeSelection()

    }

    override fun onStart() {
        super.onStart()
        if(!gameBrain.studentListIsInitialized) {
            spawnThreadToFetchNameDataFromServer()
        }
        else{
            loadNamesIntoView()
            setNameOnClickListener()
        }
        if(!gameBrain.chapterListIsInitialized) {
            fetchChapterDataFromMemory()
        }
        loadChaptersIntoView()
        setChapterOnClickListener()
    }

//        val onPictureButtonClick = View.OnClickListener {
//        val intent = Intent(requireContext(), com.ingilizceevi.studentactivities0104.pickpicture.PictureActivity::class.java)
//        intent.putExtra("studentID", gameBrain.studentInfo.studentID)
//        intent.putExtra("chapter", gameBrain.chapterSelection)
//        startActivityForResult(intent, 0)
//        //startActivity(intent)
//   }
    val onVocabClick = View.OnClickListener {
        if (checkReadinessForStartGame()){
            gameBrain.chapterSelection = chapterSelectionFragment.giveMyId()!!
            gameBrain.studentInfo = StudentInfo(nameSelectionFragment.giveMyId()!!)
            gameBrain.studentInfo.studentFirstName = nameSelectionFragment.giveMyString()
            gameBrain.studentInfo.chapterNum = chapterSelectionFragment.giveMyId()!!
            if (!gameBrain.chapterSelection.isNullOrEmpty() and !gameBrain.studentInfo.studentID.isNullOrEmpty()) {
                gameBrain.startSignal.value = true
            }
        }
}


    fun checkReadinessForStartGame():Boolean{
        return checkViewForName() and checkViewForChapter()
    }

    fun checkViewForChapter():Boolean{
        val temp = childFragmentManager.findFragmentById(R.id.bottomViewForChapter)
        if(temp is DisplaySelectionFragment) {
            return true
        }
        else {
            return false
        }
    }
    fun checkViewForName():Boolean{
        val temp = childFragmentManager.findFragmentById(R.id.topViewForNames)
        if(temp is DisplaySelectionFragment)return true
        else return false
    }
    fun getChapterCount(): Array<String>? {
        //val myPath = Environment.getExternalStorageDirectory().path + "/Documents/MySounds/" + myChapter + "/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/MyImages/"
        val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/"
        val myfile = File(myPath)
        return myfile.list()
    }
    fun getHandleOnStudentListFragment():Boolean{
        studentListFragment = childFragmentManager.findFragmentById(R.id.topViewForNames) as DisplayListFragment
        return studentListFragment.isAdded
    }
    fun getHandleOnChapterListFragment():Boolean{
        studentListFragment = childFragmentManager.findFragmentById(R.id.bottomViewForChapter) as DisplayListFragment
        return studentListFragment.isAdded
    }
    fun spawnThreadToFetchNameDataFromServer(){
        val myConnection = MotherConnection(gameBrain)
        myConnection.execute("nameConnection")
        observerIsEstablishedForConnectionComplete()
    }
    fun spawnThreadToFetchChapterData(){
        val thread = Thread{ fetchChapterDataFromMemory() }
        thread.start()
        observerIsEstablishedForChapterSummaryComplete()
    }

    fun fetchChapterDataFromMemory() {
        val array = getChapterCount()
        val myList = array!!.toMutableList()
        for (i in 0 until myList.size) myList[i] = myList[i].removePrefix("chapter")
        myList.sort()
        gameBrain.myChapterList = myList
        if(gameBrain.myChapterList.isNotEmpty()){
            gameBrain.chapterListIsInitialized = true
            //gameBrain.chapterDataIsLoaded.value = true
        }
    }


    fun loadChaptersIntoView():Boolean{
        if(!gameBrain.chapterListIsInitialized)return false
        if(!getHandleOnChapterListFragment())return false
        chapterListFragment.addListOfStringsToView(gameBrain.myChapterList)
        return true
    }
    fun loadNamesIntoView():Boolean{
        if(!getHandleOnStudentListFragment())return false
        studentListFragment.emptyTheScroll()
        val tempList = gameBrain.myStudentList
        for(i in 0 until tempList.size){
            val name = tempList[i].studentFirstName + " " + tempList[i].studentLastName
            val id = tempList[i].studentID
            studentListFragment.addStringToView(name, id.toString())
        }
        return true
    }

    fun theListViewsArePopulated(){
        loadNamesIntoView()
        setNameOnClickListener()
        fetchChapterDataFromMemory()
        setChapterOnClickListener()
    }

    fun fillTempList(){
        val temp1 = gameBrain.studentCreater("-1", "Mario", "Bros")
        val temp2 = gameBrain.studentCreater("-2", "Luigi", "Bros")
        val temp3 = gameBrain.studentCreater("-3", "Bowser", "Boss Monster")
        val temp4 = gameBrain.studentCreater("-4", "Princess", "Peach")
        val temp5 = gameBrain.studentCreater("-5", "Toad", "Guy")

        val mutable_list = mutableListOf<StudentInfo>(temp1,temp2,temp3, temp4, temp5)
        gameBrain.loadMyStudentList(mutable_list)
    }
    fun observerIsEstablishedForConnectionComplete() {
        val dataIsLoadedAction = Observer<Boolean> {
            if(it != true)fillTempList()
            loadNamesIntoView()
            setNameOnClickListener()
        }
        gameBrain.dataIsLoaded.observe(viewLifecycleOwner, dataIsLoadedAction)
    }
    fun observerIsEstablishedForChapterSummaryComplete(){
        val chapterDataIsLoadedAction = Observer<Boolean> {
            loadChaptersIntoView()
            setChapterOnClickListener()
        }
        gameBrain.chapterDataIsLoaded.observe(viewLifecycleOwner, chapterDataIsLoadedAction)
    }

    val nameOnClickListener = View.OnClickListener {
        topNameView = main.findViewById(R.id.topViewForNames)
        val myTextView = it as TextView
        val id = myTextView.tag.toString()
        nameSelectionFragment = DisplaySelectionFragment.newInstance(myTextView.text.toString(), id)
        createSelectionFragment(topNameView.id, nameSelectionFragment, "studentName")

    }
    fun observerIsEstablishedForChangeSelection(){
        val selectionIsChangedAction = Observer<String> {
            if(it == "studentName"){
                loadNameListFragment()
                loadNamesIntoView()
            }
            if(it =="chapter"){}
            setChapterOnClickListener()
        }
        gameBrain.changeSelectionSignal.observe(viewLifecycleOwner, selectionIsChangedAction)
    }


    val chapterOnClickListener = View.OnClickListener {
        bottomChapterView = main.findViewById(R.id.bottomViewForChapter)
        val myTextView = it as TextView
        val id = myTextView.tag.toString()
        chapterSelectionFragment = DisplaySelectionFragment.newInstance(myTextView.text.toString(), id)
        createSelectionFragment(bottomChapterView.id, chapterSelectionFragment, "chapter")
    }


    fun setNameOnClickListener():Boolean {
        if (!getHandleOnStudentListFragment()) return false
        val size = studentListFragment.sizeOfScroll()
        for (i in 0 until size) {
            val scrollElement = studentListFragment.handleOnScrollElement(i)
            scrollElement.setOnClickListener(nameOnClickListener )
        }
        return true
    }

    //scrolls through each element in scroll
    //places onClick listener on each element
    fun setChapterOnClickListener():Boolean {
        if (!getHandleOnChapterListFragment()) return false
        val size = chapterListFragment.sizeOfScroll()
        for (i in 0 until size) {
            val scrollElement = chapterListFragment.handleOnScrollElement(i)
            scrollElement.setOnClickListener(chapterOnClickListener )
        }
        return true
    }

    fun reloadNamesListIntoTopView(){
        loadNameListFragment()
        loadNamesIntoView()
        setNameOnClickListener()
    }
    //creates a fragment to display list of students in top window
    fun loadNameListFragment(){
        studentListFragment = DisplayListFragment()
        childFragmentManager.beginTransaction()
            .replace(topNameView.id, studentListFragment, "StudentList")
            .commitNow()
    }

    fun loadChapterListFragment(){
        chapterListFragment = DisplayListFragment()
        childFragmentManager.beginTransaction()
            .replace(bottomChapterView.id, chapterListFragment, "ChapterList")
            .commitNow()
    }


    //creates a fragment for top/bottom windows to display selected text
    fun createSelectionFragment(viewID:Int, selectionFragment:DisplaySelectionFragment, studentId:String){
        childFragmentManager.beginTransaction()
            .replace(viewID, selectionFragment, studentId)
            .commitNow()
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DataController().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}