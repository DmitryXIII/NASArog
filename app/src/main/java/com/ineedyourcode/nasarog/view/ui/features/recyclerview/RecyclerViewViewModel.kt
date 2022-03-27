package com.ineedyourcode.nasarog.view.ui.features.recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.dto.asteroidsdto.AsteroidDto
import com.ineedyourcode.nasarog.model.dto.marsphotodto.MarsDto
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.utils.getCurrentDate
import com.ineedyourcode.nasarog.utils.getPreviousDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewViewModel(
    private val liveData: MutableLiveData<AsteroidDataState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    private var requestDate = getCurrentDate()

    fun getLiveData(): MutableLiveData<AsteroidDataState> = liveData

    fun getaAsteroidsDataRequest() {
        liveData.postValue(AsteroidDataState.Loading)
        retrofitRepository.getAsteroidsData(requestDate, requestDate, object : Callback<List<AsteroidDto>> {
            override fun onResponse(call: Call<List<AsteroidDto>>, response: Response<List<AsteroidDto>>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        liveData.postValue(AsteroidDataState.AsteroidDataSuccess(it))
                    }
                } else {
                    liveData.postValue(AsteroidDataState.Error(NullPointerException("Пустой ответ сервера")))
                }
            }

            override fun onFailure(call: Call<List<AsteroidDto>>, t: Throwable) {
                liveData.postValue(AsteroidDataState.Error(NullPointerException("Ошибка связи с сервером")))
            }
        })
    }
}