package com.example.ant.page.product

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.CollectionQuery
import com.example.ant.config.Constants
import com.example.ant.model.product.FilterModel
import com.example.ant.model.product.ProductSortModel
import com.example.ant.model.product.kProductSortMap
import com.example.ant.server.Network
import com.example.type.ProductCollectionSortKeys
import com.example.type.ProductFilter
import com.example.type.VariantOptionFilter
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

class ProductSource(
    var sort: ProductSortModel,
    var filters: MutableList<String>,
    var filterCallBack: ((filters: MutableList<CollectionQuery.Filter>) -> Unit)
) : PagingSource<String, CollectionQuery.Edge>() {

    var endCursor: String? = null

    override fun getRefreshKey(state: PagingState<String, CollectionQuery.Edge>): String {
        return state.anchorPosition.toString()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<String>): LoadResult<String, CollectionQuery.Edge> {
        return try {
            val pages = params.key ?: "1"
            val page = pages.toInt()
            var products: CollectionQuery.Products? = null
            try {
                var filterList: MutableList<ProductFilter> = mutableListOf()
                for (item in filters) {
                    val filterModel = Gson().fromJson(item, FilterModel::class.java)
                    if (filterModel.variantOption != null) {
                        filterList.add(
                            ProductFilter(
                                variantOption = Optional.present(VariantOptionFilter(
                                    name = filterModel.variantOption!!.name,
                                    value = filterModel.variantOption!!.value
                                ))
                            )
                        )
                    } else if (filterModel.productType != null) {
                        filterList.add(
                            ProductFilter(
                                productType = Optional.present(filterModel.productType)
                            )
                        )
                    } else if (filterModel.productVendor != null) {
                        filterList.add(
                            ProductFilter(
                                productVendor = Optional.present(filterModel.productVendor)
                            )
                        )
                    } else if (filterModel.tag != null) {
                        filterList.add(
                            ProductFilter(
                                tag = Optional.present(filterModel.tag)
                            )
                        )
                    } else if (filterModel.available != null) {
                        filterList.add(
                            ProductFilter(
                                available = Optional.present(filterModel.available)
                            )
                        )
                    }
                }

                val response = Network.shared.query(
                    CollectionQuery(
                        id = Constants.collectionGid,
                        first = 6,
                        after = Optional.present(endCursor),
                        reverse = Optional.present(sort.reverse),
                        sortKey = Optional.present(sort.sortKey),
                        filters = Optional.present(filterList)
                    )
                ).execute()

                response.data?.collection?.products.let {
                    products = it
                    val filters = it?.filters!!.toMutableList()
                    filterCallBack(filters)
                    endCursor = products?.pageInfo?.endCursor
                }

            } catch (e: ApolloException) {
                Log.w("Product", "Failed to get product list", e)
            }
            LoadResult.Page(
                data = products?.edges ?: listOf(),
                prevKey = null,
                nextKey = if (products?.pageInfo?.hasNextPage == true) (page + 1).toString() else null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}

data class FilterState(
    var updateTime: String = "",
    var allFilters: MutableList<CollectionQuery.Filter> = mutableListOf(),
    var subFiltersName: String = "",
    var subFilters: MutableList<CollectionQuery.Value> = mutableListOf(),
    var subFiltersIndex: Int = 0,
    var currentFilters: MutableList<String> = mutableListOf(),
)

@HiltViewModel
class ProductListVM @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private var _filterState = MutableStateFlow(FilterState())

    var filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    var selectSortModel = ProductSortModel(
        "Most Relevant",
        false,
        ProductCollectionSortKeys.RELEVANCE
    )

    var products: Flow<PagingData<CollectionQuery.Edge>> = Pager(PagingConfig(pageSize = 6)) {
        ProductSource(
            sort = selectSortModel,
            filters = _filterState.value.currentFilters,
            filterCallBack = {
                initFilters(it)
            })
    }.flow.cachedIn(viewModelScope)

    var sortList: MutableList<ProductSortModel> = mutableListOf()

    init {
        for (item in kProductSortMap) {
            val tempItem = ProductSortModel(
                name = item["name"].toString(),
                reverse = item["reverse"] as Boolean,
                sortKey = item["sortKey"] as ProductCollectionSortKeys
            )
            sortList.add(tempItem)
        }
    }

    private fun updateState() {
        _filterState.update { currentState ->
            currentState.copy(updateTime = LocalDateTime.now().toString())
        }
    }

    private fun initFilters(tempFilters: MutableList<CollectionQuery.Filter>) {
        if (_filterState.value.allFilters.isEmpty()) {
            if (tempFilters.isNotEmpty()) {
                val tempSubFilters = tempFilters.first()
                _filterState.update { currentSate ->
                    currentSate.copy(
                        allFilters = tempFilters,
                        subFiltersName = tempSubFilters.label,
                        subFilters = tempSubFilters.values.toMutableList()
                    )
                }
            } else {
                _filterState.update { currentSate ->
                    currentSate.copy(allFilters = tempFilters)
                }
            }
        } else {
            _filterState.update { currentSate ->
                currentSate.copy(allFilters = tempFilters)
            }

            if (_filterState.value.allFilters.count() > _filterState.value.subFiltersIndex) {
                _filterState.update { currentSate ->
                    currentSate.copy(subFilters = _filterState.value.allFilters[_filterState.value.subFiltersIndex].values.toMutableList())
                }
            }
        }
    }

    fun leftFilterClick(index: Int) {
        val item = _filterState.value.allFilters[index]
        _filterState.update { currentSate ->
            currentSate.copy(
                subFiltersName = item.label,
                subFiltersIndex = index,
                subFilters = _filterState.value.allFilters[index].values.toMutableList()
            )
        }
    }

    fun rightFilterClick(index: Int, selected: Boolean) {
        val tempSubFilters = _filterState.value.subFilters[index]
        var tempCurrentFilters = mutableListOf<String>()
        tempCurrentFilters.addAll(_filterState.value.currentFilters)
        if (selected) {
            tempCurrentFilters.add(tempSubFilters.input as String)
        } else {
            tempCurrentFilters.remove(tempSubFilters.input as String)
        }
        _filterState.update { currentSate ->
            currentSate.copy(
                currentFilters = tempCurrentFilters
            )
        }
        updateState()
    }

    fun checkFilterIsExist(filter: String): Boolean {
        return _filterState.value.currentFilters.contains(filter)
    }

    fun clearFilter() {
        _filterState.update { currentSate ->
            currentSate.copy(
                currentFilters = mutableListOf()
            )
        }
    }
}