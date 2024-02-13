package com.example.ant.page.wishlist

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GetProductByIdQuery
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.tools.WISHLIST
import com.example.ant.model.wishlist.WishlistModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class WishlistState(
    var updateTime: String = "",
    var wishlist: MutableList<WishlistModel> = mutableListOf()
)

@HiltViewModel
class WishlistVM @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _wishlistState = MutableStateFlow(WishlistState())

    var wishlistState: StateFlow<WishlistState> = _wishlistState.asStateFlow()

    var dataStoreManager = DataStoreManager(context = application.applicationContext)

    init {
        getWishlistData()
    }

    private fun getWishlistData() {
        viewModelScope.launch {
            val localValue = dataStoreManager.getFromDataStore(WISHLIST).first()
            if (localValue.isNotEmpty()) {
                val wishlist = Gson().fromJson(localValue, Array<WishlistModel>::class.java).asList()
                    .toMutableList()
                _wishlistState.update { currentState ->
                    currentState.copy(wishlist = wishlist)
                }
                updateState()
            }
        }
    }

    fun addWishlist(product: GetProductByIdQuery.Product, isAdd: Boolean = true) {

        val wishlistModel = WishlistModel(
            id = product.id,
            title = product.title,
            vendor = product.vendor,
            price = "${product.variants.edges.first().node.price.amount}",
            image = "${product.images.edges.first().node.url}"
        )

        val wishlist = _wishlistState.value.wishlist

        wishlist.remove(wishlistModel)

        if (isAdd) {
            wishlist.add(0, wishlistModel)
        }

        viewModelScope.launch {
            dataStoreManager.saveToDataStore(key = WISHLIST, value = Gson().toJson(wishlist))
            getWishlistData()
        }
    }

    fun removeWishlist(wishlistModel: WishlistModel) {

        val wishlist = _wishlistState.value.wishlist
        wishlist.remove(wishlistModel)

        viewModelScope.launch {
            dataStoreManager.saveToDataStore(key = WISHLIST, value = Gson().toJson(wishlist))
            getWishlistData()
        }
    }


    private fun updateState() {
        _wishlistState.update { currentState ->
            currentState.copy(updateTime = LocalDateTime.now().toString())
        }
    }
}