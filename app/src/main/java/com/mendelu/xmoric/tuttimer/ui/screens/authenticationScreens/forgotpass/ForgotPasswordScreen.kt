package com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.forgotpass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.constants.Constants
import com.mendelu.xmoric.tuttimer.constants.Constants.FORGOT_PASSWORD_SCREEN
import com.mendelu.xmoric.tuttimer.constants.Constants.NO_VALUE
import com.mendelu.xmoric.tuttimer.constants.Constants.RESET_PASSWORD
import com.mendelu.xmoric.tuttimer.constants.Constants.RESET_PASSWORD_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Utils.Companion.showMessage
import org.koin.androidx.compose.getViewModel
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import com.mendelu.xmoric.tuttimer.models.Response.Failure
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.*
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signup.SignUpContent
import com.mendelu.xmoric.tuttimer.ui.theme.button_color

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = getViewModel(),
    navigation: INavigationRouter
) {
    val context = LocalContext.current

//    Scaffold(
//        topBar = {
//            ForgotPasswordTopBar(
//                navigation = navigation
//            )
//        },
//        content = { padding ->
//            ForgotPasswordContent(
//                padding = padding,
//                sendPasswordResetEmail = { email ->
//                    viewModel.sendPasswordResetEmail(email)
//                }
//            )
//        }
//    )

    BackArrowScreen(
        topBarText = FORGOT_PASSWORD_SCREEN,
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = { email ->
                    viewModel.sendPasswordResetEmail(email)
                }
            )
        },
        onBackClick = { navigation.returnBack() },
        showBackArrow = true
    )

    ForgotPassword(
        navigation = navigation,
        showResetPasswordMessage = {
            showMessage(context, RESET_PASSWORD_MESSAGE)
        },
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        },
        viewModel = viewModel
    )
}

@Composable
fun ForgotPassword(
    viewModel: ForgotPasswordViewModel,
    navigation: INavigationRouter,
    showResetPasswordMessage: () -> Unit,
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val sendPasswordResetEmailResponse = viewModel.sendPasswordResetEmailResponse) {
        is Loading -> ProgressBar()
        is Success -> {
            val isPasswordResetEmailSent = sendPasswordResetEmailResponse.data
            LaunchedEffect(isPasswordResetEmailSent) {
                if (isPasswordResetEmailSent) {
                    navigation.returnBack()
                    showResetPasswordMessage()
                }
            }
        }
        is Failure -> sendPasswordResetEmailResponse.apply {
            LaunchedEffect(e) {
                print(e)
                showErrorMessage(e.message)
            }
        }
    }
}

@Composable
fun ForgotPasswordContent(
    padding: PaddingValues,
    sendPasswordResetEmail: (email: String) -> Unit,
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(NO_VALUE)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            email = email,
            onEmailValueChange = { newValue ->
                email = newValue
            }
        )
        SmallSpacer()
        Button(
            onClick = {
                sendPasswordResetEmail(email.text)
            },
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(
                containerColor = button_color,
                contentColor = Color.White
            ),
            content = {
                Text(
                    text = RESET_PASSWORD,
                    style = MaterialTheme.typography.bodySmall
                )
            },
        )
    }
}