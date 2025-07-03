package com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xmoric.tuttimer.authentication.AuthRepository
import com.mendelu.xmoric.tuttimer.authentication.SignInResponse
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel(
    private val repo: AuthRepository
): ViewModel() {
    var signInResponse by mutableStateOf<SignInResponse>(Success(false))
        private set

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signInResponse = Loading
        signInResponse = repo.firebaseSignInWithEmailAndPassword(email, password)
    }
}