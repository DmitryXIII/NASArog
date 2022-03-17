package com.ineedyourcode.nasarog.view.tabs.photo.picoftheday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.remoterepo.dto.PictureOfTheDayDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<PictureOfTheDayState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): LiveData<PictureOfTheDayState> = liveData

    fun getPictureOfTheDayRequest(date: String = "") {
        liveData.postValue(PictureOfTheDayState.Loading(null))
        retrofitRepository.getPictureOfTheDay(date,
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveData.postValue(PictureOfTheDayState.Success(it))
                        }
                    } else {
                        liveData.postValue(PictureOfTheDayState.Error(NullPointerException("Пустой ответ сервера")))
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveData.postValue(PictureOfTheDayState.Error(Throwable("Ошибка связи с сервером")))
                }
            }
        )
    }
}