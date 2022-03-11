package com.ineedyourcode.nasarog.view.coordinator

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

    override fun onTouchEvent(parent: CoordinatorLayout, v: View, ev: MotionEvent): Boolean {

        // поправка в координату Y касания (возникает из-за разных начал отсчета по оси Y)
        val deltaTouchY = ev.rawY - ev.y

        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                when {
                    // если касание внутри заданного диапазона, то кордината Y центра FAB = координате Y касания
                    (ev.rawY - v.height / 2 - deltaTouchY) in topBorderY..bottomBorderY -> {
                        v.y = ev.rawY - v.height / 2 - deltaTouchY
                    }

                    // если касание ниже заданного диапазона, то кордината Y центра FAB = координате Y нижней границы
                    (ev.rawY - v.height / 2 - deltaTouchY) > topBorderY -> {
                        v.y = bottomBorderY
                    }

                    // если касание выше заданного диапазона, то кордината Y центра FAB = координате Y верхней границы
                    else -> {
                        v.y = topBorderY
                    }
                }
            }
        }
        return true
    }
}