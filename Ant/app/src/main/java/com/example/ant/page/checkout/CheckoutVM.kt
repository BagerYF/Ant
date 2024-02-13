package com.example.ant.page.checkout

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.CheckoutCompleteFreeMutation
import com.example.CheckoutCreateMutation
import com.example.CheckoutDiscountCodeApplyV2Mutation
import com.example.CheckoutDiscountCodeRemoveMutation
import com.example.CheckoutShippingAddressUpdateV2Mutation
import com.example.CheckoutShippingLineUpdateMutation
import com.example.CheckoutShippingLinesQuery
import com.example.CustomerAddressCreateMutation
import com.example.CustomerAddressUpdateMutation
import com.example.CustomerQuery
import com.example.GetProductByIdQuery
import com.example.QueryCartQuery
import com.example.ant.R
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.tools.TOKEN
import com.example.ant.common.tools.readJSONFromAssets
import com.example.ant.model.country.AllCountryModel
import com.example.ant.model.country.CountryModel
import com.example.ant.model.input.InputModel
import com.example.ant.page.cart.CartVM
import com.example.ant.server.Network
import com.example.fragment.CartCommon
import com.example.fragment.CheckoutCommon
import com.example.type.CheckoutCreateInput
import com.example.type.CheckoutLineItemInput
import com.example.type.MailingAddressInput
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Base64
import javax.inject.Inject

data class CheckoutState(
    var updateTime: String = "",
    var currentPageIndex: Int = -1,
    var addressInputList: MutableList<MutableList<InputModel>> = mutableListOf(),
    var countryData: MutableList<CountryModel> = mutableListOf(),
    var checkout: CheckoutCommon? = null,
    var shippingRates: MutableList<CheckoutShippingLinesQuery.ShippingRate> = mutableListOf(),
    var shippingLine: CheckoutShippingLineUpdateMutation.ShippingLine? = null,
    var creditInputModel: InputModel = InputModel(
        title = "Gift card or discount code",
        text = "",
        hideTitle = true,
    ),
    var discountCode: String = "",
    var isLoading: Boolean = true
)

