package com.example.englishapplication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="user_storage")
class EncryptedTokenStorage @Inject constructor(@ApplicationContext private val  context: Context,
    private val crypto: CryptoManager
){
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val REFRESH_IV_KEY = stringPreferencesKey("refresh_token_iv")
        private val IV_KEY = stringPreferencesKey("access_token_iv")
    }
    suspend fun saveAccessToken(token: String) {
        val (cipherText, iv) = crypto.encrypt(token)

        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = cipherText
            prefs[IV_KEY] = iv
        }
    }
    suspend fun saveRefreshToken(refreshToken: String){
        val (cipherText, iv) = crypto.encrypt(refreshToken)

        context.dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = cipherText
            prefs[REFRESH_IV_KEY] = iv
        }
    }

    suspend fun getAccessToken(): String? {
        val prefs = context.dataStore.data.first()
        val cipherText = prefs[TOKEN_KEY] ?: return null
        val iv = prefs[IV_KEY] ?: return null

        return crypto.decrypt(cipherText, iv)
    }

    suspend fun getRefreshToken(): String? {
        val prefs = context.dataStore.data.first()
        val cipherText = prefs[REFRESH_TOKEN_KEY] ?: return null
        val iv = prefs[REFRESH_IV_KEY] ?: return null

        return crypto.decrypt(cipherText, iv)
    }

    suspend fun clearAccessToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(IV_KEY)
        }
    }

    suspend fun clearRefreshToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN_KEY)
            prefs.remove(REFRESH_IV_KEY)
        }
    }

    suspend fun clearAllTokens() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(IV_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
            prefs.remove(REFRESH_IV_KEY)
        }
    }

}