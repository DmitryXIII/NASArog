package com.ineedyourcode.nasarog.view.ui.tabspager.tabs.earthphoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.model.dto.earthphotodto.EarthPhotoDto
import com.ineedyourcode.nasarog.model.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.model.remoterepo.NasaRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabEarthPhotoViewModel(
    private val liveData: MutableLiveData<TabEarthPhotoState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): LiveData<TabEarthPhotoState> {
        return liveData
    }

    fun getEarthPhotoDatesRequest() {
        liveData.postValue(TabEarthPhotoState.Loading)
        retrofitRepository.getEarthPhotoDates(object : Callback<List<EarthPhotoDateDto>> {
            override fun onResponse(
                call: Call<List<EarthPhotoDateDto>>,
                response: Response<List<EarthPhotoDateDto>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { listOfDates ->
                        liveData.postValue(TabEarthPhotoState.DatesSuccess(listOfDates))
                    }
                } else {
                    liveData.postValue(TabEarthPhotoState.Error(NullPointerException("Пустой ответ сервера")))
                }
            }

            override fun onFailure(call: Call<List<EarthPhotoDateDto>>, t: Throwable) {
                liveData.postValue(TabEarthPhotoState.Error(Throwable("Ошибка связи с сервером")))
            }
        })
    }

    fun getEarthPhotoRequest(date: String) {
        liveData.postValue(TabEarthPhotoState.Loading)
        retrofitRepository.getEarthPhoto(date,
            object : Callback<List<EarthPhotoDto>> {
                override fun onResponse(
                    call: Call<List<EarthPhotoDto>>,
                    response: Response<List<EarthPhotoDto>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let {
                            liveData.postValue(TabEarthPhotoState.PhotoSuccess(it[0]))
                        }
                    } else {
                        liveData.postValue(TabEarthPhotoState.Error(NullPointerException("Пустой ответ сервера")))
                    }
                }

                override fun onFailure(call: Call<List<EarthPhotoDto>>, t: Throwable) {
                    liveData.postValue(TabEarthPhotoState.Error(Throwable("Ошибка связи с сервером")))
                }
            })
    }
}
