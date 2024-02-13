package com.example.ant.page.profile.address

import androidx.lifecycle.ViewModel
import com.example.ant.common.tools.AppTools
import com.example.ant.model.country.CountryModel

class CountryListVM(countryData: MutableList<CountryModel>) : ViewModel() {

    var allList: MutableList<CountryModel> = mutableListOf()
    var showData: MutableMap<String, MutableList<CountryModel>> = mutableMapOf()
    var group: MutableList<String> = mutableListOf()
    var searchText: String = ""

    init {

        for (item in countryData) {
            val firstCharacter = item.name.first().uppercaseChar()
            val isCharacter = AppTools().containsOnlyLetters(firstCharacter)
            allList.add(item.copy(firstCharacter = "$firstCharacter", isCharacter = isCharacter))
        }

        searchData()
    }

    fun searchData() {
        showData = mutableMapOf()
        for (designer in allList) {
            if (showData.keys.contains(designer.firstCharacter)) {
                var tempList: MutableList<CountryModel> = showData[designer.firstCharacter]!!
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
                    var tempList:MutableList<CountryModel> = mutableListOf()
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
                        var tempList: MutableList<CountryModel> = showData["#"]!!
                        if (searchText.isNotEmpty()) {
                            if (designer.name.lowercase().contains(searchText.lowercase())) {
                                tempList.add(designer)
                            }
                        } else {
                            tempList.add(designer)
                        }
                        showData["#"] = tempList
                    } else {
                        var tempList: MutableList<CountryModel> = mutableListOf();
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