package com.ingilizceevi.slider1230

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaneViewModel : ViewModel(){
    var numOfLanes: Int? = null
    lateinit var gameBrain : GameLogic
    var gameLogicIsInitialized = false

    //the game
    var endOfLaneCounter :Int = 0

    fun initiateModel(n:Int, chapter :String){
        numOfLanes = n
        gameBrain = GameLogic(numOfLanes!!, chapter)
        gameBrain.schemaIsInitializeToBeginGame()
        gameLogicIsInitialized=true
    }

    fun getIndexToConcept(laneTag: Int): Int? {
        val index = gameBrain.getIdealLaneValue(laneTag)
        return index
    }

    fun registerEndOfLane(){
        endOfLaneCounter++
        if(endOfLaneCounter==numOfLanes){
            endOfLaneCounter=0
            letTheGameKnowThatAllLanesHaveCompletedTheRun()
        }
    }

    fun letTheGameKnowThatAllLanesHaveCompletedTheRun(){
        allLanesHaveFinishedTheRunLiveData.value = true
    }

    //the following three methods initiate the live data listeners

    val allLanesHaveFinishedTheRunLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val imageIsClickedLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

}