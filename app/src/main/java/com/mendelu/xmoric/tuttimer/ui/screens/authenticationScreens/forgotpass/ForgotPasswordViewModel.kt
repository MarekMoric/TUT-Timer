package com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.forgotpass

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xmoric.tuttimer.authentication.AuthRepository
import com.mendelu.xmoric.tuttimer.authentication.SendPasswordResetEmailResponse
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repo: AuthRepository
): ViewModel() {
    var sendPasswordResetEmailResponse by mutableStateOf<SendPasswordResetEmailResponse>(Success(false))

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = Loading
        sendPasswordResetEmailResponse = repo.sendPasswordResetEmail(email)
    }

}