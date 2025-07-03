package com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.verify

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.constants.Constants.ACCESS_REVOKED_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Constants.ALREADY_VERIFIED
import com.mendelu.xmoric.tuttimer.constants.Constants.EMAIL_NOT_VERIFIED_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Constants.REVOKE_ACCESS_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Constants.SENSITIVE_OPERATION_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Constants.SIGN_OUT
import com.mendelu.xmoric.tuttimer.constants.Constants.SPAM_EMAIL
import com.mendelu.xmoric.tuttimer.constants.Constants.VERIFY_EMAIL_SCREEN
import com.mendelu.xmoric.tuttimer.constants.Utils.Companion.showMessage
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.ProgressBar
import com.mendelu.xmoric.tuttimer.ui.elements.SmallSpacer
import com.mendelu.xmoric.tuttimer.ui.elements.TopBar
import org.koin.androidx.compose.getViewModel
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import com.mendelu.xmoric.tuttimer.models.Response.Failure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun VerifyEmailScreen(
    viewModel: VerifyViewModel = getViewModel(),
    navigation: INavigationRouter
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                title = VERIFY_EMAIL_SCREEN,
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        content = { padding ->
            VerifyEmailContent(
                padding = padding,
                reloadUser = {
                    viewModel.reloadUser()
                }
            )
        },
        scaffoldState = scaffoldState
    )

    ReloadUser(
        navigateToTimer = {
            if (viewModel.isEmailVerified) {
                navigation.navigateToTimer()
            } else {
                showMessage(context, EMAIL_NOT_VERIFIED_MESSAGE)
            }
        },
        navigation = navigation,
        viewModel = viewModel
    )

    RevokeAccess(
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        signOut = {
            viewModel.signOut()
        },
        viewModel = viewModel
    )
}

@Composable
fun VerifyEmailContent(
    padding: PaddingValues,
    reloadUser: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(start = 32.dp, end = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable {
                reloadUser()
            },
            text = ALREADY_VERIFIED,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )
        SmallSpacer()
        Text(
            text = SPAM_EMAIL,
            fontSize = 15.sp
        )
    }
}

@Composable
fun ReloadUser(
    viewModel: VerifyViewModel,
    navigateToTimer: () -> Unit,
    navigation: INavigationRouter
) {
    when(val reloadUserResponse = viewModel.reloadUserResponse) {
        is Loading -> ProgressBar()
        is Success -> {
            val isUserReloaded = reloadUserResponse.data
            LaunchedEffect(isUserReloaded) {
                if (isUserReloaded) {
                    navigation.navigateToTimer()
                }
            }
        }
        is Failure -> reloadUserResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}

@Composable
fun RevokeAccess(
    viewModel: VerifyViewModel,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    signOut: () -> Unit,
) {
    val context = LocalContext.current

    fun showRevokeAccessMessage() = coroutineScope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = REVOKE_ACCESS_MESSAGE,
            actionLabel = SIGN_OUT
        )
        if (result == SnackbarResult.ActionPerformed) {
            signOut()
        }
    }

    when(val revokeAccessResponse = viewModel.revokeAccessResponse) {
        is Loading -> ProgressBar()
        is Success -> {
            val isAccessRevoked = revokeAccessResponse.data
            LaunchedEffect(isAccessRevoked) {
                if (isAccessRevoked) {
                    showMessage(context, ACCESS_REVOKED_MESSAGE)
                }
            }
        }
        is Failure -> revokeAccessResponse.apply {
            LaunchedEffect(e) {
                print(e)
                if (e.message == SENSITIVE_OPERATION_MESSAGE) {
                    showRevokeAccessMessage()
                }
            }
        }
    }
}