package com.ingilizceevi.slider1230

import android.animation.ValueAnimator
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels

//new instance contructor parameters
private const val ARG_PARAM1 = "param1"


class LaneFragment : Fragment() {

    private var laneTag: Int? = null
    private val laneModel: LaneViewModel by activityViewModels()
    lateinit var myImageFragmentContainer: FragmentContainerView
    lateinit var main : View
    lateinit var myImageFragment: ImageFragment
    lateinit var myImageFrame : FrameLayout
    var myLaneAnimator : LaneAnimator? = null
    lateinit var laneAttr : LaneAnimatorAttributes
    var orientation:Int = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            laneTag = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        main = inflater.inflate(R.layout.fragment_lane, container, false)
        myImageFragmentContainer = main.findViewById(R.id.imageFragmentContainerView)
        myImageFragmentContainer.setOnClickListener(myOnClickListener)
        return main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceImageFragmentContainerWithNewInstanceOfImageFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(myLaneAnimator!=null
            && myLaneAnimator!!.mlaneAnimator!=null
            && myLaneAnimator!!.mlaneAnimator!!.isRunning){
            myLaneAnimator!!.mlaneAnimator!!.end()
        }
    }

    fun replaceImageFragmentContainerWithNewInstanceOfImageFragment(){
        myImageFragment = ImageFragment.newInstance(laneTag!!)
        childFragmentManager.beginTransaction()
            .replace(R.id.imageFragmentContainerView, myImageFragment, laneTag.toString())
            .commit()
    }
    fun imageFragmentHandle():ImageFragment{
        this.myImageFragment = childFragmentManager.findFragmentById(R.id.imageFragmentContainerView) as ImageFragment
        return this.myImageFragment
    }

