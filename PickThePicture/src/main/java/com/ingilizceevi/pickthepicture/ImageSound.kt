package com.ingilizceevi.pickthepicture


import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels

class ImageSound(context:Context) {

    val myContext:Context = context
    var mMediaPlayer : MediaPlayer? = null


    fun playSound(uri:Uri):MediaPlayer {
        if (mMediaPlayer != null) {
            if (mMediaPlayer!!.isPlaying)
                mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        mMediaPlayer = MediaPlayer()
        mMediaPlayer!!.setDataSource(myContext, uri)
        mMediaPlayer!!.prepare()
        return mMediaPlayer as MediaPlayer
        /*
            mMediaPlayer = MediaPlayer().apply {
                setDataSource(myContext, uri)
                setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                )
                prepare()
                start()
            }

         */
    }

}