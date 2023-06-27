package com.ingilizceevi.slider1230

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GamePanel : Fragment() {
 var numOfLanes = 3
    private var chapter: String? = null
    private var param2: String? = null
    private val laneModel: LaneViewModel by activityViewModels()
    lateinit var main: View
    lateinit var myTimer:Chronometer
    lateinit var myClickCounter: TextView
    lateinit var laneController :LaneController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chapter = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        if(!laneModel.gameLogicIsInitialized)laneModel.initiateModel(3, chapter!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        main = inflater.inflate(R.layout.fragment_game_panel, container, false)
        myTimer = main.findViewById<Chronometer>(R.id.myTimer)
        main.setOnClickListener(firstClick)
        val cancel = main.findViewById<ImageView>(R.id.cancelButton)
        myClickCounter = main.findViewById(R.id.myClicks)
        cancel.setOnClickListener {
            cancel.setOnClickListener(secondCancel)
    }
        return main
    }

    val secondCancel = View.OnClickListener { requireActivity().finish() }
    fun myLaneController():LaneController{
        return childFragmentManager.findFragmentById(R.id.laneControllerView) as LaneController
    }
    fun playIt() {
        val t = Thread { playTarget() }
        t.start()
    }
    private val firstClick = View.OnClickListener {
        laneController = myLaneController()
        it.setOnClickListener(secondClick)
    }

    private val secondClick = View.OnClickListener{
        it.setOnClickListener(thirdClick)
    }

    private val thirdClick = View.OnClickListener{
        observerIsEstablishedForEndOfRunDownTheLane()
        observerIsEstablishedForImageClicked()
        laneController.allTheLaneAnimatorsAreStarted()
        playIt()
        myTimer.start()
        it.setOnClickListener(null)
    }


    fun observerIsEstablishedForEndOfRunDownTheLane(){
        val endObserver = Observer<Boolean> {
                b -> laneDriver()
        }
        laneModel.allLanesHaveFinishedTheRunLiveData.observe(viewLifecycleOwner, endObserver)
    }
    fun unclickedImagesAreFadedOut(laneTag:Int){
        for(i in 0 until numOfLanes) {
            if (i != laneTag) {
                val handle = laneController.laneHandle(i)
                val fade = handle.imageFragmentHandle().isFaderOuter(false)
                val laneAnim = handle.myLaneAnimator!!
                if (laneAnim.isThereEnoughTimeToFadeAway(fade.duration)) {
                    fade.doOnEnd { laneAnim.mlaneAnimator!!.end() }
                }
                fade.start()
            }
        }
    }
    fun clickedImageIsTrue(laneTag:Int){
        laneModel.gameBrain.schemaForImageClickedTrue(laneTag)
        for(i in 0 until numOfLanes) {
            if (i == laneTag) laneController.laneHandle(i).imageIsBigger()
        }
        unclickedImagesAreFadedOut(laneTag)
    }

    fun clickedImageIsFalse(laneTag:Int){
        laneModel.gameBrain.schemaForImageClickedFalse(laneTag)
        for(i in 0 until numOfLanes) {
            if (i == laneTag) laneController.laneHandle(i).imageIsShaken()
        }
        unclickedImagesAreFadedOut(laneTag)
    }

    fun observerIsEstablishedForImageClicked() {
        val imageIsClickedObserver = Observer<Int> { laneTag ->
            if (laneModel.gameBrain.isTargetTrue(laneTag)) clickedImageIsTrue(laneTag)
            else clickedImageIsFalse(laneTag)
        }
        laneModel.imageIsClickedLiveData.observe(viewLifecycleOwner, imageIsClickedObserver)
    }

    fun checkGameStatus():Boolean{
        for(i in 0 until numOfLanes){
            if(laneModel.gameBrain.getIdealLaneValue(i)!=null){
                return true
            }
        }
        return false
    }

    fun playTarget(){
        if(laneModel.gameBrain.targetAsString()!="") {
            val u = laneModel.gameBrain.getTargetUri()
            val mySound = ImageSound(requireContext())
            mySound.playSound(u)
        }
    }

    fun laneDriver(){
        val finished = checkGameStatus()
        if(!finished) replaceTheGamePanelWithTotals()
        else {
            val c = laneModel.gameBrain.myClickedCounter
            myClickCounter.text = c.toString()
            playTarget()
            laneModel.gameBrain.idealSchemaIsRecycled()
            laneController.replaceEachImageFragmentWithNewInstanceOfImageFragment()
            laneController.newIterationOfRunsDownTheLaneIsStarted()
            laneController.enableImageClickedListenerOnAllLanes()
        }
       // else controller.endAllLanes()
    }

    fun replaceTheGamePanelWithTotals(){
        myTimer.stop()
        val myTimerTotal = myTimer.text.toString()
        val t = "Total Clicked: " + laneModel.gameBrain.myClickedCounter
      childFragmentManager.beginTransaction()
          .replace(R.id.laneControllerView, TotalsFragment.newInstance(myTimerTotal, t), "totals")
          .commit()
    }
    fun controllerHandle(): LaneController {
        return childFragmentManager.findFragmentById(R.id.laneControllerView) as LaneController
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GamePanel().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
