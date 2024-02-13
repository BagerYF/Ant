package com.example.ant.model.country

data class CountryModel(
    var name: String,
    var code: String,
    var flagPath: String? = null,
    var currencyCode: String? = null,
    var provinces: MutableList<CountryModel>? = null,
    var firstCharacter: String,
    var isCharacter: Boolean
)

class AllCountryModel : ArrayList<CountryModel>()