//This section code sets up the laneAnimator

    fun instantiateLaneAnimator():ValueAnimator{
        if(myLaneAnimator!=null)myLaneAnimator!!.cancelAnimation()
        if (myLaneAnimator == null) {
            myImageFrame = main.findViewById(R.id.myImageFrame)
            myLaneAnimator = LaneAnimator(myImageFrame)
            myLaneAnimator!!.laneAttr = getMyLaneAttributesObject()
            myLaneAnimator!!.setOriginalAttributes()
        }
        myLaneAnimator!!.laneAnimatorIsReady()
        return myLaneAnimator!!.mlaneAnimator!!
    }
    fun getMyLaneAttributesObject():LaneAnimatorAttributes{
        laneAttr = LaneAnimatorAttributes(laneTag!!)
        laneAttr.laneLength = getLengthOfLaneFragment()
        laneAttr.imageOffset = getImageFrameOffset()
        laneAttr.height = myImageFragment.imageView.height
        laneAttr.width = myImageFragment.imageView.width
        return laneAttr
    }

    fun listenerIsSetSoThatImageFadesOutAtAEndOfBigger(bigger:ScaleAnimation){
        bigger.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                val faderOuter = myImageFragment.isFaderOuter(false)
                if(myLaneAnimator!!.isThereEnoughTimeToFadeAway(bigger.duration)) {
                    faderOuter.doOnEnd {
                        myLaneAnimator!!.mlaneAnimator!!.end()
                    }
                }
                faderOuter.start()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    fun listenerIsSetSoThatImageFadesOutAtAEndOfShaking(shaker:RotateAnimation){
        shaker.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                val faderOuter = myImageFragment.isFaderOuter(false)
                if(myLaneAnimator!!.isThereEnoughTimeToFadeAway(shaker.duration)) {
                    faderOuter.doOnEnd {
                        myLaneAnimator!!.mlaneAnimator!!.end()
                    }
                }
                faderOuter.start()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
    fun imageIsBigger(){
        val bigger = imageFragmentHandle().imageIsBigger(false)
        listenerIsSetSoThatImageFadesOutAtAEndOfBigger(bigger)
        imageFragmentHandle().myFrameView.startAnimation(bigger)
        //imageFragmentHandle().imageView.startAnimation(bigger)
    }

    fun imageIsShaken(){
        val shaker = imageFragmentHandle().shakeImage(false)
        listenerIsSetSoThatImageFadesOutAtAEndOfShaking(shaker)
        imageFragmentHandle().imageView.startAnimation(shaker)
    }
    fun getImageFrameOffset():Float{
        var offset: Float
        val myImageFrame = main.findViewById<FrameLayout>(R.id.myImageFrame)
        if(orientation==1) offset = myImageFrame.height.toFloat()
        else offset = myImageFrame.width.toFloat()
        return offset
    }
    fun getLengthOfLaneFragment():Float{
        var length = 0F
        val width: Int = Resources.getSystem().displayMetrics.widthPixels
        val height: Int = Resources.getSystem().displayMetrics.heightPixels
        val myLaneFrame = main.findViewById<LinearLayout>(R.id.laneFrame)
        if (orientation == 1) length = myLaneFrame.height.toFloat()
        if (orientation == 2) length = myLaneFrame.width.toFloat()
        return width.toFloat()
    }

    fun imageClickedListenerIsSetup(){
        myImageFragmentContainer.setOnClickListener(myOnClickListener)
    }


    //----methods deal with the on click listener----//

    val myOnClickListener = View.OnClickListener {
        myImageFragmentContainer.setOnClickListener(null)
        laneModel.gameBrain.incrementClickedCounter()
        laneModel.imageIsClickedLiveData.value = laneTag
    }
    fun onClickIsSetToNull(clickerIsDisabled:Boolean){
        if(clickerIsDisabled)myImageFrame.setOnClickListener(null)
        else myImageFrame.setOnClickListener(myOnClickListener)
    }

    //////////////////////////////////////////////
    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            LaneFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

}
/*

    fun onEndListenerIsSetToRegisterEndOfRun(laneTag: Int){
        val anim = instantiateLaneAnimator()
        anim.doOnEnd { laneModel.registerEndOfLane() }
    }
    private fun laneAnimatorAttributeObjectIsInstantiated(){
        laneAttributes = LaneAnimatorAttributes(laneTag!!)
        laneAttributes.laneDuration = STANDARD_DURATION
        laneAttributes.laneLength = getLengthOfLaneFragment()
        laneAttributes.imagePosition = 0F
        laneAttributes.imageOffset = getImageFrameOffset()
        laneAttributes.orientation = orientation
    }
        fun laneAttributeObjectIsUpdatedToCurrentConditions(){
        laneAttributes.laneDuration = getRemainingLaneDuration()
        laneAttributes.imagePosition = getCurrentImageCoordinates()[0].toFloat()
    }
    fun getCurrentLaneCoordinates():IntArray{
        val location = IntArray(2)
        myLaneFrame.getLocationInWindow(location)
        return location
    }
        fun setStandardDuration(duration:Long) {
        STANDARD_DURATION = duration
    }
        fun listenerIsSetSoThatImageFadesOutAtAEndOfFlipping(flipper:AnimatorSet) {
        if (myLaneAnimator!!.isThereEnoughTimeToFadeAway(flipper.duration)) {
            flipper.doOnEnd { imageRunDownTheLaneIsEnded() }
        }
    }

    fun runDownLaneIsTerminatedAtEndOf(anim:ObjectAnimator){
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                imageRunDownTheLaneIsEnded()
            }
            override fun onAnimationCancel(animation: Animator){}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }
    fun imageIsVisible(vzble:Boolean){
        if(vzble)imageFragmentHandle().imageView.visibility=View.VISIBLE
        else imageFragmentHandle().imageView.visibility=View.INVISIBLE
    }

    fun getCurrentImageCoordinates():IntArray{
        return myImageFragment.getCurrentCoordinatesOnScreen()
    }
    fun getRemainingLaneDuration():Long{
        return myLaneAnimator!!.durationIsUpdatedWithCurrentTime()
    }

    ///////////////////////////////////////////////////////////////////
    fun reverseLaneAnimator():Boolean {
        if (myLaneAnimator == null) return false
        if(myLaneAnimator!!.mlaneAnimator ==null)return false
        if(myLaneAnimator!!.mlaneAnimator!!.isRunning){ myLaneAnimator!!.mlaneAnimator!!.end() }
        val currentPosition = myLaneAnimator!!.mlaneAnimator!!.animatedValue as Float
        myLaneAnimator!!.laneAttr.laneLength = 0F
        myLaneAnimator!!.laneAttr.imagePosition = currentPosition
        instantiateLaneAnimator()
        return true
    }

   fun imageRunDownTheLaneIsEnded(){
        if(myLaneAnimator!=null && myLaneAnimator!!.mlaneAnimator!!.isRunning){
            myLaneAnimator!!.mlaneAnimator!!.end()
        }
    }





 */