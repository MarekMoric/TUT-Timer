package com.mendelu.xmoric.tuttimer.di

import androidx.lifecycle.SavedStateHandle
import com.mendelu.xmoric.tuttimer.ui.activities.MainViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.activities.ActivitiesScreenViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.forgotpass.ForgotPasswordViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.creation.ActivityCreationScreenViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.detail.ActivityDetailScreenViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.profile.ProfileScreenViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.results.ResultsScreenViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.timer.TimerScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signin.SignInViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signup.SignUpViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.verify.VerifyViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.resultDetail.ResultDetailViewModel

val viewModelModule = module {
    // for screens
    viewModel { ActivityCreationScreenViewModel(get()) }
    viewModel { ActivitiesScreenViewModel(get()) }
    viewModel { ProfileScreenViewModel(get()) }
    viewModel { ActivityDetailScreenViewModel(get()) }
    viewModel { TimerScreenViewModel(get(), get()) }
    viewModel { ResultsScreenViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { VerifyViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { ResultDetailViewModel() }

    // For the saved state handle
    fun provideSavedStateHandle(): SavedStateHandle{
        return SavedStateHandle()
    }

    factory { provideSavedStateHandle() }

}
