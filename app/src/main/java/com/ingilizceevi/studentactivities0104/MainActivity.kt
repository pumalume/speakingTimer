package com.ingilizceevi.studentactivities0104

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ingilizceevi.studentactivities0104.datacontroller.PanelController
import com.ingilizceevi.vocabularycards0104.R


class MainActivity : AppCompatActivity() {
    private val SECOND_ACTIVITY_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // val myIntent = Intent(this, com.ingilizceevi.studentactivities0104.pickpicture.PictureActivity::class.java)
       // myIntent.putExtra("chapter", "03")
       // myIntent.putExtra("studentID", "01")
       // this.startActivity(myIntent)
    }

    override fun onBackPressed() {
        //
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if(requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
          //  if (resultCode == RESULT_OK) {
                var returnString = data?.getStringExtra("keyName")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, PanelController())
            .commit()
            //}
        //}
    }

}