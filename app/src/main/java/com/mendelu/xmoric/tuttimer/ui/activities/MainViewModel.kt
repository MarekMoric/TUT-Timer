package com.mendelu.xmoric.tuttimer.ui.activities

import androidx.lifecycle.viewModelScope
import com.mendelu.xmoric.tuttimer.authentication.AuthRepository
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: AuthRepository
): BaseViewModel() {
    init {
        getAuthState()
    }

    fun getAuthState() = repo.getAuthState(viewModelScope)

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false
}