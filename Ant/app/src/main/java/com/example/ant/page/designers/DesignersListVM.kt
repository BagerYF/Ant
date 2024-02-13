package com.example.ant.page.designers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.ant.common.tools.AppTools
import com.example.ant.model.designer.DesignerModel
import com.example.ant.model.designer.kDesignersAllMaps
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DesignersListVM @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel() {

    var allList: MutableList<DesignerModel> = mutableListOf()
    var showData: MutableMap<String, MutableList<DesignerModel>> = mutableMapOf()
    var group: MutableList<String> = mutableListOf()
    var searchText: String = ""

    init {
        val localData = kDesignersAllMaps.keys.toTypedArray().toList()

        for (item in localData) {
            val firstCharacter = item.first().uppercaseChar()
            val isCharacter = AppTools().containsOnlyLetters(firstCharacter)
            val tempModel = DesignerModel(name = item, firstCharacter = "$firstCharacter", isCharacter = isCharacter)
            allList.add(tempModel)
        }

        searchData()
    }

    fun searchData() {
        showData = mutableMapOf()
        for (designer in allList) {
            if (showData.keys.contains(designer.firstCharacter)) {
                var tempList: MutableList<DesignerModel> = showData[designer.firstCharacter]!!
                if (searchText.isNotEmpty()) {
                    if (designer.name.lowercase().contains(searchText.lowercase())) {
                        tempList.add(designer)
                    }
                } else {
                    tempList.add(designer)
                }
                showData[designer.firstCharacter] = tempList
            } else {
                if (designer.isCharacter) {
                    var tempList:MutableList<DesignerModel> = mutableListOf()
                    if (searchText.isNotEmpty()) {
                        if (designer.name.lowercase().contains(searchText.lowercase())) {
                            tempList.add(designer)
                            showData[designer.firstCharacter] = tempList
                        }
                    } else {
                        tempList.add(designer)
                        showData[designer.firstCharacter] = tempList
                    }
                } else {
                    if (showData.keys.contains("#")) {
                        var tempList: MutableList<DesignerModel> = showData["#"]!!
                        if (searchText.isNotEmpty()) {
                            if (designer.name.lowercase().contains(searchText.lowercase())) {
                                tempList.add(designer)
                            }
                        } else {
                            tempList.add(designer)
                        }
                        showData["#"] = tempList
                    } else {
                        var tempList: MutableList<DesignerModel> = mutableListOf();
                        if (searchText.isNotEmpty()) {
                            if (designer.name.lowercase().contains(searchText.lowercase())) {
                                tempList.add(designer)
                                showData["#"] = tempList
                            }
                        } else {
                            tempList.add(designer)
                            showData["#"] = tempList
                        }
                    }
                }
            }
        }

        group = mutableListOf()
        for (key in showData.keys) {
            group.add(key)
            group.sort()
        }
    }
}