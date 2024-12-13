package umi.app.vantech.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import umi.app.vantech.Preferences

class LoginViewModelFactory(private val preferences: Preferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}