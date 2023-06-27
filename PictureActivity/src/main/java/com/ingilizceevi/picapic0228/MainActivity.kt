package com.ingilizceevi.picapic0228

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val bundle = intent.extras
//        var myString: String? = "01"
//        if (bundle != null) myString = bundle.getString("key")
        supportFragmentManager.beginTransaction()
            .replace(R.id.GameFragment, ImageFragment.newInstance(0), "GamePanel")
            .commit()

    }
}