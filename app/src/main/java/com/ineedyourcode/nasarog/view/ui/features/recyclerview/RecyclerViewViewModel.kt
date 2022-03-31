package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidListDto
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.utils.ITEM_TYPE_HAZARDOUS
import com.ineedyourcode.nasarog.utils.ITEM_TYPE_HEADER
import com.ineedyourcode.nasarog.utils.ITEM_TYPE_UNHAZARDOUS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewViewModel(
    private val liveData: MutableLiveData<AsteroidDataState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    private var requestCounter = 0
    private lateinit var dateStart: String
    private lateinit var dateEnd: String
    private lateinit var header: String

    fun getLiveData(): MutableLiveData<AsteroidDataState> = liveData

    fun getAsteroidsDataRequest(dateStart: String, dateEnd: String, header: String) {
        this.dateStart = dateStart
        this.dateEnd = dateEnd
        this.header = header

        liveData.postValue(AsteroidDataState.Loading)
        retrofitRepository.getAsteroidsData(
            dateStart,
            dateEnd,
            this.asteroidDataCallback
        )
    }

    private val asteroidDataCallback = object : Callback<AsteroidListDto> {
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
                    requestCounter = 0
                }
            } else {
                liveData.postValue(AsteroidDataState.Error(NullPointerException("Пустой ответ сервера")))
            }
        }

        override fun onFailure(call: Call<AsteroidListDto>, t: Throwable) {
            if (requestCounter < 1) {
                requestCounter++
                retrofitRepository.getAsteroidsData(dateStart, dateEnd, this)
            } else {
                liveData.postValue(AsteroidDataState.Error(NullPointerException("Ошибка связи с сервером")))
            }
        }
    }
}
