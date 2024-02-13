package com.example.ant.model.category

data class CategoryModel(
    var name: String,
    var children: List<CategoryModel>
)

class AllCategoryModel : ArrayList<CategoryModel>()
