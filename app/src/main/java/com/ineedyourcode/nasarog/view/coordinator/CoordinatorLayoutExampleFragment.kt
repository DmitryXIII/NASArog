package com.ineedyourcode.nasarog.view.coordinator

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.ineedyourcode.nasarog.databinding.FragmentCoordinatorLayoutExampleBinding
import com.ineedyourcode.nasarog.view.BaseBindingFragment


class CoordinatorLayoutExampleFragment :
    BaseBindingFragment<FragmentCoordinatorLayoutExampleBinding>(
        FragmentCoordinatorLayoutExampleBinding::inflate
    ) {
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vTop.y = 500f
        binding.vBottom.y = 1500f
        binding.fab.y = 1304f
////        Log.d("TAG_EXAMPLE_MOVE", "v.y = " + binding.fab.y)
////        Log.d("TAG_EXAMPLE_MOVE", "v.transY = " + binding.fab.left)
//        binding.fab.setOnTouchListener(OnTouchListener { v, event ->
////            when (event.action) {
////                MotionEvent.ACTION_DOWN -> {
////                    Log.d("TAG_EXAMPLE_MOVE", "v.wid = " + v.width)
////                    Log.d("TAG_EXAMPLE_MOVE", "v.hei = " + v.height)
////                    Log.d("TAG_EXAMPLE_MOVE", "v.x = " + v.x)
////                    Log.d("TAG_EXAMPLE_MOVE", "v.y = " + v.y)
////                    Log.d("TAG_EXAMPLE_MOVE", "v.left = " + v.left)
////                    Log.d("TAG_EXAMPLE_MOVE", "v.right = " + v.right)
////                    Log.d("TAG_EXAMPLE_MOVE", "v.top = " + v.top)
////                    Log.d("TAG_EXAMPLE_MOVE", "v.bottom = " + v.bottom)
////                    Log.d("TAG_EXAMPLE_MOVE", "ev.x = " + event.x)
////                    Log.d("TAG_EXAMPLE_MOVE", "ev.y = " + event.y)
////                    Log.d("TAG_EXAMPLE_MOVE", "ev.rawX = " + event.rawX)
////                    Log.d("TAG_EXAMPLE_MOVE", "ev.rawY = " + event.rawY)
////                    Log.d("TAG_EXAMPLE_MOVE", "=============================================================")
////                }
////                MotionEvent.ACTION_MOVE -> {
//////
//////                    Log.d(
//////                        "TAG_EXAMPLE_MOVE",
//////                        "++++++++++++++++++++++++++++++++++++++++++++++++++++++"
//////                    )
//////                    Log.d("TAG_EXAMPLE_MOVE", "v.wid = " + v.width)
//////                    Log.d("TAG_EXAMPLE_MOVE", "v.hei = " + v.height)
//////                    Log.d("TAG_EXAMPLE_MOVE", "ev.x = " + event.x)
//////                    Log.d("TAG_EXAMPLE_MOVE", "ev.y = " + event.y)
//////                    Log.d("TAG_EXAMPLE_MOVE", "ev.rawX = " + event.rawX)
//////                    Log.d("TAG_EXAMPLE_MOVE", "ev.rawY = " + event.rawY)
//////                    if (event.y > 500f && event.y < 1000f) {
//////                        v.y = event.rawY - v.y
//////                        Log.d("TAG_EXAMPLE_MOVE", "v.x = " + v.x)
//////                        Log.d("TAG_EXAMPLE_MOVE", "v.y = " + v.y)
////                }
////                else -> {}
////            }
////            false
////        })
//    }
//    }
    }
}