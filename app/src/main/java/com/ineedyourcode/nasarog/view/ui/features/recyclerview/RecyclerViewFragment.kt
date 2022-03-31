package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class RecyclerViewFragment :
    BaseFragment<FragmentFeaturesRecyclerViewBinding>(FragmentFeaturesRecyclerViewBinding::inflate) {

    private val viewModel by viewModels<RecyclerViewViewModel>()
    lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var recyclerViewFragmentAdapter: RecyclerViewFragmentAdapter
    private lateinit var recViewLayoutManager: LinearLayoutManager
    private lateinit var asteroidList: List<AsteroidListDto.AsteroidDto>
    private var filteredAsteroidList = mutableListOf<AsteroidListDto.AsteroidDto>()


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

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "" || s.toString() == "null") {
                    filteredAsteroidList.clear()
                } else {
                    for (asteroid in asteroidList) {
                        if (asteroid.name.lowercase().contains(s.toString().lowercase())) {
                            filteredAsteroidList.clear()
                            if (!filteredAsteroidList.contains(asteroid)) {
                                filteredAsteroidList.add(asteroid)
                            }
                        }
                    }
                }
                recyclerViewFragmentAdapter.searchFilter(filteredAsteroidList)
            }
        })

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
                    asteroidList = state.asteroidList
                    recyclerViewFragmentAdapter =
                        RecyclerViewFragmentAdapter(object : OnAsteroidItemClickListener {
                            override fun onAsteroidItemClick(asteroid: AsteroidListDto.AsteroidDto) {
                                showToast(requireContext(), asteroid.name)
                            }
                        }, object : OnStartDragListener {
                            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                                itemTouchHelper.startDrag(viewHolder)
                            }
                        })
                    recyclerViewFragmentAdapter.setData(asteroidList)
                    recyclerView.apply {
                        layoutManager = recViewLayoutManager
                        adapter = recyclerViewFragmentAdapter
                    }
                    itemTouchHelper =
                        ItemTouchHelper(ItemTouchHelperCallback(recyclerViewFragmentAdapter))
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
                    setVisibilityOnStateSuccess(recyclerViewSpinKit, recyclerView, fabAddItem)
                }
                is AsteroidDataState.Error -> {}
            }
        }
    }

    class ItemTouchHelperCallback(val recyclerActivityAdapter: RecyclerViewFragmentAdapter) :
        ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun onMove(
            recyclerView: RecyclerView,
            from: RecyclerView.ViewHolder,
            to: RecyclerView.ViewHolder
        ): Boolean {
            recyclerActivityAdapter.onItemMove(from.adapterPosition, to.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            recyclerActivityAdapter.onItemDismiss(viewHolder.adapterPosition)
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            if (viewHolder is RecyclerViewFragmentAdapter.HeaderViewHolder) return 0
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(
                dragFlags,
                swipeFlags
            )
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
                (viewHolder as RecyclerViewFragmentAdapter.BaseAsteroidViewHolder).onItemSelected()
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemClear()
        }
    }
}