package com.ineedyourcode.nasarog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.BuildConfig
import com.ineedyourcode.nasarog.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.remoterepo.PictureOfTheDayDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<PictureOfTheDayState> = MutableLiveData(),
    private val retrofitRepository: NasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): LiveData<PictureOfTheDayState> {
        return liveData
    }

    fun getPictureOfTheDayRequest() {
        liveData.postValue(PictureOfTheDayState.Loading(null))
        retrofitRepository.getNasaRepository().getPictureOfTheDay(BuildConfig.NASA_API_KEY).enqueue(
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
                        //TODO
                    }
                }

                override fun onFailure(call: Call<PictureOfTheDayDto>, t: Throwable) {
                    //TODO
                }
            }
        )
    }
}