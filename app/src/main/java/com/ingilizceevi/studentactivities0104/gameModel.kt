package com.ingilizceevi.studentactivities0104

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class gameModel: ViewModel() {

    lateinit var myStudentList : MutableList<StudentInfo>
    lateinit var myChapterList : MutableList<String>
    lateinit var chosenStudent: StudentInfo
    lateinit var chosenChapter: String
    var studentIsChosen = false
    var chapterIsChosen = false
    var studentListIsInitialized = false
    var chapterListIsInitialized = false

    fun setStudent(id:String, name:String){
        chosenStudent = StudentInfo(id, name)
        studentIsChosen = true
    }
    fun setChapter(chapter:String){
        chosenChapter = chapter
        studentIsChosen = true
    }


    fun concatanateStudentName():String{
        return chosenStudent.studentFirstName+" "+chosenStudent.studentLastName
    }
    fun calculateTimeToSeconds(totalTime:String):Int{
        val parts = totalTime.split(":")
        val minutes= parts[0].toInt()
        var seconds=parts[1].toInt()
        seconds = seconds + (minutes * 60)
        chosenStudent.chapterTime = seconds
        return seconds
    }

    fun studentListIsLoadedFrom(studentList : MutableList<StudentInfo>)
    {
        myStudentList = studentList
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
    val changeChapterSignal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val changeNameSignal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

}