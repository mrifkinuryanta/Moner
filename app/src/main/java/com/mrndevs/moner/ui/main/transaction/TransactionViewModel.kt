package com.mrndevs.moner.ui.main.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.model.Parent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel(),
    TransactionContract.ViewModel {

    private val dateLiveData = MutableLiveData<Date>()
    private val recordsLiveData = MutableLiveData<Resource<List<Parent>>>()

    override fun getDateLiveData(): LiveData<Date> = dateLiveData
    override fun getRecordsLiveData(): LiveData<Resource<List<Parent>>> = recordsLiveData

    override fun getMonthAndYear() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDate().collect { value ->
                viewModelScope.launch(Dispatchers.Main) { dateLiveData.value = value }
            }
        }
    }

    override fun saveMonthAndYearQuery(month: String, year: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveMonthAndYearQuery(month, year)
        }
    }

    override fun getRecords(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val parentList = ArrayList<Parent>()
                val result = repository.getRecordsDate(date)
                result.forEach {
                    val list = repository.getRecordsByDay(it.date).reversed()
                    parentList.add(Parent(it.date, list))
                }
                viewModelScope.launch(Dispatchers.Main) {
                    recordsLiveData.value = Resource.Success(parentList)
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    recordsLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }
}