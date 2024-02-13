package com.example.ant.server

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.network.okHttpClient
import com.example.ant.config.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private class AuthorizationInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                addHeader("X-Shopify-Storefront-Access-Token", Constants.XShopifyStorefrontAccessToken)
            }
            .build()
        return chain.proceed(request)
    }
}

class Network {
    companion object {
        val shared : ApolloClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ApolloClient.Builder()
                .serverUrl(Constants.ApolloEndpointURL)
                .fetchPolicy(FetchPolicy.NetworkOnly)
                .okHttpClient(
                    OkHttpClient.Builder()
                        .addInterceptor(AuthorizationInterceptor())
                        .build()
                )
                .build()
        }
    }
}