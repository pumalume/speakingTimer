package com.ingilizceevi.studentactivities0104.pickpicture


import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import java.io.File


class FileLoader(chapter:String) {
    val myChapter:String = chapter

    fun fillAudioMap():MutableMap<String, Uri> {
        val myAudioMap: MutableMap<String, Uri> = mutableMapOf()
        val myPath = Environment.getExternalStorageDirectory().path + "/Music/chapter" + myChapter + "/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Music/MySounds/chapter" + myChapter + "/"
        File(myPath).walkBottomUp().forEach {
            if (it.isFile) {
                val u = Uri.parse(it.toString())
                val s = it.toString().substringAfterLast("/").dropLast(4)
                myAudioMap[s] = u
            }
        }
        return  myAudioMap
    }

    fun fillMyArray():MutableList<ConceptualObject> {
        val myListOfDrawableObjects : MutableList<ConceptualObject> = ArrayList(0)
        val myAudioMap = fillAudioMap()
        val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/chapter" + myChapter + "/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/MyImages/chapter" + myChapter + "/"
        File(myPath).walkBottomUp().forEach {
            if (it.isFile) {
                val d = Drawable.createFromPath(it.absolutePath)!!
                val s = it.toString().substringAfterLast("/").dropLast(4)
                val u = myAudioMap[s]
                val myConcept = ConceptualObject(d, s, u!!)
                myListOfDrawableObjects.add(myConcept)
            }
        }
        return myListOfDrawableObjects
    }
}