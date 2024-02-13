package com.example.ant.page.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.CustomerAccessTokenCreateMutation
import com.example.CustomerAddressDeleteMutation
import com.example.CustomerCreateMutation
import com.example.CustomerQuery
import com.example.ant.common.tools.DataStoreManager
import com.example.ant.common.tools.TOKEN
import com.example.ant.model.input.InputModel
import com.example.ant.server.Network
import com.example.type.CustomerAccessTokenCreateInput
import com.example.type.CustomerCreateInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class LoginState(
    var updateTime: String = "",
    var loginInputList: MutableList<MutableList<InputModel>> = mutableListOf(),
    var regisInputList: MutableList<MutableList<InputModel>> = mutableListOf(),
    var customer: CustomerQuery.Customer? = null,
    var isLoginOrLogout: Boolean = false,
    var isLoading: Boolean = false
)

@HiltViewModel
class LoginVM @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _loginState = MutableStateFlow(LoginState())

    var loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    var token = ""

    var callBack: (() -> Unit)? = null

    var dataStoreManager = DataStoreManager(context = application.applicationContext)

    init {
        _loginState.value.loginInputList = mutableListOf(
            mutableListOf(InputModel(title = "Email")),
            mutableListOf(InputModel(title = "Password"))
        )

        _loginState.value.regisInputList = mutableListOf(
            mutableListOf(InputModel(title = "First Name"), InputModel(title = "Last Name")),
            mutableListOf(InputModel(title = "Email")),
            mutableListOf(InputModel(title = "Password"))
        )

        viewModelScope.launch {
            val localValue = dataStoreManager.getFromDataStore(TOKEN).first()
            if (localValue.isNotEmpty()) {
                token = localValue
                queryCustomer()
            }
        }
    }

    fun loginClick() {

        var hasError = false
        for (items in _loginState.value.loginInputList) {
            for (item in items) {
                if (item.required && item.text.isEmpty()) {
                    println("${item.title} show error")
                    item.showError = true
                    hasError = true
                }
            }
        }

        updateState()

        if (hasError) {

            _loginState.value.isLoading = true

            viewModelScope.launch {
                try {
                    val response = Network.shared.mutation(
                        CustomerAccessTokenCreateMutation(
                            input = CustomerAccessTokenCreateInput(
                                email = "bager1@163.com",
                                password = "123321"
                            )
                        )
                    ).execute()
                    response.data?.customerAccessTokenCreate?.customerAccessToken?.let {
                        Log.d(
                            "d",
                            "token===" + it.accessToken
                        )
                        dataStoreManager.saveToDataStore(key = TOKEN, value = it.accessToken)

                        token = it.accessToken

                        queryCustomer()
                    }

                } catch (e: ApolloException) {
                    Log.w("Login", "Failed to login", e)
                }
            }
        }
    }

    fun queryCustomer() {

        _loginState.value.isLoading = true

        viewModelScope.launch {
            try {
                val response = Network.shared.query(
                    CustomerQuery(customerAccessToken = token)
                ).execute()

                response.data?.customer?.let {

                    _loginState.value.customer = it

                    _loginState.value.isLoginOrLogout = true

                    _loginState.value.isLoading = false

                    updateState()

                    callBack?.let { it1 -> it1() }
                }

            } catch (e: ApolloException) {
                Log.w("Login", "Failed to login", e)
            }
        }
    }

    fun regisClick() {
        var hasError = false
        for (items in _loginState.value.regisInputList) {
            for (item in items) {
                if (item.required && item.text.isEmpty()) {
                    println("${item.title} show error")
                    item.showError = true
                    hasError = true
                }
            }
        }

        updateState()

        if (!hasError) {
            _loginState.value.isLoading = true

            viewModelScope.launch {
                try {
                    val response = Network.shared.mutation(
                        CustomerCreateMutation(
                            input = CustomerCreateInput(
                                firstName = Optional.present("Bager"),
                                lastName = Optional.present("Z"),
                                email = "bager9@163.com",
                                password = "123321"
                            )
                        )
                    ).execute()
                    _loginState.value.isLoading = false

                    updateState()

                } catch (e: ApolloException) {
                    Log.w("Login", "Failed to login", e)
                }
            }
        }
    }

    private fun updateState() {
        _loginState.update { currentState ->
            currentState.copy(updateTime = LocalDateTime.now().toString())
        }
    }

    fun logoutClick() {
        _loginState.value.isLoginOrLogout = false
        _loginState.value.customer = null
        viewModelScope.launch {
            dataStoreManager.saveToDataStore(key = TOKEN, value = "")
        }
        updateState()
    }

    fun deleteAddress(id: String) {
        _loginState.value.isLoading = true
        updateState()

        viewModelScope.launch {
            try {
                val response = Network.shared.mutation(
                    CustomerAddressDeleteMutation(
                        customerAccessToken = token,
                        id = id
                    )
                ).execute()

                queryCustomer()

            } catch (e: ApolloException) {
                Log.w("Login", "Failed to login", e)
            }
        }
    }
}

