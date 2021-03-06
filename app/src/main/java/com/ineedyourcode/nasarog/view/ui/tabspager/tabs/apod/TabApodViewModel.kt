package com.ineedyourcode.nasarog.view.ui.tabspager.tabs.apod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.dto.apoddto.PictureOfTheDayDto
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabApodViewModel(
    private val liveData: MutableLiveData<TabApodState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): LiveData<TabApodState> = liveData

    fun getPictureOfTheDayRequest(date: String = "") {
        liveData.postValue(TabApodState.Loading)
        retrofitRepository.getPictureOfTheDay(date,
            object : Callback<PictureOfTheDayDto> {
                override fun onResponse(
                    call: Call<PictureOfTheDayDto>,
                    response: Response<PictureOfTheDayDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveData.postValue(TabApodState.Success(it))
                        }
                    } else {
                        liveData.postValue(TabApodState.Error(NullPointerException("Пустой ответ сервера")))
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    liveData.postValue(TabApodState.Error(Throwable("Ошибка связи с сервером")))
                }
            }
        )
    }
}