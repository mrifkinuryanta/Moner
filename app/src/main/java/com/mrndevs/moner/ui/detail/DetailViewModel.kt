package com.mrndevs.moner.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailRepository) : ViewModel(),
    DetailContract.ViewModel {

    private val repositoryLiveData = MutableLiveData<Resource<Pair<Int, RecordEntity>>>()
    override fun getRecordLiveData(): LiveData<Resource<Pair<Int, RecordEntity>>> =
        repositoryLiveData

    override fun getRecordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getRecordById(id)
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Success(Pair(Constant.CODE_READ, result))
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }

    override fun deleteRecord(record: RecordEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteRecord(record)
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Success(Pair(Constant.CODE_DELETE, record))
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }
}