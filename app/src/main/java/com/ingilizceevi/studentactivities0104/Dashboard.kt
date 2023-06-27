package com.ingilizceevi.studentactivities0104

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ingilizceevi.vocabularycards0104.R

class Dashboard: Fragment() {
    lateinit var startButtonView: Button
    lateinit var stopButtonView: Button
    lateinit var timeDisplay: Chronometer
    lateinit var nameDisplay: TextView
    lateinit var main: View
    private val gameBrain: gameModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        main = inflater.inflate(R.layout.fragment_dashboard, container, false)
        nameDisplay = main.findViewById(R.id.nameView)
        startButtonView = main.findViewById(R.id.start_button)
        stopButtonView = main.findViewById(R.id.finish_button)
        //startTimerButton = main.findViewById(R.id.start_button)
        return main
    }


    override fun onResume() {
        super.onResume()
        timeDisplay = main.findViewById(R.id.timerView)
        //startButtonView = main.findViewById(R.id.start_button)
        //stopButtonView = main.findViewById(R.id.finish_button)

    }
    fun updateName(name: String) {
        nameDisplay.text = name
    }

    fun stopTime(){
        timeDisplay.stop()
        gameBrain.calculateTimeToSeconds(timeDisplay.text.toString())
    }
    fun stopButton(): Button {
        stopTime()
        stopButtonView.text = "EXIT"
        return stopButtonView
    }

    fun thisIsTheStopButton():Button{
        return stopButtonView
    }
    fun thisIsTheStartButton():Button{
        return startButtonView
    }
    fun startButton() {
        timeDisplay.setBase(SystemClock.elapsedRealtime());
        timeDisplay.stop()
        timeDisplay.start()
        startButtonView.visibility = View.GONE
    }
}