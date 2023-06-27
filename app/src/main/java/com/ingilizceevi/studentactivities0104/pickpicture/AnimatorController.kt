package com.ingilizceevi.studentactivities0104.pickpicture


import android.animation.AnimatorSet
import androidx.core.animation.doOnEnd
import com.ingilizceevi.studentactivities0104.pickpicture.ImageController

class AnimatorController(ic: ImageController) {
    private val imageController :ImageController = ic

    fun setupFadeOutAnimator():AnimatorSet{
        val fadeOutSet = AnimatorSet()
        val fadeList = imageController.panelOfFadeOutViews()
        fadeOutSet.playTogether(fadeList)
        return fadeOutSet
    }


}