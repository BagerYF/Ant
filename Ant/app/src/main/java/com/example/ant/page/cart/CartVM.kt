package com.example.ant.page.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.AddProductsToCartMutation
import com.example.CreateCartMutation
import com.example.QueryCartQuery
import com.example.RemoveProductFromCartMutation
import com.example.UpdateProductQuantityInCartMutation
import com.example.ant.common.tools.CART_ID
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.server.Network
import com.example.fragment.CartCommon
import com.example.type.AttributeInput
import com.example.type.CartInput
import com.example.type.CartLineInput
import com.example.type.CartLineUpdateInput
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class CartState(
    var updateTime: String = "",
    var cart: CartCommon? = null,
    var qtyVariantId: String = "",
    var qty: String = ""
)

@HiltViewModel
class CartVM @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _cartState = MutableStateFlow(CartState())

    var cartState: StateFlow<CartState> = _cartState.asStateFlow()

    var isGo = false

    private var dataStoreManager = DataStoreManager(context = application.applicationContext)

    init {
        viewModelScope.launch {
            val cartId = dataStoreManager.getFromDataStore(CART_ID).first()

            if (cartId.isNotEmpty()) {
                queryCart(cartId = cartId)
            }
        }
    }

    private fun queryCart(cartId: String) {
        println("222")
        viewModelScope.launch {
            try {
                val response = Network.shared.query(
                    QueryCartQuery(id = cartId)
                ).execute()

                response.data?.cart?.let {
                    _cartState.update { currentState ->
                        currentState.copy(cart = it.cartCommon)
                    }
                    updateState()
                }

            } catch (e: ApolloException) {
                Log.w("Cart", "Failed to get cart", e)
            }
        }
    }

    suspend fun addProductsToCart(variantId: String): Boolean {
        println(_cartState.value.cart?.id)
        return if (_cartState.value.cart != null) {
            addProductsToExistCart(variantId = variantId)
        } else {
            createCart(variantId = variantId)
        }
    }

    private suspend fun createCart(variantId: String): Boolean {
        val lines: MutableList<CartLineInput> = mutableListOf()
        lines.add(
            CartLineInput(
                attributes = Optional.present(
                    listOf<AttributeInput>(
                        AttributeInput(
                            key = "cart_attribute",
                            value = "This is a cart attribute"
                        )
                    )
                ), quantity = Optional.present(1), merchandiseId = variantId
            )
        )

        try {
            val response = Network.shared.mutation(
                CreateCartMutation(input = CartInput(lines = Optional.present(lines)))
            ).execute()

            response.data?.cartCreate?.cart?.let {
                _cartState.update { currentState ->
                    currentState.copy(cart = it.cartCommon)
                }
                updateState()

                viewModelScope.launch {
                    dataStoreManager.saveToDataStore(key = CART_ID, value = it.cartCommon.id)
                }
            }
            return true
        } catch (e: ApolloException) {
            Log.w("CartCreate", "Failed to create cart", e)
            return false
        }
    }

    private suspend fun addProductsToExistCart(variantId: String): Boolean {
        val lines: MutableList<CartLineInput> = mutableListOf()
        lines.add(
            CartLineInput(
                attributes = Optional.present(
                    listOf<AttributeInput>(
                        AttributeInput(
                            key = "cart_attribute",
                            value = "This is a cart attribute"
                        )
                    )
                ), quantity = Optional.present(1), merchandiseId = variantId
            )
        )

        try {
            val response = Network.shared.mutation(
                AddProductsToCartMutation(cartId = _cartState.value.cart?.id!!, lines = lines)
            ).execute()

            response.data?.cartLinesAdd?.cart?.let {
                _cartState.update { currentState ->
                    currentState.copy(cart = it.cartCommon)
                }
                updateState()
            }
            return true
        } catch (e: ApolloException) {
            Log.w("Add Product to Cart", "Failed to add product to cart", e)
            return false
        }
    }

    suspend fun updateProductQuantityInCart(quantity: Int) {
        val lines: MutableList<CartLineUpdateInput> = mutableListOf()
        lines.add(
            CartLineUpdateInput(
                attributes = Optional.present(
                    listOf<AttributeInput>(
                        AttributeInput(
                            key = "cart_attribute",
                            value = "This is a cart attribute"
                        )
                    )
                ), quantity = Optional.present(quantity), id = _cartState.value.qtyVariantId
            )
        )
        try {
            val response = Network.shared.mutation(
                UpdateProductQuantityInCartMutation(
                    cartId = _cartState.value.cart?.id!!,
                    lines = lines
                )
            ).execute()

            response.data?.cartLinesUpdate?.cart?.let {
                _cartState.update { currentState ->
                    currentState.copy(cart = it.cartCommon)
                }
                updateState()
            }

        } catch (e: ApolloException) {
            Log.w("Update quantity", "Failed to add update quantity", e)
        }
    }

    suspend fun removeProductFromCart(variantId: String) {
        val lines: List<String> = listOf(variantId)
        try {
            val response = Network.shared.mutation(
                RemoveProductFromCartMutation(
                    cartId = _cartState.value.cart?.id!!,
                    lineIds = lines
                )
            ).execute()

            response.data?.cartLinesRemove?.cart?.let {
                _cartState.update { currentState ->
                    currentState.copy(cart = it.cartCommon)
                }
                updateState()
            }

        } catch (e: ApolloException) {
            Log.w("Remove product", "Failed to add remove product", e)
        }

    }

    private fun updateState() {
        _cartState.update { currentState ->
            currentState.copy(updateTime = LocalDateTime.now().toString(), qty = getCartQty())
        }
    }

    fun updateQtyVariantId(variantId: String) {
        _cartState.update { currentState ->
            currentState.copy(qtyVariantId = variantId)
        }
    }

    private fun getCartQty(): String {
        val text: String
        val total = _cartState.value.cart?.totalQuantity ?: 0
        text = if (total == 0) {
            ""
        } else if (total > 99) {
            "99+";
        } else {
            "$total"
        }
        return text
    }

}