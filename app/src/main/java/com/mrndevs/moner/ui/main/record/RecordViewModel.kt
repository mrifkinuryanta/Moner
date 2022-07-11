package com.mrndevs.moner.ui.main.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.model.AmountMoney
import com.mrndevs.moner.data.model.Date
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.enum.EnumRecordType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel(private val repository: RecordRepository) : ViewModel(),
    RecordContract.ViewModel {

    private val dateLiveData = MutableLiveData<Date>()
    private val amountMoneyLiveData = MutableLiveData<Resource<AmountMoney>>()
    private val recordCategoryLiveData = MutableLiveData<Resource<List<RecordEntity>>>()

    override fun getDateLiveData(): LiveData<Date> = dateLiveData
    override fun getAmountMoneyLiveData(): LiveData<Resource<AmountMoney>> = amountMoneyLiveData
    override fun getRecordCategoryLiveData(): LiveData<Resource<List<RecordEntity>>> =
        recordCategoryLiveData

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

    override fun getAmountMoney(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val expense =
                    repository.getAmountRecordByType(EnumRecordType.EXPENSE.type, date).amount
                val income =
                    repository.getAmountRecordByType(EnumRecordType.INCOME.type, date).amount
                val total = income - expense
                viewModelScope.launch(Dispatchers.Main) {
                    amountMoneyLiveData.value =
                        Resource.Success(AmountMoney(expense, income, total))
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    amountMoneyLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }

    override fun getRecordCategory(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getRecordCategory(date)
                viewModelScope.launch(Dispatchers.Main) {
                    recordCategoryLiveData.value =
                        Resource.Success(result)
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    recordCategoryLiveData.value = Resource.Error(e.toString())
                }
            }
        }
    }
}