package com.example.ant.page.profile.address

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.CustomerAddressCreateMutation
import com.example.CustomerAddressUpdateMutation
import com.example.CustomerQuery
import com.example.ant.R
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.tools.TOKEN
import com.example.ant.model.country.AllCountryModel
import com.example.ant.model.country.CountryModel
import com.example.ant.model.input.InputModel
import com.example.ant.server.Network
import com.example.type.MailingAddressInput
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class AddressState(
    var updateTime: String = "",
    var addressInputList: MutableList<MutableList<InputModel>> = mutableListOf(),
    var countryData: MutableList<CountryModel> = mutableListOf()
)

class AddressVM(
    var address: CustomerQuery.Edge? = null,
    countryData: String,
    var dataStoreManager: DataStoreManager,
) : ViewModel() {

    private var _addressState = MutableStateFlow(AddressState())

    var addressState: StateFlow<AddressState> = _addressState.asStateFlow()

    var backCallBack: (() -> Unit)? = null

    var countryList: MutableList<CountryModel> = mutableListOf()

    var selectCountry: CountryModel

    var selectProvince: CountryModel? = null

    var token = ""

    init {
        countryList = Gson().fromJson(countryData, AllCountryModel::class.java)

        if (address != null) {
            countryList.filter { it ->
                it.name == address!!.node.country
            }.toList().first().also { selectCountry = it }
            if (selectCountry.provinces != null) {
                selectCountry.provinces!!.filter { it ->
                    it.name == address!!.node.province
                }.toList().first().also { selectProvince = it }
            }
        } else {
            countryList.filter { it ->
                it.code == "CN"
            }.toList().first().also { selectCountry = it }
        }

        viewModelScope.launch {
            _addressState.update { currentState ->
                currentState.copy(
                    addressInputList = mutableListOf(
                        mutableListOf(
                            InputModel(
                                title = "First Name",
                                text = address?.node?.firstName ?: ""
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Last Name",
                                text = address?.node?.lastName ?: ""
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
                                text = address?.node?.address1 ?: ""
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Apartment,Suite,Unit,Building",
                                text = address?.node?.address2 ?: "",
                                required = false,
                                optional = true
                            )
                        ),
                        mutableListOf(InputModel(title = "City", text = address?.node?.city ?: "")),
                        mutableListOf(
                            InputModel(
                                title = "State",
                                text = address?.node?.province ?: "",
                                rightImg = R.drawable.arrow_down
                            ),
                            InputModel(
                                title = "Postcode / Zipcode",
                                text = address?.node?.zip ?: ""
                            )
                        ),
                        mutableListOf(
                            InputModel(
                                title = "Phone",
                                text = address?.node?.phone ?: "",
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
    }

    fun submitClick() {
        var hasError = false

        _addressState.update { currentState ->
            currentState.copy(addressInputList = _addressState.value.addressInputList.map { items ->
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
            _addressState.value.addressInputList

        if (!hasError) {

            if (address == null) {
                viewModelScope.launch {
                    try {
                        val response = Network.shared.mutation(
                            CustomerAddressCreateMutation(
                                address = MailingAddressInput(
                                    address1 = Optional.present(addressInputList[3][0].text),
                                    address2 = Optional.present(addressInputList[4][0].text),
                                    city = Optional.present(addressInputList[5][0].text),
                                    country = Optional.present(addressInputList[2][0].text),
                                    firstName = Optional.present(addressInputList[0][0].text),
                                    lastName = Optional.present(addressInputList[1][0].text),
                                    phone = Optional.present(addressInputList[7][0].text),
                                    province = Optional.present(addressInputList[6][0].text),
                                    zip = Optional.present(addressInputList[6][1].text)
                                ), customerAccessToken = token
                            )
                        ).execute()

                        backCallBack?.let { it() }

                    } catch (e: ApolloException) {
                        Log.w("address", "Add address", e)
                    }
                }
            } else {
                viewModelScope.launch {
                    try {
                        val response = Network.shared.mutation(
                            CustomerAddressUpdateMutation(
                                address = MailingAddressInput(
                                    address1 = Optional.present(addressInputList[3][0].text),
                                    address2 = Optional.present(addressInputList[4][0].text),
                                    city = Optional.present(addressInputList[5][0].text),
                                    country = Optional.present(addressInputList[2][0].text),
                                    firstName = Optional.present(addressInputList[0][0].text),
                                    lastName = Optional.present(addressInputList[1][0].text),
                                    phone = Optional.present(addressInputList[7][0].text),
                                    province = Optional.present(addressInputList[6][0].text),
                                    zip = Optional.present(addressInputList[6][1].text)
                                ), customerAccessToken = token, id = address!!.node.id
                            )
                        ).execute()

                        println(response.data)

                        backCallBack?.let { it() }

                    } catch (e: ApolloException) {
                        Log.w("address", "Update address", e)
                    }
                }
            }
        }
    }

    fun selectCountry(tempCountry: CountryModel) {
        if (tempCountry.flagPath != null) {
            selectCountry = tempCountry
            selectProvince = null

            _addressState.update { currentState ->
                currentState.copy(addressInputList = _addressState.value.addressInputList.map { items ->
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

            _addressState.update { currentState ->
                currentState.copy(addressInputList = _addressState.value.addressInputList.map { items ->
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
        _addressState.update {currentState ->
            currentState.copy(countryData = countryData)
        }
    }

    private fun updateState() {
        _addressState.update { currentState ->
            currentState.copy(updateTime = LocalDateTime.now().toString())
        }
    }
}