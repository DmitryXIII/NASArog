package com.ineedyourcode.nasarog.view.ui.coordinator

import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * Бихейвор, отвечающий за вертикальное движение FAB в зависимости от касаний экрана
 */
class MyFabBehavior(
    private val topBorderY: Float,
    private val bottomBorderY: Float,
    context: Context
) :
    CoordinatorLayout.Behavior<View>(context, null) {

    override fun onTouchEvent(parent: CoordinatorLayout, fab: View, touch: MotionEvent): Boolean {
        when (touch.action) {
            MotionEvent.ACTION_MOVE -> {
                when {
                    // если касание внутри заданного диапазона, то кордината Y центра FAB = координате Y касания
                    (touch.y - fab.height / 2) in topBorderY..bottomBorderY -> {
                        fab.y = touch.y - fab.height / 2
                    }

                    // если касание ниже заданного диапазона, то fab.y = координате Y нижней границы
                    (touch.y - fab.height / 2) >= bottomBorderY -> {
                        fab.y = bottomBorderY
                    }

                    // если касание выше заданного диапазона, то fab.y = координате Y верхней границы
                    (touch.y - fab.height / 2) <= topBorderY -> {
                        fab.y = topBorderY
                    }
                }
            }
        }
        return true
    }
}