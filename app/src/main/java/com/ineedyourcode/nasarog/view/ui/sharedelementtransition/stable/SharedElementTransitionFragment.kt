package com.ineedyourcode.nasarog.view.ui.sharedelementtransition.stable

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import coil.load
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentSharedElementTransitionBinding
import com.ineedyourcode.nasarog.utils.*
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

/**
 * Описание ужаса, происходящего в коде этого фрагмента.
 * Изначально была идея реализовать shared element transition в рамках navigation component.
 * Задача усложнилась желанием выводить в shared element transition элементы, подгружаемые из сети (в данном случае - три разные картинки дня из NASA).
 * ============
 * ============
 * 1. Первая проблема, с которой столкнулся: если viewModel запрашивает на сервере сразу три картинки, и liveData посылает в фрагмент данные,
 * то в фрагменте наблюдатель (observer) принимает не все отправленные из viewModel данные (не все картинки могут подгрузиться).
 * Пример такого поведения наблюдателя есть в фрагменте NotStableAnimationFragment.
 * Пофиксил, создав в viewModel три разные liveData, каждая из которых отвечает за конкретную картинку.
 * Соответственно, в этом фрагменте пришлось подписаться на три разные liveData, и немного раздуть State-класс.
 * Подскажите, пожалуйста, есть ли способ корректно обрабатывать ответы, используя одну liveData?
 *  ============
 *  ============
 * 2. Вторая возникшая проблема - плавность анимации при возвращении с фрагмента SharedElementTransitionDetailsFragment() на этот фрагмент.
 * Если пользоваться навигацией через fragment manager, то details-фрагмент можно было бы открыть через .add(...),
 * и тогда не было проблем с потерей состояния этого фрагмента.
 * Но в navigation component фрагменты открываются через .replace(...), переопределение этого момента
 * (заменить .replace() на .add()) - вопрос, на который я не нашел внятного ответа.
 * Поэтому при возвращении с экрана details в этом фрагменте вся информация обнулялась и подгружалась заново из сети,
 * из-за этого были лаги анимации.
 * В документации Google нашел про метод findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData,
 * с помощью него в рамках navigation component можно передавать данные между фрагментами в обратном направлении в рамках multiple backstack
 * (в моем случае - из details в этот фрагмент). Отсюда такое количество const val ключей и раздутый метод backStateEntryData?.observe(viewLifecycleOwner).
 * В итоге мучительно дебага понял, что в navigation component:
 * a) если переходить между фрагментами через bottomNavigationView, то это типа корневой backstack,
 * и фрагменты сохраняют свое состояние через savedInstanceState,
 * но при этом пропадают сетевые данные из findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData.
 * б) если переходить уже непосредственно из фрагмента на другой фрагмент, то это уже multiple backstack, в котором
 * стартовый фрамент обнуляет свой savedInstanceState, и надо пользоваться findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData.
 *  ============
 *  ============
 *  Баг, который я не смог устранить:
 *  1. Перейти на этот фрагмент через bottomNavigationView.
 *  2. Кликнуть любую картинку - откроется экран details.
 *  3. При открытом details перейти через bottomNavigationView на любой другой экран.
 *  4. Через bottomNavigationView вернуться обратно (нажать на иконку Shared element transition).
 *  5. Откроется details, но в bottomNavigationView иконка не становится активной.
 *  6. Через стрелку "назад" вернуться с экрана details (SharedElementTransitionDetailsFragment) на
 *  этот фрагмент (SharedElementTransitionFragment) - иконка в bottomNavigationView становится активной.
 *  Случайно, не знаете, почему так происходит, и как это исправить?
 */

