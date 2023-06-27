package com.ingilizceevi.studentactivities0104.pickpicture

import android.animation.AnimatorSet
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ingilizceevi.studentactivities0104.StudentInfo
import com.ingilizceevi.vocabularycards0104.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GamePanel : Fragment() {

    private var chapter: String? = null
    lateinit var goSignal:FrameLayout
    private var studentID:String? = null
    var numOfImages = 3
    lateinit var imageControl:ImageController
    lateinit var myTimer: Chronometer
    lateinit var myClicks: TextView
    lateinit var cancelButton : ImageView
    lateinit var main: View
    private val imageViewsPanel: MutableList<ImageView> = ArrayList(0)
    private val gameBrain: LaneViewModel by activityViewModels()
    private var gameFinished = false
    lateinit var playButton: ImageView
    lateinit var mySound:ImageSound


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chapter = it.getString(ARG_PARAM1)
            studentID = it.getString(ARG_PARAM2)
        }
        if(!gameBrain.isGameInitialized()){
            gameBrain.initiateModel(3, chapter!!)
            gameBrain.studentInfo = StudentInfo(studentID!!)
            val tempChapter=chapter!!.substringAfter("chapter")
            gameBrain.studentInfo.chapterNum = tempChapter
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main =  inflater.inflate(R.layout.fragment_game_panel, container, false)
        myTimer = main.findViewById(R.id.myTimer)
        goSignal = main.findViewById(R.id.signalCircle)
        goSignal.setBackgroundResource(R.drawable.red_circle)
        myClicks = main.findViewById(R.id.myClicks)
        cancelButton= main.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener(onCancelClick)
        playButton = main.findViewById(R.id.playButton)
        playButton.setOnClickListener{playTarget()}
        mySound=ImageSound(requireContext())
        //main.setOnClickListener(initiatorClick)
        observerIsEstablishedForStartGame()
        observerIsEstablishedForCancelGame()
        return main
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myDialog = DialogFragment()
        myDialog.show(childFragmentManager, "PressStart")
    }
    val onCancelClick = View.OnClickListener {
        val myDialog = CancelFragment()
        myDialog.show(childFragmentManager, "Cancel")
    }




    private fun startGame(){
        imageControl = childFragmentManager.findFragmentById(R.id.laneControllerView) as ImageController
        imageViewsPanelIsLoaded()
        imageControl.refreshPanelOfImages()
        gameBrain.myTargetConceptIsSetFromIdealMap()
        Handler().postDelayed({
            playTarget()
            myTimer.start()
        }, 500)
    }

    fun imageViewsPanelIsLoaded(){
        for(i in 0 until numOfImages){
            val imageFragment = imageControl.imageFragmentPanel[i]
            imageViewsPanel.add(imageFragment.handleOnImageView())
        }
    }

    fun playTarget(){
        if(gameBrain.targetConceptAsString()!="") {
            val u = gameBrain.getTargetUri()
            val player = mySound.playSound(u)
            player.setOnCompletionListener {
                setOnClickListeners()
            }
            player.start()
        }
    }

    fun setOnClickListeners(){
        for(i in 0 until numOfImages!!){
            imageViewsPanel[i].setOnClickListener(myOnClickListener)
        }
        goSignal.setBackgroundResource(R.drawable.green_circle)
    }
    fun nullifyAllImageViewListeners(){
        for(i in 0 until numOfImages){
            imageViewsPanel[i].setOnClickListener(null)
        }
        goSignal.setBackgroundResource(R.drawable.red_circle)
    }
    fun dealWithTheClicks(){
        gameBrain.increaseClickedCounter()
        val totalClicksSoFar = gameBrain.getTotalClicks().toString()
        myClicks.text = getString(R.string.totalClicks, totalClicksSoFar)

    }
    val myOnClickListener = View.OnClickListener{
        nullifyAllImageViewListeners()
        dealWithTheClicks()
        validateSelection(it.id)
    }

    fun validateSelection(viewId:Int){
        if(gameBrain.isTargetConceptTrue(viewId)){targetConceptIsTrue(viewId)}
        else targetIsFalse(viewId)
    }
    fun targetConceptIsTrue(viewId:Int) {
        if(!gameBrain.schemaForImageClickedTrue(viewId)){
            gameFinished=true
        }
        val bigAnim = imageControl.imageFragmentPanel[viewId].enlargerAnimatorIsSetForView()
        bigAnim.doOnEnd { allViewsAreFadedOut(gameFinished) }
        bigAnim.start()
    }
    fun targetIsFalse(viewId:Int) {
        gameBrain.schemaForImageClickedFalse()
        val shakeAnim = imageControl.imageFragmentPanel[viewId].shakerAnimatorIsSetForView()
        shakeAnim.doOnEnd { allViewsAreFadedOut(false) }
        shakeAnim.start()
    }

    fun setupFadeOutAnimator(finished:Boolean):AnimatorSet{
        val fadeOutControl = AnimatorController(imageControl)
        val fadeOutSet = fadeOutControl.setupFadeOutAnimator()
        fadeOutSet.doOnEnd {
            if(!finished)startAnotherRoundOfFun()
            else concludingFragmentIsLoaded()
        }
        return fadeOutSet
    }
    fun allViewsAreFadedOut(finished:Boolean){
        setupFadeOutAnimator(finished).start()
    }

    fun concludingFragmentIsLoaded(){
        playButton.visibility = View.INVISIBLE
        goSignal.visibility = View.INVISIBLE
        myTimer.stop()
        val myTime = myTimer.text.toString()
        val myClicks = gameBrain.getTotalClicks().toString()

        gameBrain.studentInfo.chapterClick = myClicks.toInt()
        gameBrain.studentInfo.chapterTime = gameBrain.calculateStudentTime(myTime)
        childFragmentManager.beginTransaction()
            .replace(R.id.laneControllerView, ConcludingFragment.newInstance(myTime, myClicks), "Hi")
            .commit()
    }
    fun startAnotherRoundOfFun(){
        imageControl.refreshPanelOfImages()
        playTarget()
    }

    fun finishGame(){
        val intent = Intent()
        intent.putExtra("keyName", "good")
        requireActivity().setResult(RESULT_OK, intent)
        requireActivity().finish()

    }
    fun observerIsEstablishedForCancelGame() {
        val gameIsCancelledObserver = Observer<Boolean> {
            finishGame()
        }
        gameBrain.cancelGameLiveData.observe(viewLifecycleOwner, gameIsCancelledObserver)
    }

    fun observerIsEstablishedForStartGame() {
        val gameIsStartedObserver = Observer<Boolean> { startGame() }
        gameBrain.startGameLiveData.observe(viewLifecycleOwner, gameIsStartedObserver)
    }
    ////////////////////////////////////////////////////
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2:String) =
            GamePanel().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}