@HiltViewModel
class CheckoutVM @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _checkoutState = MutableStateFlow(CheckoutState())

    var checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    var backCallBack: (() -> Unit)? = null

    var countryList: MutableList<CountryModel> = mutableListOf()

    var selectCountry: CountryModel

    var selectProvince: CountryModel? = null

    var token = ""

    private var cartEncoder: String? = savedStateHandle["params"]

    private val countryData = readJSONFromAssets(
        context = application.applicationContext, path = "country_data.json"
    )

    private var dataStoreManager = DataStoreManager(context = application.applicationContext)

    init {
        countryList = Gson().fromJson(countryData, AllCountryModel::class.java)

        countryList.filter { it ->
            it.code == "CN"
        }.toList().first().also { selectCountry = it }

        viewModelScope.launch {
            _checkoutState.update { currentState ->
                currentState.copy(
                    addressInputList = mutableListOf(
                        mutableListOf(
                            InputModel(
                                title = "First Name",
                                text = "zzz"
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Last Name",
                                text = "zzz"
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Email",
                                text = "zzz1@gmail.com"
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Country",
                                text = selectCountry.name,
                                leftImg = "https://d1mp1ehq6zpjr9.cloudfront.net/static/images/flags/${selectCountry.code}.png",
                                rightImg = R.drawable.arrow_down
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Address",
                                text = "zzz"
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Apartment,Suite,Unit,Building",
                                text = "zzz",
                                required = false,
                                optional = true
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "City",
                                text = "zzz"
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "State",
                                text = "Anhui",
                                rightImg = R.drawable.arrow_down
                            ),
                            InputModel(
                                title = "Postcode / Zipcode",
                                text = "999999"
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Phone",
                                text = "9999999",
                                required = false
                            )
                        ),
                    )
                )
            }
        }

        viewModelScope.launch {
            val localValue = dataStoreManager.getFromDataStore(TOKEN).first()
            if (localValue.isNotEmpty()) {
                token = localValue
            }
        }

        initData()
    }

    private fun initData() {

        val cartString = String(Base64.getUrlDecoder().decode(cartEncoder))

        val cart: CartCommon = Gson().fromJson(cartString, CartCommon::class.java)

        val lineItems: MutableList<CheckoutLineItemInput> = mutableListOf()
        cart.lines.edges.map { item ->
            lineItems.add(
                CheckoutLineItemInput(
                    quantity = item.node.quantity,
                    variantId = item.node.merchandise.onProductVariant!!.id
                )
            )
        }

        viewModelScope.launch {
            try {
                val response = Network.shared.mutation(
                    CheckoutCreateMutation(
                        input = CheckoutCreateInput(
                            lineItems = Optional.present(
                                lineItems
                            )
                        )
                    )
                ).execute()

                response.data?.checkoutCreate?.let {
                    _checkoutState.update { currentState ->
                        currentState.copy(
                            checkout = it.checkout?.checkoutCommon,
                            currentPageIndex = 0,
                            isLoading = false
                        )
                    }
                    updateState()
                }

            } catch (e: ApolloException) {
                Log.w("Checkout Create", "Failed to create checkout", e)
            }
        }
    }

    fun submitClick() {

        if (_checkoutState.value.currentPageIndex == 0) {
            var hasError = false

            _checkoutState.update { currentState ->
                currentState.copy(addressInputList = _checkoutState.value.addressInputList.map { items ->
                    items
                        .map { item ->
                            if (item.required && item.text.isEmpty() && item.show) {
                                hasError = true
                                println(item.title + "show error")
                                item.copy(showError = true)
                            } else {
                                item
                            }
                        }
                        .toMutableList()
                }.toMutableList())
            }

            updateState()

            val addressInputList: MutableList<MutableList<InputModel>> =
                _checkoutState.value.addressInputList

            if (!hasError) {
                _checkoutState.update { currentState ->
                    currentState.copy(isLoading = true)
                }

                viewModelScope.launch {
                    try {
                        val response = Network.shared.mutation(
                            CheckoutShippingAddressUpdateV2Mutation(
                                checkoutId = _checkoutState.value.checkout!!.id,
                                shippingAddress = MailingAddressInput(
                                    address1 = Optional.present(addressInputList[4][0].text),
                                    address2 = Optional.present(addressInputList[5][0].text),
                                    city = Optional.present(addressInputList[6][0].text),
                                    country = Optional.present(addressInputList[3][0].text),
                                    firstName = Optional.present(addressInputList[0][0].text),
                                    lastName = Optional.present(addressInputList[1][0].text),
                                    phone = Optional.present(addressInputList[8][0].text),
                                    province = Optional.present(addressInputList[7][0].text),
                                    zip = Optional.present(addressInputList[7][1].text)
                                ),
                            )
                        ).execute()

                        response.data?.checkoutShippingAddressUpdateV2?.let {
                            _checkoutState.update { currentState ->
                                currentState.copy(checkout = it.checkout?.checkoutCommon)
                            }
                            updateState()
                            updateShippingRates()
                        }

                    } catch (e: ApolloException) {
                        Log.w("address", "Add address", e)
                    }
                }
            }
        } else if (_checkoutState.value.currentPageIndex == 1) {
            _checkoutState.update { currentState ->
                currentState.copy(
                    currentPageIndex = 2
                )
            }
            updateState()
        } else {
            checkoutCompleteFree()
        }
    }

    private fun updateShippingRates() {
        viewModelScope.launch {
            try {
                val response = Network.shared.query(
                    CheckoutShippingLinesQuery(
                        id = _checkoutState.value.checkout!!.id
                    )
                ).execute()

                if (response.data?.node?.onCheckout?.availableShippingRates?.ready == true) {
                    response.data?.node?.onCheckout?.let {
                        _checkoutState.update { currentState ->
                            currentState.copy(
                                shippingRates = it.availableShippingRates?.shippingRates!!.toMutableList(),
                                currentPageIndex = 1,
                                isLoading = false
                            )
                        }
                        updateState()
                    }
                } else {
                    println(response.data?.node?.onCheckout?.availableShippingRates?.ready)
                    updateShippingRates()
                }

            } catch (e: ApolloException) {
                Log.w("update rates", "Failed update rates", e)
            }
        }
    }

    fun updateShippingLine(index: Int) {
        val tempShippingLine = _checkoutState.value.shippingRates[index]

        _checkoutState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                val response = Network.shared.mutation(
                    CheckoutShippingLineUpdateMutation(
                        checkoutId = _checkoutState.value.checkout!!.id,
                        shippingRateHandle = tempShippingLine.handle
                    )
                ).execute()

                response.data?.checkoutShippingLineUpdate?.let {
                    _checkoutState.update { currentState ->
                        currentState.copy(
                            checkout = it.checkout?.checkoutCommon,
                            shippingLine = it.checkout?.shippingLine,
                            isLoading = false
                        )
                    }
                    updateState()
                }

            } catch (e: ApolloException) {
                Log.w("update shipping line", "Failed to update shipping line", e)
            }
        }
    }

    fun applyDiscountCode() {
        val code = _checkoutState.value.creditInputModel.text
        if (code.isNotEmpty()) {
            _checkoutState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch {
                try {
                    val response = Network.shared.mutation(
                        CheckoutDiscountCodeApplyV2Mutation(
                            checkoutId = _checkoutState.value.checkout!!.id,
                            discountCode = code
                        )
                    ).execute()

                    response.data?.checkoutDiscountCodeApplyV2?.let {
                        _checkoutState.update { currentState ->
                            currentState.copy(
                                checkout = it.checkout?.checkoutCommon,
                                discountCode = code,
                                creditInputModel = _checkoutState.value.creditInputModel.copy(text = ""),
                                isLoading = false
                            )
                        }
                        updateState()
                    }

                } catch (e: ApolloException) {
                    Log.w("Checkout apply discount code", "Failed to apply discount code", e)
                }
            }
        }
    }

    fun removeDiscountCode() {
        _checkoutState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                val response = Network.shared.mutation(
                    CheckoutDiscountCodeRemoveMutation(
                        checkoutId = _checkoutState.value.checkout!!.id,
                    )
                ).execute()

                response.data?.checkoutDiscountCodeRemove?.let {
                    _checkoutState.update { currentState ->
                        currentState.copy(
                            checkout = it.checkout?.checkoutCommon,
                            discountCode = "",
                            isLoading = false
                        )
                    }
                    updateState()
                }

            } catch (e: ApolloException) {
                Log.w("Checkout remove discount code", "Failed to remove discount code", e)
            }
        }
    }


    private fun checkoutCompleteFree() {
        if (_checkoutState.value.checkout?.totalPriceV2?.amount.toString().toDouble() == 0.0) {
            _checkoutState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch {
                try {
                    val response = Network.shared.mutation(
                        CheckoutCompleteFreeMutation(
                            checkoutId = _checkoutState.value.checkout!!.id,
                        )
                    ).execute()

                    response.data?.checkoutCompleteFree?.let {
                        _checkoutState.update { currentState ->
                            currentState.copy(
                                currentPageIndex = 3,
                                isLoading = false
                            )
                        }
                        updateState()
                    }

                } catch (e: ApolloException) {
                    Log.w("checkout free", "Failed to checkout", e)
                }
            }
        }
    }


    fun selectCountry(tempCountry: CountryModel) {
        if (tempCountry.flagPath != null) {
            selectCountry = tempCountry
            selectProvince = null

            _checkoutState.update { currentState ->
                currentState.copy(addressInputList = _checkoutState.value.addressInputList.map { items ->
                    items
                        .map { item ->
                            if (item.title == "Country") {
                                item.copy(
                                    text = selectCountry.name,
                                    leftImg = "https://d1mp1ehq6zpjr9.cloudfront.net/static/images/flags/${selectCountry.code}.png"
                                )
                            } else if (item.title == "State") {
                                item.copy(
                                    text = "",
                                    show = (tempCountry.provinces != null && tempCountry.provinces!!.isNotEmpty())
                                )
                            } else {
                                item
                            }
                        }
                        .toMutableList()
                }.toMutableList())
            }


        } else {
            selectProvince = tempCountry

            _checkoutState.update { currentState ->
                currentState.copy(addressInputList = _checkoutState.value.addressInputList.map { items ->
                    items
                        .map { item ->
                            if (item.title == "State") {
                                item.copy(text = selectProvince?.name ?: "", showError = false)
                            } else {
                                item
                            }
                        }
                        .toMutableList()
                }.toMutableList())
            }
        }
    }

    fun updateCountryData(countryData: MutableList<CountryModel>) {
        _checkoutState.update { currentState ->
            currentState.copy(countryData = countryData)
        }
    }

    private fun updateState() {
        _checkoutState.update { currentState ->
            currentState.copy(updateTime = LocalDateTime.now().toString())
        }
    }
}