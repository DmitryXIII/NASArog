package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesRecyclerViewBinding
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import java.util.*
import kotlin.random.Random


class RecyclerViewFragment :
    BaseFragment<FragmentFeaturesRecyclerViewBinding>(FragmentFeaturesRecyclerViewBinding::inflate) {

    private val viewModel by viewModels<RecyclerViewViewModel>()
    lateinit var itemTouchHelper:ItemTouchHelper
    private lateinit var recyclerViewFragmentAdapter: RecyclerViewFragmentAdapter
    private lateinit var recViewLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recViewLayoutManager = LinearLayoutManager(requireContext())



        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        val dateStart = getCurrentDate()
        val dateEnd = getTwoDaysForwardDate()

        viewModel.getAsteroidsDataRequest(
            dateStart,
            dateEnd,
            getString(
                R.string.header,
                convertNasaDateFormatToMyFormat(dateStart),
                convertNasaDateFormatToMyFormat(dateEnd)
            )
        )

        binding.fabAddItem.setOnClickListener {
            recyclerViewFragmentAdapter.appendItem()
            binding.recyclerView.smoothScrollToPosition(recyclerViewFragmentAdapter.itemCount)
        }
    }

    private fun renderData(state: AsteroidDataState) {
        with(binding) {
            when (state) {
                AsteroidDataState.Loading -> {
                    setVisibilityOnStateLoading(recyclerViewSpinKit, recyclerView, fabAddItem)
                }
                is AsteroidDataState.AsteroidDataSuccess -> {
                    recyclerViewFragmentAdapter =
                        RecyclerViewFragmentAdapter(object : OnAsteroidItemClickListener {
                            override fun onAsteroidItemClick(asteroid: AsteroidListDto.AsteroidDto) {
                                showToast(requireContext(), asteroid.name)
                            }
                        },object: OnStartDragListener{
                            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                                itemTouchHelper.startDrag(viewHolder)
                            }
                        } )
                    recyclerViewFragmentAdapter.setData(state.asteroidList)
                    recyclerView.apply {
                        layoutManager = recViewLayoutManager
                        adapter = recyclerViewFragmentAdapter
                    }
                    itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(recyclerViewFragmentAdapter))
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                    setVisibilityOnStateSuccess(recyclerViewSpinKit, recyclerView, fabAddItem)
                }
                is AsteroidDataState.Error -> {}
            }
        }


    }

    class ItemTouchHelperCallback(private val recyclerViewFragmentAdapter: RecyclerViewFragmentAdapter) :
        ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            from: RecyclerView.ViewHolder,
            to: RecyclerView.ViewHolder
        ): Boolean {
            recyclerViewFragmentAdapter.onItemMove(
                from.absoluteAdapterPosition,
                to.absoluteAdapterPosition
            )
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            recyclerViewFragmentAdapter.onItemDismiss(viewHolder.absoluteAdapterPosition)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (viewHolder !is RecyclerViewFragmentAdapter.HeaderViewHolder) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
                    (viewHolder as RecyclerViewFragmentAdapter.BaseAsteroidViewHolder).onItemSelected()
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            if (viewHolder !is RecyclerViewFragmentAdapter.HeaderViewHolder)
                (viewHolder as RecyclerViewFragmentAdapter.BaseAsteroidViewHolder).onItemClear()
        }
    }
}