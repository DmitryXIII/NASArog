package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.utils.getCurrentDate
import com.ineedyourcode.nasarog.utils.getTwoDaysForwardDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewViewModel(
    private val liveData: MutableLiveData<AsteroidDataState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): MutableLiveData<AsteroidDataState> = liveData

    fun getAsteroidsDataRequest() {
        liveData.postValue(AsteroidDataState.Loading)
        retrofitRepository.getAsteroidsData(getCurrentDate(), getTwoDaysForwardDate(), object : Callback<AsteroidListDto> {
            override fun onResponse(call: Call<AsteroidListDto>, response: Response<AsteroidListDto>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        val asteroidList = mutableListOf<AsteroidListDto.AsteroidDto>()
                        it.nearEarthObjects.forEach {(_, asteroidListByDate) ->
                            for (asteroid in asteroidListByDate) {
                                asteroidList.add(asteroid)
                            }
                        }
                            liveData.postValue(AsteroidDataState.AsteroidDataSuccess(asteroidList))
                    }
                } else {
                    liveData.postValue(AsteroidDataState.Error(NullPointerException("Пустой ответ сервера")))
                }
            }

            override fun onFailure(call: Call<AsteroidListDto>, t: Throwable) {
                liveData.postValue(AsteroidDataState.Error(NullPointerException("Ошибка связи с сервером")))
            }
        })
    }
}
