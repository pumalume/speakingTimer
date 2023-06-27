package com.ingilizceevi.slider1230
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels

private const val ARG_PARAM1 = "param1"

class LaneController : Fragment() {

    //from activity
    var numOfLanes: Int = 3
    private val laneModel: LaneViewModel by activityViewModels()

    //handles on views and fragments
    //handles on views and fragments
    val tableOfLaneFragmentViews: MutableList<FragmentContainerView> = ArrayList(numOfLanes)
    lateinit var main: View
    lateinit var viewOfTheLanes: LinearLayout
    private val theLanes: MutableList<LaneFragment?> = ArrayList(0)
    lateinit var  myLayoutParams : LinearLayout.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numOfLanes = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        main = inflater.inflate(R.layout.lane_controller, container, false)
        viewOfTheLanes = main.findViewById(R.id.layoutForLaneFragments)
        return main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myLayoutParams = layoutParamsForFullyInflatedLane()
        replaceEachLaneFrameWitheNewInstanceOfLaneFragment()
    }

    fun allTheLaneAnimatorsAreSetUp(){
        for(i in 0 until numOfLanes){
                doOnEndListenerIsSetToRegisterEndOfRunDownLane(i)
            }
    }

    fun allTheLaneAnimatorsAreStarted(){
        for(i in 0 until numOfLanes){
            myLaneAnimatorIsStarted(i)
        }
    }

    fun replaceEachLaneFrameWitheNewInstanceOfLaneFragment() {
        for (i in 0 until numOfLanes) {
            val id = viewOfTheLanes.getChildAt(i).id
            main.findViewById<FrameLayout>(id).layoutParams = myLayoutParams
            childFragmentManager.beginTransaction()
                .replace(viewOfTheLanes.getChildAt(i).id, LaneFragment.newInstance(i), i.toString())
                .commit()
        }
    }

    fun laneHandle(laneTag: Int):LaneFragment {
        val id = viewOfTheLanes.getChildAt(laneTag).id
       return childFragmentManager.findFragmentById(id) as LaneFragment
    }


     fun layoutParamsForFullyInflatedLane(): LinearLayout.LayoutParams {
         //main.width
         val param = LinearLayout.LayoutParams(
             LinearLayout.LayoutParams.MATCH_PARENT, //width
             0,  //height
             1.0f  //weight
         )
         return param
     }

     fun doOnEndListenerIsSetToRegisterEndOfRunDownLane(laneTag: Int){
         val anim = laneHandle(laneTag).instantiateLaneAnimator()
         anim.doOnEnd {
             laneModel.registerEndOfLane()
         }
     }

    fun theImageIsReadyToStartRunDownLane(laneTag:Int){
        val laneHandle=laneHandle(laneTag)
        laneHandle.imageFragmentHandle().imageValuesAreReset()
        laneHandle.imageFragmentHandle().imageView.scaleX = 1f
        laneHandle.imageFragmentHandle().loadImageFromMasterCollection()

    }


     fun myLaneAnimatorIsStarted(laneTag: Int):Boolean {
         doOnEndListenerIsSetToRegisterEndOfRunDownLane(laneTag)
         val laneHandle=laneHandle(laneTag)
         if (laneHandle.myLaneAnimator == null) return false
         if(laneHandle.myLaneAnimator!!.mlaneAnimator==null)return false
         //doOnEndListenerIsSetToRegisterEndOfRunDownLane(laneTag)
         laneHandle.myLaneAnimator!!.mlaneAnimator!!.start()
         return true
     }

    fun enableImageClickedListenerOnAllLanes(){
        for(i in 0 until numOfLanes) laneHandle(i).imageClickedListenerIsSetup()
    }

    fun newIterationOfRunsDownTheLaneIsStarted(){
        for(i in 0 until 3){
            myLaneAnimatorIsStarted(i)
        }
    }

    fun replaceEachImageFragmentWithNewInstanceOfImageFragment(){
        for(i in 0 until numOfLanes){
            laneHandle(i).replaceImageFragmentContainerWithNewInstanceOfImageFragment()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            LaneController().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

}

/*
    fun getLayoutParamsForViewOfEmptyLanes(): LinearLayout.LayoutParams {
        val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, //width
            0,// ,  //height
            0f  //weight
        )
        return param
    }
  fun myLaneAnimatorIsPaused(laneTag: Int):Boolean{
         if (ahandleOnLaneFragment(laneTag).myLaneAnimator == null) return false
         if(ahandleOnLaneFragment(laneTag).myLaneAnimator!!.mlaneAnimator ==null)return false
         if(!ahandleOnLaneFragment(laneTag).myLaneAnimator!!.mlaneAnimator!!.isRunning)return false
         ahandleOnLaneFragment(laneTag).myLaneAnimator!!.mlaneAnimator!!.pause()
         return true
     }
     fun myLaneAnimatorIsReadyToUse(laneTag: Int):Boolean{
         if (ahandleOnLaneFragment(laneTag).myLaneAnimator == null) return false
         if(ahandleOnLaneFragment(laneTag).myLaneAnimator!!.mlaneAnimator ==null)return false
         if(ahandleOnLaneFragment(laneTag).myLaneAnimator!!.mlaneAnimator!!.isRunning)return false
         return true
     }

    fun replaceLaneFrameWithNewInstanceOfLaneFragment(laneTag:Int){
            val id = viewOfTheLanes.getChildAt(laneTag).id
            childFragmentManager.beginTransaction()
                .replace(viewOfTheLanes.getChildAt(laneTag).id, LaneFragment.newInstance(laneTag), laneTag.toString())
                .commit()
        }
     fun laneForNumOfLanesIsAddedToFragmentManager(){
         val transactionManager = childFragmentManager.beginTransaction()
         for(i in 0 until numOfLanes){
             transactionManager.add(viewOfTheLanes.getChildAt(i).id, LaneFragment.newInstance(i), i.toString())
         }
         transactionManager.commit()
     }









     fun imageIsFlipped(laneTag:Int){
         val flipper = ahandleOnLaneFragment(laneTag).myImageFragment.imageIsFlippedAndFaded(false)
         flipper[1].doOnEnd {
             ahandleOnLaneFragment(laneTag).imageRunDownTheLaneIsEnded()
         }
         flipper[0].start()
         flipper[1].start()
     }

 */