package com.example.ant.page.search

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.tools.SEARCH_HISTORY
import com.example.ant.model.category.AllCategoryModel
import com.example.ant.model.category.CategoryModel
import com.example.ant.model.category.kCategoryMaps
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var allList: MutableList<CategoryModel> = mutableListOf()
    var historyList: MutableList<String> = mutableListOf()

    private var dataStoreManager = DataStoreManager(context = application.applicationContext)

    init {
        allList = Gson().fromJson(kCategoryMaps, AllCategoryModel::class.java)

        getHistoryData()
    }

    private fun getHistoryData() {
        historyList = mutableListOf()
        viewModelScope.launch {
            val localValue = dataStoreManager.getFromDataStore(SEARCH_HISTORY).first()
            if (localValue.isNotEmpty()) {
                historyList = Gson().fromJson(localValue, Array<String>::class.java).asList()
                    .toMutableList()
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            dataStoreManager.clearDataStore()
            getHistoryData()
        }
    }

    fun search(text: String) {
        if (text.isNotEmpty()) {
            viewModelScope.launch {
                dataStoreManager.saveToDataStore(key = SEARCH_HISTORY, value = text)
                getHistoryData()
            }
        }
    }
}