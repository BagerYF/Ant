package com.example.ant.common.tools

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "APP_DATASTORE")

val TOKEN = stringPreferencesKey("TOKEN")
val SEARCH_HISTORY = stringPreferencesKey("SEARCH_HISTORY")
val CART_ID = stringPreferencesKey("CART_ID")
val WISHLIST = stringPreferencesKey("WISHLIST")

class DataStoreManager(val context: Context) {

    companion object {

    }

    suspend fun saveToDataStore(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit {
            if (key == SEARCH_HISTORY) {
                val localValue = getFromDataStore(key = key).first()
                if (localValue.isNotEmpty()) {
                    val list = Gson().fromJson(localValue, Array<String>::class.java).asList().toMutableList()
                    list.remove(value)
                    list.add(0, value)
                    if (list.count() > 5) {
                        list.removeLast()
                    }
                    it[key] = list.toString()
                } else {
                    val list = mutableListOf<String>()
                    list.add(value)
                    it[key] = list.toString()
                }
            } else {
                it[key] = value
            }
        }
    }

    fun getFromDataStore(key: Preferences.Key<String>) : Flow<String> {
        val r = context.dataStore.data.map {
            it[key] ?: ""
        }
        return r
    }


    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }

}
