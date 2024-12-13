package umi.app.vantech.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umi.app.vantech.Preferences
import umi.app.vantech.data.LoginRequest
import umi.app.vantech.data.TokenResponse
import umi.app.vantech.network.ApiHelper

class LoginViewModel(private val preferences: Preferences) : ViewModel() {

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    fun login(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _loginResult.value = "Silahkan isi username dan password"
            return
        }

        val request = LoginRequest(username = username, password = password)
        performLogin(request)
    }

    private fun performLogin(request: LoginRequest) {
        val apiService = ApiHelper.createApiService()

        apiService.login(request).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()

                    // Simpan data login ke Preferences
                    preferences.setValues("STATUS_LOGIN", "true")
                    preferences.setValues("ACCESS_TOKEN", tokenResponse?.access_token ?: "")
                    preferences.setValues("REFRESH_TOKEN", tokenResponse?.refresh_token ?: "")

                    _loginResult.value = "Login berhasil"
                } else {
                    _loginResult.value = "Login gagal: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                _loginResult.value = "Error: ${t.message}"
            }
        })
    }
}