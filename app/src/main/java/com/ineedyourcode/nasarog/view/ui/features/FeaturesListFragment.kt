package com.ineedyourcode.nasarog.view.ui.features

import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesListBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment
import kotlin.random.Random

/**
 * MultiBackstack из navigation component создает проблемы при воспроизведении анимации.
 * Если перейти по кнопке в один из вложенных фрагментов и вернуться обратно, то
 * обнуляется все, что связано с координатами на экране, из-за этого анимация начинает вести себя неправильно.
 * Долго пытался поймать в жизненных циклах фрагмента разные состояния savedInstanceState и
 * findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData, но так и не уловил момент, почему
 * происходит обнуление значений координат виджетов.
 * Оставлю себе это как факультатив на каникулах.
 * Сейчас анимация отображается корректно только при переходе на этот фрагмент через bottomNavigationView.
 * ===============
 * ===============
 * Обе анимации (передвигающиеся круги и звездное небо) зациклены, из-за большого количества звезд на небе
 * на эмуляторе может подтормаживать, на реальном устройстве смотрится интереснее.
 */

private const val STARS_NUMBER = 1000 // количество звезд на экране
private const val STARS_BORNING_DURATION = 10000 // врямя стартовой отрисовки звезд (миллисекунды)
private const val COMET_BORNING_CHANCE = 3 // вероятность рождения летящей кометы (%)

