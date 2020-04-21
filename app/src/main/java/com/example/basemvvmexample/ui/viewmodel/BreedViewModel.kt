package com.example.basemvvmexample.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.basemvvmexample.data.api.ApiHelper
import com.example.basemvvmexample.data.api.RetrofitFactory
import com.example.basemvvmexample.data.api.response.Resource
import com.example.basemvvmexample.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedViewModel : BaseViewModel() {

    private val mainRepository: MainRepository

    private var _dogBreeds = MutableLiveData<Map<String, List<Any>>>()
    val dogBreeds = _dogBreeds

    init {
        // val breedDao = DogsRoomDatabase.getDatabase(application).breedDao()
        val apiHelper = ApiHelper(RetrofitFactory.getApiService())
        mainRepository = MainRepository(apiHelper)
    }

    fun getDogBreeds() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val dogBreedsResponse = mainRepository.getDogBreeds()
            _dogBreeds.postValue(dogBreedsResponse.body()?.message)
            emit(Resource.success(data = dogBreedsResponse))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun saveThatShit(list: List<String>?) {
        viewModelScope.launch(Dispatchers.IO) {
            // mainRepository.insert()
        }
    }
}
