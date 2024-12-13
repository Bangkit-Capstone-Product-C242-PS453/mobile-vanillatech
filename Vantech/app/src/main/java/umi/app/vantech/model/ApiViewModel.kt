package umi.app.vantech.network


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.HttpException
import umi.app.vantech.Preferences
import umi.app.vantech.data.Disease
import umi.app.vantech.data.LoginRequest
import umi.app.vantech.data.PredictionResponse

import umi.app.vantech.data.TokenResponse
import umi.app.vantech.network.ApiHelper
import java.io.IOException

class ApiViewModel(private val preferences: Preferences) : ViewModel() {

    private val _loginResponse = MutableLiveData<TokenResponse?>()
    val loginResponse: LiveData<TokenResponse?> = _loginResponse

    private val _predictionResponse = MutableLiveData<PredictionResponse?>()
    val predictionResponse: LiveData<PredictionResponse?> = _predictionResponse

    private val _diseases = MutableLiveData<List<Disease>?>()
    val diseases: LiveData<List<Disease>?> = _diseases

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val apiService = ApiHelper.createApiService()

    // Function for logging in
    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.login(LoginRequest(username, password)).execute()
                }
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    _errorMessage.value = "Login failed: ${response.message()} - ${response.errorBody()?.string()}"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error: ${e.message}"
            } catch (e: HttpException) {
                _errorMessage.value = "HTTP error: ${e.message()}"
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.localizedMessage}"
            }
        }
    }

    // Function for predicting image
    fun predictImage(imagePart: MultipartBody.Part) {
        val token = preferences.getValues("ACCESS_TOKEN")
        if (token.isNullOrEmpty()) {
            _errorMessage.postValue("Token is missing or expired.")
            return
        }

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.predictImage(imagePart, "Bearer $token").execute()
                }
                if (response.isSuccessful) {
                    _predictionResponse.postValue(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue("Prediction failed: ${response.code()} - ${response.message()} - Body: $errorBody")
                }
            } catch (e: IOException) {
                _errorMessage.postValue("Network error: ${e.localizedMessage}")
            } catch (e: HttpException) {
                _errorMessage.postValue("HTTP error: ${e.code()} ${e.message}")
            } catch (e: Exception) {
                _errorMessage.postValue("Unexpected error: ${e.localizedMessage}")
            }
        }
    }
}