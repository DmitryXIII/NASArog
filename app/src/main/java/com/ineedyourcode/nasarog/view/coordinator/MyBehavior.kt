package com.ineedyourcode.nasarog.view.coordinator

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ineedyourcode.nasarog.R

class MyBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ) = dependency is FloatingActionButton

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val fab = dependency as FloatingActionButton
        val centerX = fab.x + fab.width / 2
        val centerY = fab.y + fab.height / 2

        Log.d("TAGTAGTAG", "fab.x - " + fab.x.toString())
        Log.d("TAGTAGTAG", "fab.y - " + fab.y.toString())
        Log.d("TAGTAGTAG", "fab.top - " + fab.top.toString())
        Log.d("TAGTAGTAG", "fab.bottom - " + fab.bottom.toString())
        Log.d("TAGTAGTAG", "===========================================================")
        Log.d("TAGTAGTAG", "fab.centerX - " + (fab.x + 98f).toString())
        Log.d("TAGTAGTAG", "fab.centerY - " + (fab.y - 98f).toString())
//        Log.d("TAGTAGTAG", "fab.centerY - " + fab.y.toString())

        val movingRange = 200f

        val biasFactorY = kotlin.math.abs((fab.y - 1304f) / 804f)

        when (child.id) {
            R.id.iv_top_left -> {
                child.x = centerX - (movingRange * biasFactorY) - child.width / 2
                child.y = centerY - (movingRange * biasFactorY) - child.height / 2
            }
            R.id.iv_top_right -> {
                child.x = centerX + (movingRange * biasFactorY) - child.width / 2
                child.y = centerY - (movingRange * biasFactorY) - child.height / 2
            }
            R.id.iv_bottom_left -> {
                child.x = centerX - (movingRange * biasFactorY) - child.width / 2
                child.y = centerY + (movingRange * biasFactorY) - child.height / 2
            }
            R.id.iv_bottom_right -> {
                child.x = centerX + (movingRange * biasFactorY) - child.width / 2
                child.y = centerY + (movingRange * biasFactorY) - child.height / 2
            }
        }
        return true
    }
}
