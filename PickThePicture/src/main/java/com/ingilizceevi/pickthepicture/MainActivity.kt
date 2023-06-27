package com.ingilizceevi.pickthepicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    var studentID:Int = 0
    var studentName:String?=""
    var chapter :String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pickthepic_main)
        val bundle = intent.extras
        if (bundle != null) {
            studentName = bundle.getString("studentName")
            studentID= bundle.getInt("studentID")
            chapter= bundle.getString("chapter")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.GameFragment, GamePanel.newInstance(chapter!!, studentID!!), "GamePanel")
            .commit()

    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}