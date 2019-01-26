package com.prasanna.spinningdate.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.prasanna.spinningdate.R
import com.prasanna.spinningdate.utils.AppConstants
import com.prasanna.spinningdate.utils.AppUtils
import com.prasanna.spinningdate.utils.NetworkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val mGrayAreaExpandedHeight = 250f
    private val mGrayAreaHeight = 40f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        setOnTouchListenerForSpinningTextView()
        executeTimeServiceForEveryMilliSecond()
    }

    private fun executeTimeServiceForEveryMilliSecond() {
        //this could be replaced with Work Manager & then we can MVVM if required
        val executor = Executors.newScheduledThreadPool(1)
        executor.scheduleAtFixedRate(this::setTime, 0, 1, TimeUnit.MILLISECONDS)
    }

    private fun setTime() {
        val data = NetworkManager.doGetRequest()
        runOnUiThread { tvSpinningSquareTime.text = data }
    }

    private fun setOnTouchListenerForSpinningTextView() {
        tvSpinningSquareTime.onDrag { event, expandGrayArea ->
            setGrayAreaHeight()
            if (event?.action == MotionEvent.ACTION_DOWN) {
                resizeGrayArea(vGrayArea, mGrayAreaExpandedHeight)
            } else if (event?.action == MotionEvent.ACTION_UP && !expandGrayArea) {
                resizeGrayArea(vGrayArea, mGrayAreaHeight)
            }
        }
    }

    private fun resizeGrayArea(view: View, grayAreaHeight: Float) {
        val anim = ValueAnimator.ofInt(view.measuredHeight, AppUtils.getGrayAreaHeight(this, grayAreaHeight).toInt())
        anim.addUpdateListener { valueAnimator ->
            val height = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = height
            view.layoutParams = layoutParams
        }
        anim.duration = AppConstants.TOUCH_EVENT_ANIM_DURATION.toLong()
        anim.start()
    }

    private fun setGrayAreaHeight() {
        tvSpinningSquareTime.deviceHeight = AppUtils.getDeviceHeight(this).toFloat() - AppUtils.getStatusBarHeight(this)
        tvSpinningSquareTime.deviceWidth = AppUtils.getDeviceWidth(this).toFloat()

        tvSpinningSquareTime.grayAreaTotalHeight = AppUtils.getGrayAreaHeight(this, mGrayAreaExpandedHeight)
        tvSpinningSquareTime.grayAreaHeight = vGrayArea.measuredHeight.toFloat()
    }

}
