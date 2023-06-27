package com.ingilizceevi.slider1230

import android.net.Uri
import androidx.collection.arrayMapOf

class GameLogic(numlanes:Int, chapter:String) {

    lateinit var masterCollection: MutableList<ConceptualObject>
    val myConceptTracker:MutableMap<Int, Int> = arrayMapOf()
    //    var masterListOfDrawablesObjects : MutableList<DrawableObject>
    //    var myFile: FileLoader = FileLoader("chapter03")
    private var numOfLanes:Int = numlanes
    private val chapter = chapter
    private var myTarget : Int? = 0
    private val availableConcepts: MutableList<Int> = ArrayList(0)
    private val pileOfDiscardedConcepts:MutableList<Int> = ArrayList(0)
    private var liveConceptualMap: MutableList<Int?> = ArrayList(0)
    private val idealConceptualMap: MutableMap<Int, Int?> = arrayMapOf()
    var myClickedCounter:Int = 0
    val repetitionLimit=3

    init {
        masterCollection = FileLoader(chapter).fillMyArray()
        gameIsInitialized()
    }

    private fun gameIsInitialized(){
        availableConceptsAreGenerated()
        conceptualMapsAreInitialized()
    }

    fun getTargetUri():Uri{
        return masterCollection[myTarget!!].myUri
    }
    private fun availableConceptsAreGenerated() {
        val size = masterCollection.size
        for(i in 0 until size){
            availableConcepts.add(i)
            myConceptTracker[i]=0
        }
    }
    private fun conceptualMapsAreInitialized(){
        for(i in 0 until numOfLanes){
            liveConceptualMap.add(null)
            idealConceptualMap[i] = null
        }
    }

    private fun conceptIsPulledFromAvailableConcepts():Int?{
        return if(availableConcepts.isNotEmpty()) availableConcepts.removeAt(0)
        else null
    }
    private fun conceptIsRegisteredToIdealMapFromAvailableList(laneTag:Int):Boolean{
        idealConceptualMap[laneTag]=conceptIsPulledFromAvailableConcepts()
        if(idealConceptualMap[laneTag] != null){
            val index=idealConceptualMap[laneTag]
            var temp = myConceptTracker[index]!!
            myConceptTracker[index!!]=++temp
        }
        return idealConceptualMap[laneTag] != null
    }

    private fun conceptIsThrownToDiscardPile(laneTag:Int){
        if(idealConceptualMap[laneTag]!=null&&myConceptTracker[laneTag]==repetitionLimit) {
            pileOfDiscardedConcepts.add(idealConceptualMap[laneTag]!!)
        }
        else conceptIsReturnedToAvailableList(laneTag)
        idealConceptualMap[laneTag]=null
    }
    fun liveLaneIsSetToMatchIdealLane(laneTag : Int){
        liveConceptualMap[laneTag] = idealConceptualMap[laneTag]
    }

    fun schemaIsInitializeToBeginGame():Boolean {
        for (i in 0 until numOfLanes) {
           if(!conceptIsRegisteredToIdealMapFromAvailableList(i))return false
        }
        return true
    }
    fun incrementClickedCounter(){
        myClickedCounter++
    }
    fun idealSchemaIsRecycled(){
        val tempList :MutableList<Int?> = ArrayList(0)
        for(i in 0 until numOfLanes) tempList.add(idealConceptualMap[i])
        tempList.shuffle()
        for(i in 0 until numOfLanes) idealConceptualMap[i] = tempList[i]
    }

    private fun pictureIsPulledFromDiscardedPile():Int{
        if (pileOfDiscardedConcepts.isEmpty())return -1
        val size = pileOfDiscardedConcepts.size
        return pileOfDiscardedConcepts[(0..size).random()]
    }

   private fun conceptIsReturnedToAvailableList(laneTag:Int){
       val concept = idealConceptualMap[laneTag]
       if(concept!=null) availableConcepts.add(concept)
   }

    fun schemaForImageClickedTrue(laneTag:Int) {
        conceptIsThrownToDiscardPile(laneTag)
        conceptIsRegisteredToIdealMapFromAvailableList(laneTag)
        //newConceptIsAppliedToIdealLane(laneTag)
        idealSchemaIsRecycled()
        if(checkIfLanesAreEmpty()){
            while(!myTargetIsSetFromIdealMap()){}
        }
    }

    fun checkIfLanesAreEmpty():Boolean{
        for(i in 0 until numOfLanes){
            if(idealConceptualMap[i]!=null)return true
        }
        return false
    }
    fun schemaForImageClickedFalse(laneTag:Int){
        conceptIsReturnedToAvailableList(laneTag)
        conceptIsRegisteredToIdealMapFromAvailableList(laneTag)
        //newConceptIsAppliedToIdealLane(laneTag)
        idealSchemaIsRecycled()
    }
    fun targetAsString():String{
        if(myTarget!=null) return masterCollection[myTarget!!].myString
        return ""
    }

    fun getIdealLaneValue(laneTag:Int):Int?{
        return idealConceptualMap[laneTag]
    }
    fun isTargetTrue(laneTag: Int):Boolean{
        return idealConceptualMap[laneTag]==myTarget
    }

    private fun myTargetIsSetFromIdealMap():Boolean{
        val randomIndex = (0 until numOfLanes).random()
        myTarget = idealConceptualMap[randomIndex]
        return myTarget != null
    }
}


