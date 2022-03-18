package com.ineedyourcode.nasarog.view.coordinator

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.ineedyourcode.nasarog.databinding.FragmentCoordinatorLayoutExampleBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

/**
 * Фрагмент с использованием кастомных бихейворов.
 * Делать бихейворы для работы с тулбаром не хотелось - слишком много информации и примеров на эту тему,
 * решил сделать что-то чисто свое.
 * Используется 2 кастомных бихейвора, реагируют на вертикальный свайп по экрану (черная полоса со стрелками - просто для наглядности).
 * Потрачено 3 кг нервов и 100500 часов, чтобы разобраться с системой координат,
 * но зато теперь имеется хоть какое-то представление о работе бихейворов.
 * Адаптации под разные экраны пока нет - задача себе на будущее.
 */

private const val TOP_Y_BORDER = 500f
private const val BOTTOM_Y_BORDER = 1300f
private const val ITEM_MOVING_RANGE = 200f
private const val FAB_MOVING_RANGE = BOTTOM_Y_BORDER - TOP_Y_BORDER

class CoordinatorLayoutExampleFragment :
    BaseFragment<FragmentCoordinatorLayoutExampleBinding>(
        FragmentCoordinatorLayoutExampleBinding::inflate
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            fab.apply {
                y = BOTTOM_Y_BORDER
                (layoutParams as CoordinatorLayout.LayoutParams).behavior =
                    MyFabBehavior(TOP_Y_BORDER, BOTTOM_Y_BORDER, requireContext())
            }

            for (imageView in listOf(ivTopLeft, ivTopRight, ivBottomRight, ivBottomLeft)) {
                (imageView.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                    MyMovingItemBehavior(BOTTOM_Y_BORDER, ITEM_MOVING_RANGE, FAB_MOVING_RANGE, requireContext())
            }
        }
    }
}