package com.ingilizceevi.studentactivities0104.pickpicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.ingilizceevi.studentactivities0104.StudentInfo
import com.ingilizceevi.vocabularycards0104.R

class PictureActivity : AppCompatActivity() {
    var studentID:String? = ""
    var chapter :String? = ""
    private val gameBrain: LaneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pickthepic_main)
        val bundle = intent.extras
        if (bundle != null) {
            studentID= bundle.getString("studentID")
            chapter= bundle.getString("chapter")
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.GameFragment, GamePanel.newInstance(chapter!!, studentID!!), "GamePanel")
            //.replace(R.id.GameFragment, ImageFragment(), "GamePanel")
            .commit()

    }

    override fun onBackPressed() {
            //super.onBackPressed()
        }
}