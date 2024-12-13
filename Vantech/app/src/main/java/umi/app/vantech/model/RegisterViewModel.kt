package umi.app.vantech.ui

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umi.app.vantech.data.RegisterRequest
import umi.app.vantech.data.RegisterResponse
import umi.app.vantech.network.ApiHelper

class RegisterViewModel(application: Application) : ViewModel() {

    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

    fun registerUser (context: Context, request: RegisterRequest) {
        viewModelScope.launch {
            val apiService = ApiHelper.createApiService()
            apiService.register(request).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        _registrationResult.value = "Registrasi berhasil: ${registerResponse?.message}"
                    } else {
                        _registrationResult.value = "Registrasi gagal: ${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _registrationResult.value = "Error: ${t.message}"
                }
            })
        }
    }

    fun validateInput(
        namaLengkap: String,
        alamat: String,
        noTelp: String,
        username: String,
        email: String,
        password: String,
        konfirPassword: String,
        context: Context
    ): Boolean {
        return when {
            namaLengkap.isEmpty() || alamat.isEmpty() || noTelp.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || konfirPassword.isEmpty() -> {
                Toast.makeText(context, "Lengkapi data terlebih dahulu", Toast.LENGTH_LONG).show()
                false
            }
            password != konfirPassword -> {
                Toast.makeText(context, "Password tidak sama", Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }
}