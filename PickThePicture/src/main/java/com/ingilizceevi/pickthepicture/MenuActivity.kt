package com.ingilizceevi.pickthepicture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import java.io.File


class MenuActivity : AppCompatActivity() {
    val myListOfChapters: MutableList<String> = ArrayList(0)
    lateinit var myVerticalView: LinearLayout
    var myChapterSelection:String? = null
    var studentName:String? = null
    var studentID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        studentName = bundle!!.getString("studentName")
        studentID = bundle!!.getInt("studentID")
        StrictMode.setVmPolicy(
            VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build()
        )
        setContentView(R.layout.activity_menu)
        myVerticalView = findViewById(R.id.myVerticalLayout)
        val tv:TextView = findViewById(R.id.studentNameText)
        tv.text = studentName
        tv.textSize=30f
        val button = findViewById<ImageView>(R.id.menuCancel)
        button.setOnClickListener{finish()}
        addChapterList(this)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
    override fun onResume() {
        super.onResume()
        if(this::myVerticalView.isInitialized){
            if(myChapterSelection!=null){
                val t = (myChapterSelection!!.toInt())-1
                val v = myVerticalView.getChildAt(t) as TextView
                v.setBackgroundColor(resources.getColor(R.color.EHYellow))
            }
            myChapterSelection=null
        }
    }
    fun addChapterList(activity: Activity){

        val myColor = resources.getColor(R.color.EHYellow)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(15)
        val myScroll = getChapterListSize()
        if (myScroll != null) {
            for(i in 0 until myScroll.size) {

                val tempText = TextView(activity)
                tempText.textAlignment = View.TEXT_ALIGNMENT_CENTER
                tempText.layoutParams = lp
                tempText.setBackgroundColor(myColor)
                tempText.setPadding(10)

               // var myChapterString = ""
               // var myChapter = i+1
                //if(myChapter < 10 )myChapterString = myScroll[i]
               // else myChapterString = myChapter.toString()
                tempText.text = myScroll[i]
                tempText.textSize = 50f
                myVerticalView.addView(tempText)
            }
        }
        if (myScroll != null) {
            for(i in myScroll.indices) {
                val tempView = myVerticalView.getChildAt(i) as TextView
                tempView.setOnClickListener(myOnClickListener)
            }
        }
    }

    val myOnClickListener = View.OnClickListener{
        val textView = it as TextView
        textView.setBackgroundColor(resources.getColor(R.color.white))
        myChapterSelection = textView.text.toString()
        val intent = Intent(this, MainActivity::class.java)
            .apply {
                putExtra("myChapter", myChapterSelection)
                putExtra("studentID", studentID)
            }
        this.startActivity(intent)
    }
    fun getChapterListSize(): Array<out String>? {
        var counter = 0
        val myChapterList: MutableList<String> = ArrayList(0)
        val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/MyImages/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Pictures/MyImages/"
        //File(myPath).walkTopDown().forEach {
        //    if (it.isDirectory) {
        //        val t = it.list()
        //    }
        //}
        return File(myPath).list()
    }
}