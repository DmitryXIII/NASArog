package com.ineedyourcode.nasarog.view.ui.features.sharedelementtransition.stable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.dto.apoddto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.utils.getBeforeYesterdayDate
import com.ineedyourcode.nasarog.utils.getYesterdayDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedElementTransitionViewModel(
    private val liveDataToday: MutableLiveData<SharedElementTransitionState> = MutableLiveData(),
    private val liveDataYesterday: MutableLiveData<SharedElementTransitionState> = MutableLiveData(),
    private val liveDataBeforeYesterday: MutableLiveData<SharedElementTransitionState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveDataToday(): LiveData<SharedElementTransitionState> = liveDataToday
    fun getLiveDataYesterday(): LiveData<SharedElementTransitionState> = liveDataYesterday
    fun getLiveDataBeforeYesterday(): LiveData<SharedElementTransitionState> = liveDataBeforeYesterday

    fun getApod() {
        getApodToday()
        getApodYesterday()
        getApodBeforeYesterday()
    }

     private fun getApodToday() {
        liveDataToday.postValue(SharedElementTransitionState.TodayLoading)
        retrofitRepository.getPictureOfTheDay("",
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveDataToday.postValue(SharedElementTransitionState.TodaySuccess(it))
                        }
                    } else {
                        liveDataToday.postValue(
                            SharedElementTransitionState.TodayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveDataToday.postValue(SharedElementTransitionState.TodayError(Throwable("Ошибка связи с сервером")))
                }
            })
    }

    private fun getApodYesterday() {
        liveDataYesterday.postValue(SharedElementTransitionState.YesterdayLoading)
        retrofitRepository.getPictureOfTheDay(
            getYesterdayDate(),
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveDataYesterday.postValue(
                                SharedElementTransitionState.YesterdaySuccess(
                                    it
                                )
                            )
                        }
                    } else {
                        liveDataYesterday.postValue(
                            SharedElementTransitionState.YesterdayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveDataYesterday.postValue(
                        SharedElementTransitionState.YesterdayError(
                            Throwable("Ошибка связи с сервером")
                        )
                    )
                }
            })
    }

    private fun getApodBeforeYesterday() {
        liveDataBeforeYesterday.postValue(SharedElementTransitionState.BeforeYesterdayLoading)
        retrofitRepository.getPictureOfTheDay(
            getBeforeYesterdayDate(),
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveDataBeforeYesterday.postValue(
                                SharedElementTransitionState.BeforeYesterdaySuccess(
                                    it
                                )
                            )
                        }
                    } else {
                        liveDataBeforeYesterday.postValue(
                            SharedElementTransitionState.BeforeYesterdayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveDataBeforeYesterday.postValue(
                        SharedElementTransitionState.BeforeYesterdayError(
                            Throwable("Ошибка связи с сервером")
                        )
                    )
                }
            })
    }
}