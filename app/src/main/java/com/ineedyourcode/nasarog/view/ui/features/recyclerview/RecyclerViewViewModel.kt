package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewViewModel(
    private val liveData: MutableLiveData<AsteroidDataState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): MutableLiveData<AsteroidDataState> = liveData

    fun getAsteroidsDataRequest(dateStart: String, dateEnd: String, header: String) {
        liveData.postValue(AsteroidDataState.Loading)
        retrofitRepository.getAsteroidsData(
            dateStart,
            dateEnd,
            object : Callback<AsteroidListDto> {
                override fun onResponse(
                    call: Call<AsteroidListDto>,
                    response: Response<AsteroidListDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            val asteroidList = mutableListOf<AsteroidListDto.AsteroidDto>()
                            asteroidList.add(
                                AsteroidListDto.AsteroidDto(
                                    "0",
                                    header,
                                    0f,
                                    false,
                                    listOf(),
                                    ITEM_TYPE_HEADER
                                )
                            )
                            it.nearEarthObjects.forEach { (_, asteroidListByDate) ->
                                for (asteroid in asteroidListByDate) {
                                    asteroidList.add(asteroid.apply {
                                        when (isPotentiallyHazardousAsteroid) {
                                            true -> asteroid.type = ITEM_TYPE_HAZARDOUS
                                            false -> asteroid.type = ITEM_TYPE_UNHAZARDOUS
                                        }
                                    })
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
