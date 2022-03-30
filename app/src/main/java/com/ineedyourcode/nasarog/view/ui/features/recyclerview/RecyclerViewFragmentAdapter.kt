package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.ineedyourcode.nasarog.R
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

class RecyclerViewFragmentAdapter(val onClickListener: OnAsteroidItemClickListener) :
    RecyclerView.Adapter<RecyclerViewFragmentAdapter.BaseAsteroidViewHolder>() {
    lateinit var asteroidList: MutableList<AsteroidListDto.AsteroidDto>

    var startPosition: Int = 0
    var endPosition: Int = 0

    fun setData(mAsteroidList: List<AsteroidListDto.AsteroidDto>) {
        asteroidList = mAsteroidList.toMutableList()
    }

    fun appendItem() {
        asteroidList.add(generateAsteroidItem())
        notifyItemInserted(asteroidList.size - 1)
    }

    fun getOnScreenPosition(mStartPosition: Int, mEndPosition: Int) {
        startPosition = mStartPosition
        endPosition = mEndPosition
        Log.d("WWWWWWW", "startPos - $startPosition")
        Log.d("WWWWWWW", "endPos - $endPosition")
    }

    override fun getItemViewType(position: Int): Int {
        return asteroidList[position].type
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
        holder.bind(asteroidList[position])
    }

    override fun getItemCount() = asteroidList.size

    inner class UnhazardousAsteroidViewHolder(view: View) : BaseAsteroidViewHolder(view) {
        override fun bind(asteroid: AsteroidListDto.AsteroidDto) {
            FragmentFeaturesRecyclerViewUnhazardousAsteroidItemBinding.bind(itemView).apply {
//                Log.d("WWWWWWW", "IS RECYCLABLE $isRecyclable")
//                Log.d("WWWWWWW", "layoutPosition $layoutPosition")
//                Log.d("WWWWWWW", "absoluteAdapterPosition $absoluteAdapterPosition")
//                Log.d("WWWWWWW", "adapterPosition $adapterPosition")
//                Log.d("WWWWWWW", "bindingAdapterPosition $bindingAdapterPosition")
//                Log.d("WWWWWWW", "position $position")
//                Log.d("WWWWWWW", "oldPosition $oldPosition")
                tvAsteroidName.text = asteroid.name
                tvAsteroidMagnitudeH.text = asteroid.absoluteMagnitudeH.toString()
                tvCloseApproachDate.text =
                    convertNasaDateFormatToMyFormat(asteroid.closeApproachData.first().closeApproachDate)

                ivUnhazardousItemIcon.setOnClickListener {
                    onClickListener.onAsteroidItemClick(asteroid)
                }

                ivAddItemIcon.setOnClickListener {
                    asteroidList.add(layoutPosition, generateAsteroidItem())
                    notifyItemInserted(layoutPosition)
                }

                ivDeleteItemIcon.setOnClickListener {
                    closeItemMenuPanel(
                        layoutUnhazardousItem,
                        R.id.itemMenuPanel,
                        R.id.layoutUnhazardousItem
                    )
                    asteroidList.removeAt(layoutPosition)
                    notifyItemRemoved(layoutPosition)
                }

                ivMenuPanelBackArrow.setOnClickListener {
                    closeItemMenuPanel(
                        layoutUnhazardousItem,
                        R.id.itemMenuPanel,
                        R.id.layoutUnhazardousItem
                    )
                }

                ivMenuPanelOpenArrow.setOnClickListener {
                    openItemMenuPanel(
                        layoutUnhazardousItem,
                        R.id.itemMenuPanel,
                        R.id.layoutUnhazardousItem,
                        R.id.tv_asteroid_name_title
                    )
                }
            }
        }
    }

    inner class HazardousAsteroidViewHolder(view: View) : BaseAsteroidViewHolder(view) {
        override fun bind(asteroid: AsteroidListDto.AsteroidDto) {
            FragmentFeaturesRecyclerViewHazardousAsteroidItemBinding.bind(itemView).apply {
                tvAsteroidName.text = asteroid.name
                tvAsteroidMagnitudeH.text = asteroid.absoluteMagnitudeH.toString()
                tvCloseApproachDate.text =
                    convertNasaDateFormatToMyFormat(asteroid.closeApproachData.first().closeApproachDate)
                ivHazardousItemIcon.setOnClickListener {
                    onClickListener.onAsteroidItemClick(asteroid)
                }
            }
        }
    }

    inner class HeaderViewHolder(view: View) : BaseAsteroidViewHolder(view) {
        override fun bind(asteroid: AsteroidListDto.AsteroidDto) {
            FragmentFeaturesRecyclerViewHeaderItemBinding.bind(itemView).apply {
                tvHeader.text = asteroid.name
            }
        }
    }

    abstract class BaseAsteroidViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(asteroid: AsteroidListDto.AsteroidDto)
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

    private fun openItemMenuPanel(
        layoutItem: ConstraintLayout,
        menuPanelId: Int,
        layoutItemId: Int,
        asteroidTitleId: Int
    ) {
        ConstraintSet().apply {
            clone(layoutItem)
            clear(menuPanelId, ConstraintSet.START)
            connect(
                menuPanelId,
                ConstraintSet.START,
                asteroidTitleId,
                ConstraintSet.START
            )
            connect(
                menuPanelId,
                ConstraintSet.END,
                layoutItemId,
                ConstraintSet.END
            )
            constrainWidth(menuPanelId, 0)
            applyTo(layoutItem)
        }

        TransitionManager.beginDelayedTransition(
            layoutItem,
            ChangeBounds().apply {
                interpolator = DecelerateInterpolator()
                duration = 300
            })
    }

    private fun closeItemMenuPanel(
        layoutItem: ConstraintLayout,
        menuPanelId: Int,
        layoutItemId: Int
    ) {
        ConstraintSet().apply {
            clone(layoutItem)
            clear(menuPanelId, ConstraintSet.START)
            clear(menuPanelId, ConstraintSet.END)
            connect(
                menuPanelId,
                ConstraintSet.START,
                layoutItemId,
                ConstraintSet.END
            )
            constrainWidth(menuPanelId, ConstraintSet.WRAP_CONTENT)
            applyTo(layoutItem)
        }

        TransitionManager.beginDelayedTransition(
            layoutItem,
            ChangeBounds().apply {
                interpolator = AccelerateInterpolator()
                duration = 300
            })
    }
}