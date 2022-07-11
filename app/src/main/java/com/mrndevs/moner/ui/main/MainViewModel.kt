package com.mrndevs.moner.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrndevs.moner.base.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel(), MainContract.ViewModel {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val repositoryLiveData = MutableLiveData<Resource<String>>()
    override fun getResultLiveData(): LiveData<Resource<String>> = repositoryLiveData

    override fun saveMonthAndYearQuery(month: String, year: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.saveMonthAndYearQuery(month, year)
                _isLoading.value = false
            } catch (e: Exception) {
                repositoryLiveData.value = Resource.Error(e.toString())
                _isLoading.value = false
            }
        }
    }
}