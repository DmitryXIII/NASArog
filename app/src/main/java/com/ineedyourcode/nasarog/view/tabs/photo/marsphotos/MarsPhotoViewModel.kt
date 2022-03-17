package com.ineedyourcode.nasarog.view.tabs.photo.marsphotos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.nasarog.remoterepo.INasaRepository
import com.ineedyourcode.nasarog.remoterepo.NasaRepository
import com.ineedyourcode.nasarog.remoterepo.dto.marsphotodto.MarsDto
import com.ineedyourcode.nasarog.utils.getCurrentDate
import com.ineedyourcode.nasarog.utils.getPreviousDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsPhotoViewModel(
    private val liveData: MutableLiveData<MarsPhotoState> = MutableLiveData(),
    private val retrofitRepository: INasaRepository = NasaRepository()
) : ViewModel() {

    private var requestDate = getCurrentDate()
    private var dateIndex = 0

    fun getLiveData(): MutableLiveData<MarsPhotoState> = liveData

    fun getMarsPhotoRequest() {
        liveData.postValue(MarsPhotoState.Loading)
        retrofitRepository.getMarsPhoto(requestDate, object : Callback<MarsDto> {
            override fun onResponse(call: Call<MarsDto>, response: Response<MarsDto>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        // если на текущую дату нет фотогорафий,
                        // то идет поиск ближайшей предыдущей даты с фотографиями
                        if (it.photos.isEmpty()) {
                            dateIndex--
                            requestDate = getPreviousDate(dateIndex)
                            getMarsPhotoRequest()
                        } else {
                            liveData.postValue(MarsPhotoState.MarsPhotoSuccess(it))
                        }
                    }
                } else {
                    liveData.postValue(MarsPhotoState.Error(NullPointerException("Пустой ответ сервера")))
                }
            }

            override fun onFailure(call: Call<MarsDto>, t: Throwable) {
                liveData.postValue(MarsPhotoState.Error(NullPointerException("Ошибка связи с сервером")))
            }
        })
    }
}