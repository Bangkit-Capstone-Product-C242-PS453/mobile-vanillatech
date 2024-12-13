package umi.app.vantech.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umi.app.vantech.Preferences
import umi.app.vantech.data.LoginRequest
import umi.app.vantech.data.TokenResponse
import umi.app.vantech.databinding.ActivityLoginBinding
import umi.app.vantech.network.ApiHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = Preferences(this)

        binding.apply {
            btnLogin.setOnClickListener {
                login()
            }
            tvKlikDisini.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun login() {
        binding.apply {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Silahkan isi username dan password", Toast.LENGTH_LONG).show()
                return
            }

            val request = LoginRequest(username = username, password = password)
            performLogin(request)
        }
    }

    private fun performLogin(request: LoginRequest) {
        val apiService = ApiHelper.createApiService()

        apiService.login(request).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()

                    // Simpan data login ke Preferences
                    pref.setValues("STATUS_LOGIN", "true")
                    pref.setValues("ACCESS_TOKEN", tokenResponse?.access_token ?: "")
                    pref.setValues("REFRESH_TOKEN", tokenResponse?.refresh_token ?: "")

                    Toast.makeText(applicationContext, "Login berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Login gagal: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
