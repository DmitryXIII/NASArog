package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewHazardousAsteroidItemBinding
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewUnhazardousAsteroidItemBinding
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto

class RecyclerViewFragmentAdapter(val onClickListener: OnAsteroidItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var asteroidList: List<AsteroidListDto.AsteroidDto>

    fun setData(mAsteroidList: List<AsteroidListDto.AsteroidDto>) {
        asteroidList = mAsteroidList
    }

    override fun getItemViewType(position: Int): Int {
        return when (asteroidList[position].isPotentiallyHazardousAsteroid) {
            true -> 1
            false -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val binding = FragmentFeaturesRecyclerViewHazardousAsteroidItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HazardousAsteroidViewHolder(binding.root)
            }
            else -> {
                val binding = FragmentFeaturesRecyclerViewUnhazardousAsteroidItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                UnhazardousAsteroidViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            1 -> {
                (holder as HazardousAsteroidViewHolder).bind(asteroidList[position])
            }
            2 -> {
                (holder as UnhazardousAsteroidViewHolder).bind(asteroidList[position])
            }
        }
    }

    override fun getItemCount() = asteroidList.size

    inner class UnhazardousAsteroidViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(asteroid: AsteroidListDto.AsteroidDto) {
            FragmentFeaturesRecyclerViewUnhazardousAsteroidItemBinding.bind(itemView).apply {
                tvAsteroidName.text = asteroid.name
                tvAsteroidMagnitudeH.text = asteroid.absoluteMagnitudeH.toString()
                tvCloseApproachDate.text = asteroid.closeApproachData.first().closeApproachDate
                ivUnhazardousItemIcon.setOnClickListener {
                    onClickListener.onAsteroidItemClick(asteroid)
                }
            }
        }
    }

    inner class HazardousAsteroidViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(asteroid: AsteroidListDto.AsteroidDto) {
            FragmentFeaturesRecyclerViewHazardousAsteroidItemBinding.bind(itemView).apply {
                tvAsteroidName.text = asteroid.name
                tvAsteroidMagnitudeH.text = asteroid.absoluteMagnitudeH.toString()
                tvCloseApproachDate.text = asteroid.closeApproachData.first().closeApproachDate
                ivHazardousItemIcon.setOnClickListener {
                    onClickListener.onAsteroidItemClick(asteroid)
                }
            }
        }
    }
}