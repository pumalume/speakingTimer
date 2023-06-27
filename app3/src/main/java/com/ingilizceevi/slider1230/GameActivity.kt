package com.ingilizceevi.slider1230

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_game)
        val extras = intent.extras
        var chapter: String?
        if (extras != null) {
            chapter = extras.getString("key")
            chapter = "chapter"+chapter
            supportFragmentManager.beginTransaction()
                .replace(R.id.gamePanel, GamePanel.newInstance(chapter!!, "EH"), "0")
                .commit()
        }

    }
    override fun onBackPressed() {}
}