package com.ineedyourcode.nasarog.view.coordinator

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class MyTouchBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun onTouchEvent(parent: CoordinatorLayout, v: View, ev: MotionEvent): Boolean {
        val coeff = ev.rawY - ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("TAG_EXAMPLE_MOVE", "coeff = " + coeff)
                Log.d("TAG_EXAMPLE_MOVE", "v.x(before) = " + (v.x + v.width/2))
                Log.d("TAG_EXAMPLE_MOVE", "v.y(before) = " + (v.y + v.height/2))
                Log.d("TAG_EXAMPLE_MOVE", "ev.translationY = " + ev.yPrecision)
//                v.x = ev.rawX - v.width/2
                if ((ev.rawY - v.height/2 - coeff) in 500f..1304f) {
//                    v.x = ev.rawX - v.width/2
                    v.y = ev.rawY - v.height/2 - coeff
                } else if ((ev.rawY - v.height/2 - coeff) > 1304f) {
                    v.y = 1304f
                } else {
                    v.y = 500f
                }
                Log.d("TAG_EXAMPLE_MOVE", "v.x = " + (v.x + v.width/2))
                Log.d("TAG_EXAMPLE_MOVE", "v.y = " + (v.y + v.height/2))
                Log.d("TAG_EXAMPLE_MOVE", "ev.x = " + ev.x)
                Log.d("TAG_EXAMPLE_MOVE", "ev.y = " + (ev.rawY - coeff))
                Log.d("TAG_EXAMPLE_MOVE", "=============================================================")
            }
            MotionEvent.ACTION_MOVE -> {
                if ((ev.rawY - v.height/2 - coeff) in 500f..1304f) {
//                    v.x = ev.rawX - v.width/2
                    v.y = ev.rawY - v.height/2 - coeff
                } else if ((ev.rawY - v.height/2 - coeff) > 1304f) {
                    v.y = 1304f
                } else {
                    v.y = 500f
                }
//
//                    Log.d(
//                        "TAG_EXAMPLE_MOVE",
//                        "++++++++++++++++++++++++++++++++++++++++++++++++++++++"
//                    )
//                    Log.d("TAG_EXAMPLE_MOVE", "v.wid = " + v.width)
//                    Log.d("TAG_EXAMPLE_MOVE", "v.hei = " + v.height)
//                    Log.d("TAG_EXAMPLE_MOVE", "ev.x = " + event.x)
//                    Log.d("TAG_EXAMPLE_MOVE", "ev.y = " + event.y)
//                    Log.d("TAG_EXAMPLE_MOVE", "ev.rawX = " + event.rawX)
//                    Log.d("TAG_EXAMPLE_MOVE", "ev.rawY = " + event.rawY)
//                    if (event.y > 500f && event.y < 1000f) {
//                        v.y = event.rawY - v.y
//                        Log.d("TAG_EXAMPLE_MOVE", "v.x = " + v.x)
//                        Log.d("TAG_EXAMPLE_MOVE", "v.y = " + v.y)
            }
            else -> {}
        }
        return true
    }
}