package com.mrndevs.moner.ui.recordform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.utils.Constant
import com.mrndevs.moner.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordFormViewModel(private val repository: RecordFormRepository) : ViewModel(),
    RecordFormContract.ViewModel {

    private val repositoryLiveData = MutableLiveData<Resource<Pair<Int, RecordEntity>>>()
    override fun getResultLiveData(): LiveData<Resource<Pair<Int, RecordEntity>>> =
        repositoryLiveData

    override fun getRecordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getRecordById(id)
                if (result.id != 0) {
                    viewModelScope.launch(Dispatchers.Main) {
                        repositoryLiveData.value =
                            Resource.Success(Pair(Constant.CODE_READ, result))
                    }
                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                        repositoryLiveData.value = Resource.Error(result.toString())
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }

    override fun insertRecord(record: RecordEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.insertRecord(record)
                if (result > 0) {
                    viewModelScope.launch(Dispatchers.Main) {
                        repositoryLiveData.value =
                            Resource.Success(Pair(Constant.CODE_INSERT, Utils.recordEmpty()))
                    }
                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                        repositoryLiveData.value = Resource.Error(result.toString())
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }

    override fun updateRecord(record: RecordEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.updateRecord(record)
                if (result > 0) {
                    viewModelScope.launch(Dispatchers.Main) {
                        repositoryLiveData.value =
                            Resource.Success(Pair(Constant.CODE_UPDATE, Utils.recordEmpty()))
                    }
                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                        repositoryLiveData.value = Resource.Error(result.toString())
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }
}