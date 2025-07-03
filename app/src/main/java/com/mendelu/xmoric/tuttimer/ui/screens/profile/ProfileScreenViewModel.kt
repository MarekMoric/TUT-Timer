package com.mendelu.xmoric.tuttimer.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xmoric.tuttimer.authentication.AuthRepository
import com.mendelu.xmoric.tuttimer.authentication.ReloadUserResponse
import com.mendelu.xmoric.tuttimer.authentication.RevokeAccessResponse
import com.mendelu.xmoric.tuttimer.authentication.SendPasswordResetEmailResponse
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val repo: AuthRepository): BaseViewModel() {
    var revokeAccessResponse by mutableStateOf<RevokeAccessResponse>(Success(false))
        private set
    var reloadUserResponse by mutableStateOf<ReloadUserResponse>(Success(false))
        private set

    fun reloadUser() = viewModelScope.launch {
        reloadUserResponse = Loading
        reloadUserResponse = repo.reloadFirebaseUser()
    }

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

    fun signOut() = repo.signOut()

    fun revokeAccess() = viewModelScope.launch {
        revokeAccessResponse = Loading
        revokeAccessResponse = repo.revokeAccess()
    }

    var sendPasswordResetEmailResponse by mutableStateOf<SendPasswordResetEmailResponse>(Success(false))
    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = Loading
        sendPasswordResetEmailResponse = repo.sendPasswordResetEmail(email)
    }
}