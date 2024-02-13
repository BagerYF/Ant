package com.example.ant.model.product

import com.example.type.ProductCollectionSortKeys

val kProductSortMap = listOf(
    mapOf(
        "name" to "Most Relevant",
        "reverse" to false,
        "sortKey" to ProductCollectionSortKeys.RELEVANCE
    ),
    mapOf(
        "name" to "Alphabetically, A-Z",
        "reverse" to false,
        "sortKey" to ProductCollectionSortKeys.TITLE
    ),
    mapOf(
        "name" to "Alphabetically, Z-A",
        "reverse" to true,
        "sortKey" to ProductCollectionSortKeys.TITLE
    ),
    mapOf("name" to "New In", "reverse" to false, "sortKey" to ProductCollectionSortKeys.CREATED),
    mapOf(
        "name" to "Price, low to high",
        "reverse" to false,
        "sortKey" to ProductCollectionSortKeys.PRICE
    ),
    mapOf(
        "name" to "Price, high to low",
        "reverse" to true,
        "sortKey" to ProductCollectionSortKeys.PRICE
    ),
)

data class ProductSortModel(
    var name: String,
    var reverse: Boolean,
    var sortKey: ProductCollectionSortKeys
)

data class FilterModel(
    var variantOption: VariantModel? = null,
    var productVendor: String? = null,
    var available: Boolean? = null,
    var tag: String? = null,
    var productType: String? = null
)

data class VariantModel(
    var name: String,
    var value: String
)

class AllFilterModel : ArrayList<FilterModel>()