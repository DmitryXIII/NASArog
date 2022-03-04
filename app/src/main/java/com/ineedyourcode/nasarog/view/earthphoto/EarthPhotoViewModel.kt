package com.ineedyourcode.nasarog.view.earthphoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoDateDto
import com.ineedyourcode.nasarog.remoterepo.dto.earthphotodto.EarthPhotoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class EarthPhotoViewModel(
    private val liveData: MutableLiveData<EarthPhotoState> = MutableLiveData(),
    private val retrofitRepository: NasaRepository = NasaRepository()
) : ViewModel() {

    fun getLiveData(): LiveData<EarthPhotoState> {
        return liveData
    }

    fun getEarthPhotoRequest() {
        liveData.postValue(EarthPhotoState.Loading(null))
        retrofitRepository.getNasaRepository().getEarthPhotoDates()
            .enqueue(object : Callback<List<EarthPhotoDateDto>> {
                override fun onResponse(
                    call: Call<List<EarthPhotoDateDto>>,
                    response: Response<List<EarthPhotoDateDto>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let { listOfDates ->
                            retrofitRepository.getNasaRepository()
                                .getEarthPhoto(listOfDates[0].date)
                                .enqueue(object :
                                    Callback<List<EarthPhotoItem>> {
                                    override fun onResponse(
                                        call: Call<List<EarthPhotoItem>>,
                                        response: Response<List<EarthPhotoItem>>
                                    ) {
                                        if (response.isSuccessful && response.body() != null) {
                                            response.body()?.let {
                                                liveData.postValue(EarthPhotoState.Success(it[0]))
                                            }
                                        } else {
                                            liveData.postValue(
                                                EarthPhotoState.Error(
                                                    NullPointerException("Пустой ответ сервера")
                                                )
                                            )
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<List<EarthPhotoItem>>,
                                        t: Throwable
                                    ) {

                                    }
                                })
                        }

                    }
                }

                override fun onFailure(call: Call<List<EarthPhotoDateDto>>, t: Throwable) {

                }

            })
    }
}