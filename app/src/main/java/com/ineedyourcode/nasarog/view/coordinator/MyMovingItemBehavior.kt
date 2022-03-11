package com.ineedyourcode.nasarog.view.coordinator

import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ineedyourcode.nasarog.R

/**
 * Бихейвор, отвечающий за раскрытие лепестков, скрывающих FAB.
 */
class MyMovingItemBehavior(
    private val bottomBorderY: Float,
    private val itemMovingRange: Float,
    private val fabMovingRange: Float,
    context: Context
) :
    CoordinatorLayout.Behavior<View>(context, null) {

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

        // координаты X центральной точки FAB
        val fabCenterX = fab.x + fab.width / 2
        val fabCenterY = fab.y + fab.height / 2

        // коэффициент смещения FAB относительно заданного диапазона движения FAB по оси Y
        val biasFactorY = kotlin.math.abs((fab.y - bottomBorderY) / fabMovingRange)

        // изменение координат лепестков в зависимости от движения FAB
        when (child.id) {
            R.id.iv_top_left -> {
                child.x = fabCenterX - (itemMovingRange * biasFactorY) - child.width / 2
                child.y = fabCenterY - (itemMovingRange * biasFactorY) - child.height / 2
            }
            R.id.iv_top_right -> {
                child.x = fabCenterX + (itemMovingRange * biasFactorY) - child.width / 2
                child.y = fabCenterY - (itemMovingRange * biasFactorY) - child.height / 2
            }
            R.id.iv_bottom_left -> {
                child.x = fabCenterX - (itemMovingRange * biasFactorY) - child.width / 2
                child.y = fabCenterY + (itemMovingRange * biasFactorY) - child.height / 2
            }
            R.id.iv_bottom_right -> {
                child.x = fabCenterX + (itemMovingRange * biasFactorY) - child.width / 2
                child.y = fabCenterY + (itemMovingRange * biasFactorY) - child.height / 2
            }
        }
        return true
    }
}
