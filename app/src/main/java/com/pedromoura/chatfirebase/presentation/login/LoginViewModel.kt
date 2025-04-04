package com.pedromoura.chatfirebase.presentation.login

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    private var userID: String = ""

    fun saveCredential(username: String, password: String) {
        userID = if (username == "userone") {
            "1"
        } else {
            "2"
        }
        viewModelScope.launch {
            with(sharedPreferences.edit()) {
                putString("USERID", userID)
                putString("USERNAME", username)
                putString("PASSWORD", password)
                apply()
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}