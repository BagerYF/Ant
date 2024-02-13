package com.example.ant.model.home

data class HomeModel(
    var name: String,
    var items: List<HomeItem>,
    var subTitle: String,
    var title: String
)

data class HomeItem(
    var text: String? = null,
    var absoluteMobileImageUrl: String? = null,
    var brand: String? = null,
    var productName: String? = null
)

class AllHomeModel : ArrayList<HomeModel>()