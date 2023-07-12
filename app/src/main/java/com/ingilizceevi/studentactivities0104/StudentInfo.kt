package com.ingilizceevi.studentactivities0104

public class StudentInfo(id: String, name:String){
    var studentID:String = ""
    var studentFirstName:String = ""
    var studentLastName:String= ""
    var chapterNum:String = ""
    var chapterTime:Int = 0
    var chapterClick:Int = 0

    init{
        studentID = id
        studentFirstName = name
    }
}