class SharedElementTransitionFragment :
    BaseFragment<FragmentSharedElementTransitionBinding>(FragmentSharedElementTransitionBinding::inflate) {

    private val mapOfArguments = HashMap<String, Any>()
    private val apodExampleViewModel by viewModels<SharedElementTransitionViewModel>()

    companion object {
        const val DATE_TYPE_TODAY = "TODAY"
        const val DATE_TYPE_YESTERDAY = "YESTERDAY"
        const val DATE_TYPE_BEFORE_YESTERDAY = "BEFORE_YESTERDAY"
        const val KEY_BACK_STACK_ENTRY_MAP = "KEY_BACK_STACK_ENTRY_MAP"
        const val KEY_TODAY_DATE = "KEY_TODAY_DATE"
        const val KEY_TODAY_TITLE = "KEY_TODAY_TITLE"
        const val KEY_TODAY_HDURL = "KEY_TODAY_HDURL"
        const val KEY_TODAY_EXPLANATION = "KEY_TODAY_EXPLANATION"
        const val KEY_YESTERDAY_DATE = "KEY_YESTERDAY_DATE"
        const val KEY_YESTERDAY_TITLE = "KEY_YESTERDAY_TITLE"
        const val KEY_YESTERDAY_HDURL = "KEY_YESTERDAY_HDURL"
        const val KEY_YESTERDAY_EXPLANATION = "KEY_YESTERDAY_EXPLANATION"
        const val KEY_BEFORE_YESTERDAY_DATE = "KEY_BEFORE_YESTERDAY_DATE"
        const val KEY_BEFORE_YESTERDAY_TITLE = "KEY_BEFORE_YESTERDAY_TITLE"
        const val KEY_BEFORE_YESTERDAY_HDURL = "KEY_BEFORE_YESTERDAY_HDURL"
        const val KEY_BEFORE_YESTERDAY_EXPLANATION = "KEY_BEFORE_YESTERDAY_EXPLANATION"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backStateEntryData = findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<Map<String, String>>(
                KEY_BACK_STACK_ENTRY_MAP
            )

        backStateEntryData?.observe(viewLifecycleOwner) { mapResult ->
            with(binding) {
                tvApodExampleTodayDate.text = mapResult[KEY_TODAY_DATE]
                tvApodExampleTodayTitle.text = mapResult[KEY_TODAY_TITLE]
                ivApodExampleToday.load(mapResult[KEY_TODAY_HDURL])

                tvApodExampleYesterdayDate.text = mapResult[KEY_YESTERDAY_DATE]
                tvApodExampleYesterdayTitle.text = mapResult[KEY_YESTERDAY_TITLE]
                ivApodExampleYesterday.load(mapResult[KEY_YESTERDAY_HDURL])

                tvApodExampleBeforeYesterdayDate.text =
                    mapResult[KEY_BEFORE_YESTERDAY_DATE]
                tvApodExampleBeforeYesterdayTitle.text =
                    mapResult[KEY_BEFORE_YESTERDAY_TITLE]
                ivApodExampleBeforeYesterday.load(mapResult[KEY_BEFORE_YESTERDAY_HDURL])

                setOnImageClickNavigation(
                    ivApodExampleToday,
                    tvApodExampleTodayDate,
                    tvApodExampleTodayTitle,
                    DATE_TYPE_TODAY
                )

                setOnImageClickNavigation(
                    ivApodExampleYesterday,
                    tvApodExampleYesterdayDate,
                    tvApodExampleYesterdayTitle,
                    DATE_TYPE_YESTERDAY
                )

                setOnImageClickNavigation(
                    ivApodExampleBeforeYesterday,
                    tvApodExampleBeforeYesterdayDate,
                    tvApodExampleBeforeYesterdayTitle,
                    DATE_TYPE_BEFORE_YESTERDAY,
                    postponed = true
                )
            }
        }

        if (savedInstanceState != null || backStateEntryData?.value == null) {

            apodExampleViewModel.getLiveDataToday().observe(viewLifecycleOwner) {
                renderDataToday(it)
            }

            apodExampleViewModel.getLiveDataYesterday().observe(viewLifecycleOwner) {
                renderDataYesterday(it)
            }

            apodExampleViewModel.getLiveDataBeforeYesterday().observe(viewLifecycleOwner) {
                renderDataBeforeYesterday(it)
            }

            apodExampleViewModel.getApod()
        }
    }

    private fun renderDataToday(state: SharedElementTransitionState) = with(binding) {
        when (state) {
            SharedElementTransitionState.TodayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarToday,
                    cardApodExampleToday
                )
            }

            is SharedElementTransitionState.TodaySuccess -> {
                tvApodExampleTodayDate.text = getCurrentDate()
                tvApodExampleTodayTitle.text = state.apodToday.title
                ivApodExampleToday.loadWithTransformAndCallback(state.apodToday.hdurl, 200) {
                    setVisibilityOnStateSuccess(progressBarToday, cardApodExampleToday)
                    setOnImageClickNavigation(
                        ivApodExampleToday,
                        tvApodExampleTodayDate,
                        tvApodExampleTodayTitle,
                        DATE_TYPE_TODAY
                    )
                }

                addValuesToMapOfArguments(
                    DATE_TYPE_TODAY,
                    state.apodToday.hdurl,
                    state.apodToday.explanation
                )
            }

            is SharedElementTransitionState.TodayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            else -> {}
        }
    }

    private fun renderDataYesterday(state: SharedElementTransitionState) = with(binding) {
        when (state) {
            SharedElementTransitionState.YesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarYesterday,
                    cardApodExampleYesterday
                )
            }

            is SharedElementTransitionState.YesterdaySuccess -> {
                tvApodExampleYesterdayDate.text = getYesterdayDate()
                tvApodExampleYesterdayTitle.text = state.apodYesterday.title
                ivApodExampleYesterday.loadWithTransformAndCallback(
                    state.apodYesterday.hdurl,
                    200
                ) {
                    setOnImageClickNavigation(
                        ivApodExampleYesterday,
                        tvApodExampleYesterdayDate,
                        tvApodExampleYesterdayTitle,
                        DATE_TYPE_YESTERDAY
                    )
                    setVisibilityOnStateSuccess(
                        progressBarYesterday,
                        cardApodExampleYesterday
                    )
                }

                addValuesToMapOfArguments(
                    DATE_TYPE_YESTERDAY,
                    state.apodYesterday.hdurl,
                    state.apodYesterday.explanation
                )
            }

            is SharedElementTransitionState.BeforeYesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            else -> {}
        }
    }

    private fun renderDataBeforeYesterday(state: SharedElementTransitionState) = with(binding) {
        when (state) {
            SharedElementTransitionState.BeforeYesterdayLoading -> {
                setVisibilityOnStateLoading(
                    progressBarBeforeYesterday,
                    cardApodExampleBeforeYesterday
                )
            }

            is SharedElementTransitionState.BeforeYesterdaySuccess -> {
                tvApodExampleBeforeYesterdayDate.text = getBeforeYesterdayDate()
                tvApodExampleBeforeYesterdayTitle.text = state.apodBeforeYesterday.title
                ivApodExampleBeforeYesterday.loadWithTransformAndCallback(
                    state.apodBeforeYesterday.hdurl,
                    200
                ) {
                    setOnImageClickNavigation(
                        ivApodExampleBeforeYesterday,
                        tvApodExampleBeforeYesterdayDate,
                        tvApodExampleBeforeYesterdayTitle,
                        DATE_TYPE_BEFORE_YESTERDAY,
                        postponed = true
                    )
                    setVisibilityOnStateSuccess(
                        progressBarBeforeYesterday,
                        cardApodExampleBeforeYesterday
                    )

                    addValuesToMapOfArguments(
                        DATE_TYPE_BEFORE_YESTERDAY,
                        state.apodBeforeYesterday.hdurl,
                        state.apodBeforeYesterday.explanation
                    )
                }
            }

            is SharedElementTransitionState.YesterdayError -> {
                view?.showSnackWithoutAction(state.error.message.toString())
            }

            else -> {}
        }
    }

    private fun setOnImageClickNavigation(
        image: ImageView,
        date: TextView,
        title: TextView,
        dateType: String,
        postponed: Boolean = false,
        explanationView: TextView = binding.tvEmptyExplanation
    ) {

        image.setOnClickListener {
            mapOfArguments[SharedElementTransitionDetailsFragment.KEY_DATE_TYPE] = dateType
            mapOfArguments[SharedElementTransitionDetailsFragment.KEY_IS_POSTPONED_TRANSITION] =
                postponed

            val extras = FragmentNavigatorExtras(
                image to getString(R.string.transition_apod_details),
                date to getString(R.string.transition_details_date),
                title to getString(R.string.transition_details_title),
                explanationView to getString(R.string.transition_details_explanation)
            )

            findNavController().navigate(
                R.id.action_apodExampleFragment_to_apodExampleDetailsFragment,
                bundleOf(SharedElementTransitionDetailsFragment.KEY_ARGUMENTS_MAP to mapOfArguments),
                null,
                extras
            )
        }
    }

    private fun addValuesToMapOfArguments(dateType: String, hdurl: String, explanation: String) =
        with(binding) {
            when (dateType) {
                DATE_TYPE_TODAY -> {
                    mapOfArguments[KEY_TODAY_DATE] = tvApodExampleTodayDate.text
                    mapOfArguments[KEY_TODAY_TITLE] = tvApodExampleTodayTitle.text
                    mapOfArguments[KEY_TODAY_HDURL] = hdurl
                    mapOfArguments[KEY_TODAY_EXPLANATION] = explanation
                }

                DATE_TYPE_YESTERDAY -> {
                    mapOfArguments[KEY_YESTERDAY_DATE] = tvApodExampleYesterdayDate.text
                    mapOfArguments[KEY_YESTERDAY_TITLE] = tvApodExampleYesterdayTitle.text
                    mapOfArguments[KEY_YESTERDAY_HDURL] = hdurl
                    mapOfArguments[KEY_YESTERDAY_EXPLANATION] = explanation
                }

                DATE_TYPE_BEFORE_YESTERDAY -> {
                    mapOfArguments[KEY_BEFORE_YESTERDAY_DATE] =
                        tvApodExampleBeforeYesterdayDate.text
                    mapOfArguments[KEY_BEFORE_YESTERDAY_TITLE] =
                        tvApodExampleBeforeYesterdayTitle.text
                    mapOfArguments[KEY_BEFORE_YESTERDAY_HDURL] = hdurl
                    mapOfArguments[KEY_BEFORE_YESTERDAY_EXPLANATION] = explanation
                }
            }
        }
}