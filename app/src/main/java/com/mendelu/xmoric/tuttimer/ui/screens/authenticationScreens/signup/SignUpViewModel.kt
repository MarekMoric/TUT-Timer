package com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xmoric.tuttimer.authentication.AuthRepository
import com.mendelu.xmoric.tuttimer.authentication.SendEmailVerificationResponse
import com.mendelu.xmoric.tuttimer.authentication.SignUpResponse
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import kotlinx.coroutines.launch


class SignUpViewModel(
    private val repo: AuthRepository
): ViewModel() {
    var signUpResponse by mutableStateOf<SignUpResponse>(Success(false))
        private set
    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(Success(false))
        private set

    fun signUpWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signUpResponse = Loading
        signUpResponse = repo.firebaseSignUpWithEmailAndPassword(email, password)
    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = Loading
        sendEmailVerificationResponse = repo.sendEmailVerification()
    }
}