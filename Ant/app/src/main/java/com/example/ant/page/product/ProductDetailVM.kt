package com.example.ant.page.product

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.example.CollectionQuery
import com.example.CustomerQuery
import com.example.GetProductByIdQuery
import com.example.ProductRecommendationsQuery
import com.example.ant.server.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Base64
import javax.inject.Inject

data class ProductDetailState(
    var updateTime: String = "",
    var product: GetProductByIdQuery.Product? = null,
    var variantIndex: Int = 1,
    var recommendList: List<ProductRecommendationsQuery.ProductRecommendation>? = null
)

@HiltViewModel
class ProductDetailVM @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

//    var id = "gid://shopify/Product/7713246904542"
    var id = ""

    private var _productDetailState = MutableStateFlow(ProductDetailState())

    var productDetailState: StateFlow<ProductDetailState> = _productDetailState.asStateFlow()

    private var paramsId: String? = savedStateHandle["params"]

    init {
        id = String(Base64.getUrlDecoder().decode(paramsId))
        getProductDetail()
        getRecommendList()
    }

    private fun getProductDetail() {
        viewModelScope.launch {
            try {
                val response = Network.shared.query(
                    GetProductByIdQuery(id = id)
                ).execute()

                response.data?.product?.let {
                    _productDetailState.update { currentState ->
                        currentState.copy(product = it)
                    }
                    updateState()
                }

            } catch (e: ApolloException) {
                Log.w("Product Detail", "Failed to get detail", e)
            }
        }
    }

    private fun getRecommendList() {
        viewModelScope.launch {
            try {
                val response = Network.shared.query(
                    ProductRecommendationsQuery(productId = id)
                ).execute()

                response.data?.productRecommendations?.let {
                    _productDetailState.update { currentState ->
                        currentState.copy(recommendList = it)
                    }
                    updateState()
                }

            } catch (e: ApolloException) {
                Log.w("Product Detail Recommend", "Failed to get detail", e)
            }
        }
    }

    fun sizeClick(index: Int) {
        val variant = _productDetailState.value.product!!.variants.edges[index].node
        if (variant.quantityAvailable!! > 0) {
            _productDetailState.update { currentState ->
                currentState.copy(variantIndex = index)
            }
        }
    }

    private fun updateState() {
        _productDetailState.update { currentState ->
            currentState.copy(updateTime = LocalDateTime.now().toString())
        }
    }
}