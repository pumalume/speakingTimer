package com.ingilizceevi.picapic0228

import android.net.Uri
import androidx.collection.arrayMapOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaneViewModel : ViewModel(){
    var numOfImages: Int = 0
    var gameBrainIsInitialized = false
    lateinit var masterCollection: MutableList<ConceptualObject>
    private var myTargetConcept : Int = -1
    private val availableConcepts: MutableList<Int> = ArrayList(0)
    private val pileOfDiscardedConcepts:MutableList<Int> = ArrayList(0)
    private val idealConceptualMap: MutableMap<Int, Int> = arrayMapOf()
    private var clickedCounter = 0

    fun initiateModel(numOfLanes:Int, chapter:String){
        this.numOfImages = numOfLanes
        masterCollection = FileLoader(chapter).fillMyArray()
        availableConceptsAreGenerated()
        conceptualMapsAreInitialized()
        schemaIsInitializeToBeginGame()
        gameBrainIsInitialized=true
    }


    fun getTargetUri(): Uri {
        return masterCollection[myTargetConcept].myUri
    }
    private fun availableConceptsAreGenerated() {
        val size = masterCollection.size
        for(i in 0 until size){
            availableConcepts.add(i)
        }
        availableConcepts.shuffle()
    }
    private fun conceptualMapsAreInitialized(){
        for(i in 0 until numOfImages){
            idealConceptualMap[i] = -1
        }
    }

    private fun conceptIsPulledFromAvailableConcepts():Int{
        return if(availableConcepts.isNotEmpty()) availableConcepts.removeAt(0)
        else -1
    }
    private fun conceptIsRegisteredToIdealMapFromAvailableList(laneTag:Int):Boolean{
        idealConceptualMap[laneTag]=conceptIsPulledFromAvailableConcepts()
        return idealConceptualMap[laneTag] != -1
    }

    private fun conceptIsThrownToDiscardPile(laneTag:Int) {
        if (idealConceptualMap[laneTag] != null) {
            pileOfDiscardedConcepts.add(idealConceptualMap[laneTag]!!)
            idealConceptualMap[laneTag] = -1
        }
    }

    fun schemaIsInitializeToBeginGame():Boolean {
        for (i in 0 until numOfImages) {
            if(!conceptIsRegisteredToIdealMapFromAvailableList(i))return false
        }
        return true
    }

    fun idealSchemaIsRecycled(){
        val tempList :MutableList<Int> = ArrayList(0)
        for(i in 0 until numOfImages) tempList.add(idealConceptualMap[i]!!)
        tempList.shuffle()
        for(i in 0 until numOfImages) idealConceptualMap[i] = tempList[i]
    }

    private fun conceptIsReturnedToAvailableList(viewId:Int){
        val concept = idealConceptualMap[viewId]
        if (concept != -1) availableConcepts.add(concept!!)
        availableConcepts.shuffle()
    }

    fun allImagesAreFinished():Boolean{
        for(i in 0 until numOfImages){
            if(idealConceptualMap[i]!=-1)return false
        }
        return true
    }
    fun schemaForImageClickedTrue(viewId:Int):Boolean{
        for(index in 0 until numOfImages) {
            if(index == viewId){
                conceptIsThrownToDiscardPile(index)
                conceptIsRegisteredToIdealMapFromAvailableList(index)
            }
            else{
                conceptIsReturnedToAvailableList(index)
                conceptIsRegisteredToIdealMapFromAvailableList(index)
            }
        }
        if(allImagesAreFinished())return false
        myTargetConceptIsSetFromIdealMap()
        return true
    }

    fun schemaForImageClickedFalse(){
        for(index in 0 until numOfImages){
            val tempConcept = idealConceptualMap[index]
            if(tempConcept!=myTargetConcept) {
                conceptIsReturnedToAvailableList(index)
                conceptIsRegisteredToIdealMapFromAvailableList(index)
            }
        }
        idealSchemaIsRecycled()
    }
    fun targetConceptAsString():String{
        if(myTargetConcept!=null) return masterCollection[myTargetConcept!!].myString
        return ""
    }

    fun getIdealLaneValue(laneTag:Int):Int?{
        return idealConceptualMap[laneTag]
    }
    fun isTargetConceptTrue(laneTag: Int):Boolean{
        return idealConceptualMap[laneTag]==myTargetConcept
    }

    fun getRandomConceptFromDiscardedPile():Int{
        val size = pileOfDiscardedConcepts.size
        return pileOfDiscardedConcepts[(0 until size).random()]
    }
    fun myTargetConceptIsSetFromIdealMap(){
        do{
            myTargetConcept = idealConceptualMap[(0 until numOfImages).random()]!!
        } while(myTargetConcept<0)
    }

    private fun pictureIsPulledFromDiscardedPile():Int{
        if (pileOfDiscardedConcepts.isEmpty())return -1
        val size = pileOfDiscardedConcepts.size
        return pileOfDiscardedConcepts[(0..size).random()]
    }

    fun getTotalClicks():Int{return clickedCounter}
    fun increaseClickedCounter(){
        clickedCounter++
    }
    //the following three methods initiate the live data listeners
    val imageIsClickedLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

}