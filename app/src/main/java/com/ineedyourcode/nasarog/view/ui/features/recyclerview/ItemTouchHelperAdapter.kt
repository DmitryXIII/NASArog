package com.ineedyourcode.nasarog.view.ui.features.recyclerview

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}