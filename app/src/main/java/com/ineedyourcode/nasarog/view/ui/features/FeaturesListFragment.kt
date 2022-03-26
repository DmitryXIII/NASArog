package com.ineedyourcode.nasarog.view.ui.features

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesListBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition.stable.SharedElementTransitionFragment

class FeaturesListFragment :
    BaseFragment<FragmentFeaturesListBinding>(FragmentFeaturesListBinding::inflate) {

    companion object {
        const val KEY_BACK_STACK_ENTRY = "KEY_BACK_STACK_ENTRY"
    }

    private val viewList = mutableListOf<View>()

    private var backEntry = false

    private var isFirstOnResume = true

    private var rangeX = 0f
    private var rangeY = 0f

    private var dotScale = 1f

    private var dotSpeed = 2 // единица изерения -> px/ms

    private var delay = 300L
    private var dotStartMovingDelay = 0L

    private var dotsStartY = 0f
    private var dot1startX = 0f
    private var dot2startX = 0f
    private var dot3startX = 0f
    private var dot4startX = 0f
    private var dot5startX = 0f

    private var scene1DestinationY = 0f
    private var scene3DestinationX = 0f
    private var scene4DestinationY = 0f
    private var scene5DestinationX = 0f

    override fun onResume() {
        with(binding) {
            rangeX = rootContainer.width.toFloat() - animatedCircle1.width
            rangeY = rootContainer.height.toFloat() - animatedCircle1.width

            dotStartMovingDelay = animatedCircle1.width.toLong()

            dot1startX = animatedCircle1.x
            dot2startX = animatedCircle2.x
            dot3startX = animatedCircle3.x
            dot4startX = animatedCircle4.x
            dot5startX = animatedCircle5.x
            dotsStartY = animatedCircle1.y

            scene1DestinationY = rootContainer.y + rootContainer.height - animatedCircle1.height
            scene3DestinationX = rootContainer.x + rootContainer.width - animatedCircle5.width
            scene4DestinationY = rootContainer.y
            scene5DestinationX = rootContainer.x

            viewList.add(animatedCircle1)
            viewList.add(animatedCircle2)
            viewList.add(animatedCircle3)
            viewList.add(animatedCircle4)
            viewList.add(animatedCircle5)
        }
        if (isFirstOnResume) {
            animationScene1FallDown(viewList)
        }
        isFirstOnResume = false
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backEntry = findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(KEY_BACK_STACK_ENTRY)?.value == true

        Log.d("ATGANIM", "savedInstance - $savedInstanceState")

        binding.btnCustomBehavior.setOnClickListener {
            findNavController().navigate(R.id.action_featuresListFragment_to_coordinatorLayoutExampleFragment)
        }

        binding.btnSharedElementTransition.setOnClickListener {
            findNavController().navigate(R.id.action_featuresListFragment_to_sharedElementTransitionFragment)
        }

        binding.btnSharedElementTransitionNotStable.setOnClickListener {
            findNavController().navigate(R.id.action_featuresListFragment_to_notStableAnimationFragment)
        }
    }

    private fun animationScene1FallDown(viewList: List<View>) {
        delay = 300
        for (view in viewList) {
            view.animate()
                .setStartDelay(delay)
                .alpha(1f)
                .y(scene1DestinationY)
                .setInterpolator(BounceInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene2MoveInOneDot(viewList)
                    }
                }
                .duration = 1500
            delay += 300
        }
        delay = 0
    }

    private fun animationScene2MoveInOneDot(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .x(dot3startX)
                .setStartDelay(
                    when (view.id) {
                        R.id.animated_circle_1 -> 200
                        R.id.animated_circle_2 -> 100
                        R.id.animated_circle_4 -> 100
                        R.id.animated_circle_5 -> 200
                        else -> 0
                    }
                )
                .setInterpolator(AnticipateOvershootInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene3MoveToBottomRight(viewList)
                    }
                }
                .duration = 1000
        }
    }

    private fun animationScene3MoveToBottomRight(viewList: List<View>) {
        delay = 150
        for (view in viewList) {
            view.animate()
                .scaleX(dotScale)
                .scaleY(dotScale)
                .x(scene3DestinationX)
                .setStartDelay(delay)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene4MoveToTopRight(viewList)
                    }
                }
                .duration = (rangeX).toLong()
            delay += dotStartMovingDelay / dotSpeed
            dotScale -= 0.15f
        }
        delay = 0
        dotScale = 1f
    }

    private fun animationScene4MoveToTopRight(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .y(scene4DestinationY)
                .setStartDelay(delay)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene5MoveToTopLeft(viewList)
                    }
                }
                .duration = (rangeY / dotSpeed).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun animationScene5MoveToTopLeft(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .x(scene5DestinationX)
                .setStartDelay(delay)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene6MoveToBottomLeft(viewList)
                    }
                }
                .duration = (rangeX).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun animationScene6MoveToBottomLeft(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .y(scene1DestinationY)
                .setStartDelay(delay)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene7MoveToOneDot(viewList)
                    }
                }
                .duration = (rangeY / dotSpeed).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun animationScene7MoveToOneDot(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .x(dot3startX)
                .setStartDelay(delay)
                .setInterpolator(OvershootInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene8MoveToFiveDots(viewList)
                    }
                }
                .duration = (rangeY / dotSpeed * 1.5).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun animationScene8MoveToFiveDots(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .x(
                    when (view.id) {
                        R.id.animated_circle_1 -> dot1startX
                        R.id.animated_circle_2 -> dot2startX
                        R.id.animated_circle_3 -> dot3startX
                        R.id.animated_circle_4 -> dot4startX
                        else -> dot5startX
                    }
                )
                .setStartDelay(
                    when (view.id) {
                        R.id.animated_circle_1 -> 200
                        R.id.animated_circle_5 -> 200
                        R.id.animated_circle_2 -> 100
                        R.id.animated_circle_4 -> 100
                        else -> 0
                    }
                )
                .setInterpolator(OvershootInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> {
                            animationScene9MoveToStartPositions(viewList)
                        }
                    }
                }
                .duration = 1000
        }
    }

    private fun animationScene9MoveToStartPositions(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .alpha(0f)
                .y(dotsStartY)
                .setStartDelay(delay)
                .setInterpolator(AnticipateOvershootInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> animationScene1FallDown(viewList)
                    }
                }
                .duration = 1000
            delay += 150
        }
        delay = 0
    }
}