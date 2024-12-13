package umi.app.vantech.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import umi.app.vantech.Preferences
import umi.app.vantech.network.ApiViewModel

class ApiViewModelFactory(private val application: Preferences) : ViewModelProvider.Factory {

    // Override the create method to instantiate ApiViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiViewModel::class.java)) {
            // Pass the application context to ApiViewModel
            return ApiViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
