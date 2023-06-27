package com.ingilizceevi.slider1230

import android.animation.ValueAnimator
import android.view.animation.*
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnPause
import androidx.core.animation.doOnRepeat
import androidx.core.animation.doOnStart

data class LaneAnimatorAttributes(val tag:Int){
    val laneTag = tag
    var laneLength : Float = 0F
    var imagePosition: Float = 0F
    var laneDuration: Long = 4500L
    var imageOffset : Float = 0F
    var height:Int = 0
    var width:Int = 0
}
class LaneAnimator(fl:FrameLayout) {

    var mlaneAnimator: ValueAnimator? = null
    lateinit var laneAttr :LaneAnimatorAttributes
    lateinit var originalAttr: LaneAnimatorAttributes
    private val myImageFrame : FrameLayout = fl

    fun setOriginalAttributes(){
        originalAttr = laneAttr
    }

    fun cancelAnimation(){
        if(mlaneAnimator!=null&&mlaneAnimator!!.isRunning){
            mlaneAnimator!!.removeAllListeners()
            mlaneAnimator!!.end()
        }
    }

    fun isThereEnoughTimeToFadeAway(fadeDuration:Long):Boolean{
        val standardDuration =laneAttr.laneDuration
        val passedTime = mlaneAnimator!!.currentPlayTime
        val remainingTime = standardDuration-passedTime
        return (remainingTime-100)>fadeDuration
    }
    fun durationIsReduced(){
        if(laneAttr.laneDuration>2500){
            laneAttr.laneDuration-=200
        }
    }

    fun startPositionIsCalculatedForOffset(){
        if(laneAttr.imagePosition==0F) laneAttr.imagePosition -= laneAttr.imageOffset!!
    }

    fun laneAnimatorIsReady():Boolean {
        if(mlaneAnimator!=null&&mlaneAnimator!!.isRunning)mlaneAnimator!!.end()
        startPositionIsCalculatedForOffset()
        mlaneAnimator = ValueAnimator.ofFloat(laneAttr.imagePosition, laneAttr.laneLength)
        mlaneAnimator!!.addUpdateListener {
                val value = mlaneAnimator!!.animatedValue as Float
                myImageFrame.translationX = value
        }
//        mlaneAnimator!!.interpolator = LinearInterpolator()
        mlaneAnimator!!.interpolator = AccelerateInterpolator()
        mlaneAnimator!!.duration = laneAttr.laneDuration
        //mlaneAnimator.repeatCount = -1
        return true
    }
}


/*
fun isStarted(){
    mlaneAnimator!!.start()
}
fun getHandleOnAnimator():ValueAnimator{
    return mlaneAnimator!!
}
fun currentPosition():Float{return mlaneAnimator!!.animatedValue as Float}
  fun laneIsResetToOriginalPosition() {
        laneAttr = originalAttr
        laneAnimatorIsReady()
    }
    fun durationIsUpdatedWithCurrentTime():Long{
        laneAttr.laneDuration -= mlaneAnimator!!.currentPlayTime
        return laneAttr.laneDuration
    }
*/