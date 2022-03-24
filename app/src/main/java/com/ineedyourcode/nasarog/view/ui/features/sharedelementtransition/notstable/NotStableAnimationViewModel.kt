package com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition.notstable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.model.dto.apoddto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.utils.getBeforeYesterdayDate
import com.ineedyourcode.nasarog.utils.getYesterdayDate
import com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition.stable.SharedElementTransitionState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotStableAnimationViewModel(
    private val liveData: MutableLiveData<SharedElementTransitionState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): LiveData<SharedElementTransitionState> = liveData

    fun getApod() {
        getApodToday()
        getApodYesterday()
        getApodBeforeYesterday()
    }

    private fun getApodToday() {
        liveData.postValue(SharedElementTransitionState.TodayLoading)
        retrofitRepository.getPictureOfTheDay("",
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveData.postValue(SharedElementTransitionState.TodaySuccess(it))
                        }
                    } else {
                        liveData.postValue(
                            SharedElementTransitionState.TodayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveData.postValue(SharedElementTransitionState.TodayError(Throwable("Ошибка связи с сервером")))
                }
            })
    }

    private fun getApodYesterday() {
        liveData.postValue(SharedElementTransitionState.YesterdayLoading)
        retrofitRepository.getPictureOfTheDay(
            getYesterdayDate(),
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveData.postValue(SharedElementTransitionState.YesterdaySuccess(it))
                        }
                    } else {
                        liveData.postValue(
                            SharedElementTransitionState.YesterdayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveData.postValue(
                        SharedElementTransitionState.YesterdayError(
                            Throwable("Ошибка связи с сервером")
                        )
                    )
                }
            })
    }

    private fun getApodBeforeYesterday() {
        liveData.postValue(SharedElementTransitionState.BeforeYesterdayLoading)
        retrofitRepository.getPictureOfTheDay(
            getBeforeYesterdayDate(),
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveData.postValue(
                                SharedElementTransitionState.BeforeYesterdaySuccess(it)
                            )
                        }
                    } else {
                        liveData.postValue(
                            SharedElementTransitionState.BeforeYesterdayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveData.postValue(
                        SharedElementTransitionState.BeforeYesterdayError(
                            Throwable("Ошибка связи с сервером")
                        )
                    )
                }
            })
    }
}