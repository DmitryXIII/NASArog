package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.core.view.MotionEventCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewHazardousAsteroidItemBinding
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewHeaderItemBinding
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewUnhazardousAsteroidItemBinding
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.utils.ITEM_TYPE_HAZARDOUS
import com.ineedyourcode.nasarog.utils.ITEM_TYPE_UNHAZARDOUS
import com.ineedyourcode.nasarog.utils.convertNasaDateFormatToMyFormat
import com.ineedyourcode.nasarog.utils.getCurrentDate
import java.util.*
import kotlin.random.Random

class RecyclerViewFragmentAdapter(val onClickListener: OnAsteroidItemClickListener, val onStartDragListener: OnStartDragListener) :
    RecyclerView.Adapter<RecyclerViewFragmentAdapter.BaseAsteroidViewHolder>(),
    ItemTouchHelperAdapter {
    private var asteroidList = mutableListOf<Pair<AsteroidListDto.AsteroidDto, Boolean>>()
    private var tempAsteroidList = mutableListOf<Pair<AsteroidListDto.AsteroidDto, Boolean>>()

    fun setData(mAsteroidList: List<AsteroidListDto.AsteroidDto>) {
        for (asteroid in mAsteroidList) {
            asteroidList.add(Pair(asteroid, false))
        }
        tempAsteroidList = asteroidList
    }

    fun searchFilter(filteredList: List<AsteroidListDto.AsteroidDto>) {
        if (filteredList.isEmpty()) {
            asteroidList = tempAsteroidList
            notifyDataSetChanged()
        } else {
            asteroidList.clear()
            notifyDataSetChanged()
            for (asteroid in filteredList) {
                asteroidList.add(Pair(asteroid, false))
                notifyItemInserted(asteroidList.size - 1)
            }
        }
    }

    fun appendItem() {
        asteroidList.add(Pair(generateAsteroidItem(), false))
        notifyItemInserted(asteroidList.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return asteroidList[position].first.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAsteroidViewHolder {
        return when (viewType) {
            ITEM_TYPE_HAZARDOUS -> {
                val binding = FragmentFeaturesRecyclerViewHazardousAsteroidItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HazardousAsteroidViewHolder(binding.root)
            }
            ITEM_TYPE_UNHAZARDOUS -> {
                val binding = FragmentFeaturesRecyclerViewUnhazardousAsteroidItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                UnhazardousAsteroidViewHolder(binding.root)
            }
            else -> {
                val binding = FragmentFeaturesRecyclerViewHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeaderViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseAsteroidViewHolder, position: Int) {
        holder.bind(Pair(asteroidList[position].first, false))
    }

    override fun getItemCount() = asteroidList.size

    inner class UnhazardousAsteroidViewHolder(view: View) : BaseAsteroidViewHolder(view),
        ItemTouchHelperViewHolder {
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(asteroid: Pair<AsteroidListDto.AsteroidDto, Boolean>) {
            FragmentFeaturesRecyclerViewUnhazardousAsteroidItemBinding.bind(itemView).apply {

                tvAsteroidName.text = asteroid.first.name
                tvAsteroidMagnitudeH.text = asteroid.first.absoluteMagnitudeH.toString()
                tvCloseApproachDate.text =
                    convertNasaDateFormatToMyFormat(asteroid.first.closeApproachData.first().closeApproachDate)

                checkItemMenuPanel(
                    asteroidList[layoutPosition].second,
                    groupDetails,
                    groupMenuPanel
                )

                ivItemIcon.setOnClickListener {
                    onClickListener.onAsteroidItemClick(asteroid.first)
                }

                ivAddItem.setOnClickListener {
                    asteroidList.add(layoutPosition, Pair(generateAsteroidItem(), false))
                    notifyItemInserted(layoutPosition)
                }

                ivDeleteItem.setOnClickListener {
                    asteroidList.removeAt(layoutPosition)
                    notifyItemRemoved(layoutPosition)
                }

                ivMenuPanelBackArrow.setOnClickListener {
                    asteroidList[layoutPosition] = asteroidList[layoutPosition].first to false
                    notifyItemChanged(layoutPosition)
                }

                ivMenuPanelOpenArrow.setOnClickListener {
                    asteroidList[layoutPosition] = asteroidList[layoutPosition].first to true
                    notifyItemChanged(layoutPosition)
                }

                ivMoveItemUp.setOnClickListener {
                    if (layoutPosition > 1) {
                        asteroidList.removeAt(layoutPosition).apply {
                            asteroidList.add(layoutPosition - 1, this)
                        }
                        notifyItemMoved(layoutPosition, layoutPosition - 1)
                    }
                }

                ivMoveItemDown.setOnClickListener {
                    if (layoutPosition < asteroidList.size - 1) {
                        asteroidList.removeAt(layoutPosition).apply {
                            asteroidList.add(layoutPosition + 1, this)
                        }
                        notifyItemMoved(layoutPosition, layoutPosition + 1)
                    }
                }

                ivMoveItem.setOnTouchListener { _, event ->
                    if(MotionEventCompat.getActionMasked(event)== MotionEvent.ACTION_DOWN){
                        onStartDragListener.onStartDrag(this@UnhazardousAsteroidViewHolder)
                    }
                    false
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.WHITE)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    inner class HazardousAsteroidViewHolder(view: View) : BaseAsteroidViewHolder(view), ItemTouchHelperViewHolder {
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(asteroid: Pair<AsteroidListDto.AsteroidDto, Boolean>) {
            FragmentFeaturesRecyclerViewHazardousAsteroidItemBinding.bind(itemView).apply {

                tvAsteroidName.text = asteroid.first.name
                tvAsteroidMagnitudeH.text = asteroid.first.absoluteMagnitudeH.toString()
                tvCloseApproachDate.text =
                    convertNasaDateFormatToMyFormat(asteroid.first.closeApproachData.first().closeApproachDate)

                checkItemMenuPanel(
                    asteroidList[layoutPosition].second,
                    groupDetails,
                    groupMenuPanel
                )

                ivItemIcon.setOnClickListener {
                    onClickListener.onAsteroidItemClick(asteroid.first)
                }

                ivAddItem.setOnClickListener {
                    asteroidList.add(layoutPosition, Pair(generateAsteroidItem(), false))
                    notifyItemInserted(layoutPosition)
                    bindingAdapterPosition
                }

                ivDeleteItem.setOnClickListener {
                    asteroidList.removeAt(layoutPosition)
                    notifyItemRemoved(layoutPosition)
                }

                ivMenuPanelBackArrow.setOnClickListener {
                    asteroidList[layoutPosition] = asteroidList[layoutPosition].first to false
                    notifyItemChanged(layoutPosition)
                }

                ivMenuPanelOpenArrow.setOnClickListener {
                    asteroidList[layoutPosition] = asteroidList[layoutPosition].first to true
                    notifyItemChanged(layoutPosition)
                }

                ivMoveItemUp.setOnClickListener {
                    if (layoutPosition > 1) {
                        asteroidList.removeAt(layoutPosition).apply {
                            asteroidList.add(layoutPosition - 1, this)
                        }
                        notifyItemMoved(layoutPosition, layoutPosition - 1)
                    }
                }

                ivMoveItemDown.setOnClickListener {
                    if (layoutPosition < asteroidList.size - 1) {
                        asteroidList.removeAt(layoutPosition).apply {
                            asteroidList.add(layoutPosition + 1, this)
                        }
                        notifyItemMoved(layoutPosition, layoutPosition + 1)
                    }
                }

                ivMoveItem.setOnTouchListener { _, event ->
                    if(MotionEventCompat.getActionMasked(event)== MotionEvent.ACTION_DOWN){
                        onStartDragListener.onStartDrag(this@HazardousAsteroidViewHolder)
                    }
                    false
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.WHITE)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseAsteroidViewHolder(view) {
        override fun bind(asteroid: Pair<AsteroidListDto.AsteroidDto, Boolean>) {
            FragmentFeaturesRecyclerViewHeaderItemBinding.bind(itemView).apply {
                tvHeader.text = asteroid.first.name
            }
        }

        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }
    }

    abstract class BaseAsteroidViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        abstract fun bind(asteroid: Pair<AsteroidListDto.AsteroidDto, Boolean>)
    }

    private fun generateAsteroidItem(): AsteroidListDto.AsteroidDto {
        val generatedIsPotentiallyHazardousAsteroid = Random.nextBoolean()

        return AsteroidListDto.AsteroidDto(
            id = UUID.randomUUID().toString(),
            name = "Кастомный астероид",
            absoluteMagnitudeH = (((Random.nextFloat() * 10) + 15).toString().substring(0, 6)
                .toFloat()),
            isPotentiallyHazardousAsteroid = generatedIsPotentiallyHazardousAsteroid,
            closeApproachData = listOf(
                AsteroidListDto.AsteroidDto.CloseApproachData(getCurrentDate())
            ),
            type = when (generatedIsPotentiallyHazardousAsteroid) {
                true -> 1
                false -> 2
            }
        )
    }

    private fun checkItemMenuPanel(
        isPanelOpen: Boolean,
        groupDetails: Group,
        groupMenuPanel: Group
    ) {
        if (isPanelOpen) {
            groupDetails.isVisible = false
            groupMenuPanel.isVisible = true
        } else {
            groupDetails.isVisible = true
            groupMenuPanel.isVisible = false
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (toPosition == 0) {
            asteroidList.removeAt(fromPosition).apply { asteroidList.add(1, this) }
            notifyItemMoved(fromPosition, 1)
        } else {
            asteroidList.removeAt(fromPosition).apply { asteroidList.add(toPosition, this) }
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onItemDismiss(position: Int) {
        asteroidList.removeAt(position)
        notifyItemRemoved(position)
    }
}