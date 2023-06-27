package com.ingilizceevi.slider1230

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.rotationMatrix

class ImageAnimator() {

    fun simpleFadeOut(myImageView: ImageView, duration:Long):ObjectAnimator{
        val myFadeOutAnimator = ObjectAnimator.ofFloat(myImageView, "alpha", 1F, 0F)
        myFadeOutAnimator.duration = duration
        //myFadeOutAnimator.start()
        return myFadeOutAnimator
    }

        fun myEnlarger(myImageView: ImageView):ScaleAnimation{
        val amim = ScaleAnimation(
            1f, 1.5f,
            1.0f, 1.5f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        amim.duration=500
        amim.fillAfter= true
        /*
        amim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                simpleFadeOut(myImageView, 400)
                //myImageView.visibility = View.VISIBLE
            }
        })
        //myImageView.startAnimation(amim)
         */
        return amim
    }

    fun myShaker():RotateAnimation{
        val rotate = RotateAnimation(-3.0f,
            3f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f)
        rotate.duration = 500
        rotate.startOffset = 0
        rotate.repeatMode = Animation.REVERSE
        rotate.interpolator = CycleInterpolator(25f)
        return rotate
        }

}
