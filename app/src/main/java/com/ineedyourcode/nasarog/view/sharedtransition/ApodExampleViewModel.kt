package com.ineedyourcode.nasarog.view.sharedtransition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.utils.getBeforeYesterdayDate
import com.ineedyourcode.nasarog.utils.getYesterdayDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApodExampleViewModel(
    private val liveDataToday: MutableLiveData<ApodExampleState> = MutableLiveData(),
    private val liveDataYesterday: MutableLiveData<ApodExampleState> = MutableLiveData(),
    private val liveDataBeforeYesterday: MutableLiveData<ApodExampleState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveDataToday(): LiveData<ApodExampleState> = liveDataToday
    fun getLiveDataYesterday(): LiveData<ApodExampleState> = liveDataYesterday
    fun getLiveDataBeforeYesterday(): LiveData<ApodExampleState> = liveDataBeforeYesterday

    fun getApod() {
        getApodToday()
        getApodYesterday()
        getApodBeforeYesterday()
    }

     private fun getApodToday() {
        liveDataToday.postValue(ApodExampleState.TodayLoading)
        retrofitRepository.getPictureOfTheDay("",
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveDataToday.postValue(ApodExampleState.TodaySuccess(it))
                        }
                    } else {
                        liveDataToday.postValue(ApodExampleState.TodayError(NullPointerException("Пустой ответ сервера")))
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveDataToday.postValue(ApodExampleState.TodayError(Throwable("Ошибка связи с сервером")))
                }
            })
    }

    private fun getApodYesterday() {
        liveDataYesterday.postValue(ApodExampleState.YesterdayLoading)
        retrofitRepository.getPictureOfTheDay(
            getYesterdayDate(),
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveDataYesterday.postValue(ApodExampleState.YesterdaySuccess(it))
                        }
                    } else {
                        liveDataYesterday.postValue(
                            ApodExampleState.YesterdayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveDataYesterday.postValue(ApodExampleState.YesterdayError(Throwable("Ошибка связи с сервером")))
                }
            })
    }

    private fun getApodBeforeYesterday() {
        liveDataBeforeYesterday.postValue(ApodExampleState.BeforeYesterdayLoading)
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
                                ApodExampleState.BeforeYesterdaySuccess(
                                    it
                                )
                            )
                        }
                    } else {
                        liveDataBeforeYesterday.postValue(
                            ApodExampleState.BeforeYesterdayError(
                                NullPointerException("Пустой ответ сервера")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveDataBeforeYesterday.postValue(
                        ApodExampleState.BeforeYesterdayError(
                            Throwable("Ошибка связи с сервером")
                        )
                    )
                }
            })
    }
}