class FeaturesListFragment :
    BaseFragment<FragmentFeaturesListBinding>(FragmentFeaturesListBinding::inflate) {

    companion object {
        const val KEY_BACK_STACK_ENTRY = "KEY_BACK_STACK_ENTRY"
    }

    private val starsList = mutableListOf<View>()

    private val starsColorsList = mutableListOf<Int>()

    private val dotsList = mutableListOf<View>()

    private var savedInstance: Bundle? = null

    private var backEntry: Boolean? = false

    private var rangeX = 0f
    private var rangeY = 0f
    private var spaceWidth = 0f
    private var spaceHeight = 0f

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
        if (backEntry != true || savedInstance != null) {
            with(binding) {
                rangeX = rootContainer.width.toFloat() - animatedCircle1.width
                rangeY = rootContainer.height.toFloat() - animatedCircle1.width
                spaceWidth = rootContainer.height.toFloat()
                spaceHeight = rootContainer.height.toFloat()

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

                dotsList.add(animatedCircle1)
                dotsList.add(animatedCircle2)
                dotsList.add(animatedCircle3)
                dotsList.add(animatedCircle4)
                dotsList.add(animatedCircle5)
            }
            backEntry = false

            starsColorsList.add(R.drawable.space_star_color_1)
            starsColorsList.add(R.drawable.space_star_color_2)
            starsColorsList.add(R.drawable.space_star_color_3)
            starsColorsList.add(R.drawable.space_star_color_4)
            starsColorsList.add(R.drawable.space_star_color_5)

            dotsAnimationScene1FallDown(dotsList)

            for (starView in starsList) {
                animationSpaceBackgroundScene1(starView)
            }
        }
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstance = savedInstanceState

        for (i in 0..STARS_NUMBER) {
            starsList.add(View(requireContext()).apply {
                alpha = 0f
            })

            starsList[i].parent?.let {
                (starsList[i].parent as FrameLayout).removeView(starsList[i])
            }

            binding.rootFrame.addView(starsList[i])
        }

        backEntry =
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
                KEY_BACK_STACK_ENTRY
            )?.value

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

    private fun dotsAnimationScene1FallDown(viewList: List<View>) {
        delay = 300
        for (view in viewList) {
            view.animate()
                .setStartDelay(delay)
                .alpha(1f)
                .y(scene1DestinationY)
                .setInterpolator(BounceInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> dotsAnimationScene2MoveInOneDot(viewList)
                    }
                }
                .duration = 1500
            delay += 300
        }
        delay = 0
    }

    private fun dotsAnimationScene2MoveInOneDot(viewList: List<View>) {
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
                        R.id.animated_circle_5 -> dotsAnimationScene3MoveToBottomRight(viewList)
                    }
                }
                .duration = 1000
        }
    }

    private fun dotsAnimationScene3MoveToBottomRight(viewList: List<View>) {
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
                        R.id.animated_circle_5 -> dotsAnimationScene4MoveToTopRight(viewList)
                    }
                }
                .duration = (rangeX).toLong()
            delay += dotStartMovingDelay / dotSpeed
            dotScale -= 0.15f
        }
        delay = 0
        dotScale = 1f
    }

    private fun dotsAnimationScene4MoveToTopRight(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .y(scene4DestinationY)
                .setStartDelay(delay)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> dotsAnimationScene5MoveToTopLeft(viewList)
                    }
                }
                .duration = (rangeY / dotSpeed).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun dotsAnimationScene5MoveToTopLeft(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .x(scene5DestinationX)
                .setStartDelay(delay)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> dotsAnimationScene6MoveToBottomLeft(viewList)
                    }
                }
                .duration = (rangeX).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun dotsAnimationScene6MoveToBottomLeft(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .y(scene1DestinationY)
                .setStartDelay(delay)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> dotsAnimationScene7MoveToOneDot(viewList)
                    }
                }
                .duration = (rangeY / dotSpeed).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun dotsAnimationScene7MoveToOneDot(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .x(dot3startX)
                .setStartDelay(delay)
                .setInterpolator(OvershootInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> dotsAnimationScene8MoveToFiveDots(viewList)
                    }
                }
                .duration = (rangeY / dotSpeed * 1.5).toLong()
            delay += dotStartMovingDelay / dotSpeed
        }
        delay = 0
    }

    private fun dotsAnimationScene8MoveToFiveDots(viewList: List<View>) {
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
                            dotsAnimationScene9MoveToStartPositions(viewList)
                        }
                    }
                }
                .duration = 1000
        }
    }

    private fun dotsAnimationScene9MoveToStartPositions(viewList: List<View>) {
        for (view in viewList) {
            view.animate()
                .alpha(0f)
                .y(dotsStartY)
                .setStartDelay(delay)
                .setInterpolator(AnticipateOvershootInterpolator())
                .withEndAction {
                    when (view.id) {
                        R.id.animated_circle_5 -> dotsAnimationScene1FallDown(viewList)
                    }
                }
                .duration = 1000
            delay += 150
        }
        delay = 0
    }

    private fun animationSpaceBackgroundScene1(starView: View) {
        val radius = (1..10).random()
        starView.apply {
            setBackgroundResource(starsColorsList[Random.nextInt(starsColorsList.size)])
            layoutParams = FrameLayout.LayoutParams(radius, radius)
            x = (0..spaceWidth.toInt()).random().toFloat()
            y = (0..spaceHeight.toInt()).random().toFloat()
            animate()
                .setStartDelay((100..STARS_BORNING_DURATION).random().toLong())
                .alpha(Random.nextFloat())
                .setInterpolator(LinearInterpolator())
                .withEndAction { animationSpaceBackgroundScene2(starView) }
                .duration = (100..10000).random().toLong()
        }
    }

    private fun animationSpaceBackgroundScene2(starView: View) {
        starView.apply {
            animate()
                .setStartDelay((3000..10000).random().toLong())
                .alpha(0f)
                .setInterpolator(LinearInterpolator())
                .withEndAction {
                    animationSpaceBackgroundScene3(starView)
                }
                .duration = (100..2000).random().toLong()
        }
    }

    private fun animationSpaceBackgroundScene3(starView: View) {
        val radius = (1..10).random()
        starView.apply {
            setBackgroundResource(starsColorsList[Random.nextInt(starsColorsList.size)])
            layoutParams = FrameLayout.LayoutParams(radius, radius)
            x = (0..spaceWidth.toInt()).random().toFloat()
            y = (0..spaceHeight.toInt()).random().toFloat()
            when (Random.nextFloat()) {
                in 0f..COMET_BORNING_CHANCE / 100f -> {
                    alpha = 0f
                    val flyingToX = if (Random.nextBoolean()) {
                        (0..(spaceWidth).toInt()).random().toFloat()
                    } else {
                        ((0..(spaceWidth).toInt()).random().toFloat()) * -1
                    }
                    val flyingToY = if (Random.nextBoolean()) {
                        (0..(spaceHeight).toInt()).random().toFloat()
                    } else {
                        ((0..(spaceHeight).toInt()).random().toFloat()) * -1
                    }
                    val flyingDuration = (200..1000).random().toLong()
                    animate()
                        .setStartDelay(0)
                        .alpha(1f)
                        .x(starView.x + flyingToX / 2)
                        .y(starView.y + flyingToY / 2)
                        .setInterpolator(LinearInterpolator())
                        .withEndAction {
                            animate()
                                .setStartDelay(0)
                                .alpha(0f)
                                .x(starView.x + flyingToX / 2)
                                .y(starView.y + flyingToY / 2)
                                .setInterpolator(LinearInterpolator())
                                .withEndAction { animationSpaceBackgroundScene3(starView) }
                                .duration = flyingDuration
                        }
                        .duration = flyingDuration
                }
                else -> {
                    animate()
                        .setStartDelay((0..1000).random().toLong())
                        .alpha(Random.nextFloat())
                        .setInterpolator(LinearInterpolator())
                        .withEndAction { animationSpaceBackgroundScene2(starView) }
                        .duration = (300..10000).random().toLong()
                }
            }
        }
    }
}