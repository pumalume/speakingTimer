package com.ingilizceevi.slider1230
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

private const val ARG_PARAM1 = "param1"


class ImageFragment : Fragment() {
    private var laneTag: Int? = null
    lateinit var mainView: View
    lateinit var imageView: ImageView
    lateinit var myFrameView:FrameLayout
    lateinit var myImageFrameLayout:FrameLayout
    var myShaker:RotateAnimation? = null
    var myFaderOuter:ObjectAnimator? = null

    private val laneModel: LaneViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            laneTag = it.getInt(ARG_PARAM1)
        }
    }

    fun myImageIsSetTo(drawable:Drawable){
        imageView.setImageDrawable(drawable)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_image, container, false)
        imageView = mainView.findViewById(R.id.imageView)
        myFrameView=mainView.findViewById(R.id.myFrame)
        myImageFrameLayout=mainView.findViewById(R.id.imageFrameLayout)
        if(laneTag!=null)loadImageFromMasterCollection()
        return mainView
    }
    fun loadImageFromMasterCollection() {
        val reference = laneModel.getIndexToConcept(laneTag!!)
        if (reference == null) imageView.setImageResource(R.drawable.logo)
        else {
            val drawable = laneModel.gameBrain.masterCollection[reference!!].myPicture
            imageView.setImageDrawable(drawable)
        }
        laneModel.gameBrain.liveLaneIsSetToMatchIdealLane(laneTag!!)
    }
    fun imageValuesAreReset(){
        imageView.alpha = 1f
        imageView.rotation = 0F
        myFrameView.scaleX = 1.0f
        myFrameView.scaleY = 1.0f

    }
    fun shakeImage(start:Boolean):RotateAnimation{
        //val shaker = ImageAnimator().myShaker()
        myShaker = RotateAnimation(-3.0f,
            3f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f)
        myShaker!!.duration = 500
        myShaker!!.startOffset = 0
        myShaker!!.repeatMode = Animation.REVERSE
        myShaker!!.interpolator = CycleInterpolator(25f)
        if(start)imageView.startAnimation(myShaker)
        return myShaker!!

    }

    fun isFaderOuter(start:Boolean):ObjectAnimator{
        myFaderOuter = ObjectAnimator.ofFloat(imageView, "alpha", 1F, 0F)
        myFaderOuter!!.duration = 400
        if(start)myFaderOuter!!.start()
        return myFaderOuter!!
    }

    fun imageIsBigger(start:Boolean):ScaleAnimation{
        val anim = ScaleAnimation(
                1f, 1.5f, // Start and end values for the X axis scaling
                1f,1.5f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, .5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, .5f) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 600
        if(start){
            myFrameView.startAnimation(anim)
            //imageView.startAnimation(anim)
        }
        return anim
    }
    fun getCurrentCoordinatesOnScreen():IntArray{
        val frame = mainView.findViewById<FrameLayout>(R.id.imageFrameLayout)
        val location = IntArray(2)
        frame.getLocationInWindow(location)
        return location
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}