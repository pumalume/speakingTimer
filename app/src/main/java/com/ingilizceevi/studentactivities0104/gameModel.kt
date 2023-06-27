package com.ingilizceevi.studentactivities0104

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.system.exitProcess

class gameModel: ViewModel() {

    lateinit var myStudentList : MutableList<StudentInfo>
    lateinit var myChapterList : MutableList<String>
    lateinit var studentInfo: StudentInfo
    var chapterSelection = ""
    var studentListIsInitialized = false
    var chapterListIsInitialized = false

    fun setDummyStudent(ss:String):StudentInfo{
        val s = StudentInfo(ss)
        s.studentFirstName = "Mario"
        s.studentLastName = "Bros"+ss
        return s
    }
    fun setDummyStudentList(){
        val s = setDummyStudent("-99")
        val tempStudentList = arrayListOf<StudentInfo>(s)
         val n = -99
        for(i in 0 until 9){
            val x = (n+1).toString()
            val zz = setDummyStudent(x)
            tempStudentList.add(zz)
        }
        myStudentList = tempStudentList
        studentListIsInitialized
    }

    fun setStudent(id:String){
        val index = studentMapper(id)
        val fName = myStudentList[index].studentFirstName
        val lName = myStudentList[index].studentLastName
        studentInfo = StudentInfo(id)
        studentInfo.studentFirstName = fName
        studentInfo.studentLastName = lName
    }
    fun studentMapper(id:String):Int{
        for(i in 0 until myStudentList.size) {
            if (myStudentList[i].studentID == id) return i
        }
        return -1
    }
    fun studentCreater(studentID : String, name:String, lastName:String):StudentInfo{
        val tempStudentInfo = StudentInfo(studentID)
        tempStudentInfo.studentFirstName = name
        tempStudentInfo.studentLastName = lastName
        return tempStudentInfo
    }


    fun calculateTimeToSeconds(totalTime:String):Int{
        val parts = totalTime.split(":")
        val minutes= parts[0].toInt()
        var seconds=parts[1].toInt()
        seconds = seconds + (minutes * 60)
        studentInfo.chapterTime = seconds
        return seconds
    }

    fun addStudent(student:StudentInfo){
        myStudentList.add(student)
    }
    fun loadMyStudentList(tl : MutableList<StudentInfo>)
    {
        myStudentList = tl
        studentListIsInitialized = true
    }
    fun flagIsRaisedForLoadedStudentList(filledStudentList:Boolean){
            dataIsLoaded.value = filledStudentList
    }
    val dataIsLoaded: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val chapterDataIsLoaded: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val nameIsSelected: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val chapterIsSelected: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val quitSignal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val startSignal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val changeSelectionSignal: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

}