package com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.constants.Constants
import com.mendelu.xmoric.tuttimer.constants.Constants.ALREADY_USER
import com.mendelu.xmoric.tuttimer.constants.Constants.NO_VALUE
import com.mendelu.xmoric.tuttimer.constants.Constants.SIGN_UP
import com.mendelu.xmoric.tuttimer.constants.Constants.SIGN_UP_SCREEN
import com.mendelu.xmoric.tuttimer.constants.Constants.VERIFY_EMAIL_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Utils.Companion.showMessage
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import org.koin.androidx.compose.getViewModel
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import com.mendelu.xmoric.tuttimer.models.Response.Failure
import com.mendelu.xmoric.tuttimer.ui.elements.*
import com.mendelu.xmoric.tuttimer.ui.theme.button_color
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(navigation: INavigationRouter, viewModel: SignUpViewModel = getViewModel()) {

    val context = LocalContext.current

    BackArrowScreen(
        topBarText = SIGN_UP_SCREEN,
        content = { padding ->
            SignUpContent(
                padding = padding,
                signUp = { email, password ->
                    viewModel.signUpWithEmailAndPassword(email, password)
                },
                navigation = navigation
            )
        },
        onBackClick = { navigation.returnBack() },
        showBackArrow = true
    )

    SignUp(
        viewModel = viewModel,
        sendEmailVerification = {
            viewModel.sendEmailVerification()
        },
        showVerifyEmailMessage = {
            showMessage(context, VERIFY_EMAIL_MESSAGE)
        }
    )

    SendEmailVerification(viewModel = viewModel)
}

@Composable
fun SendEmailVerification(
    viewModel: SignUpViewModel
) {
    when(val sendEmailVerificationResponse = viewModel.sendEmailVerificationResponse) {
        is Loading -> ProgressBar()
        is Success -> Unit
        is Failure -> sendEmailVerificationResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}

@Composable
fun SignUp(
    viewModel: SignUpViewModel,
    sendEmailVerification: () -> Unit,
    showVerifyEmailMessage: () -> Unit
) {
    when(val signUpResponse = viewModel.signUpResponse) {
        is Loading -> ProgressBar()
        is Success -> {
            val isUserSignedUp = signUpResponse.data
            LaunchedEffect(isUserSignedUp) {
                if (isUserSignedUp) {
                    sendEmailVerification()
                    showVerifyEmailMessage()
                }
            }
        }
        is Failure -> signUpResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}

@Composable
@ExperimentalComposeUiApi
fun SignUpContent(
    padding: PaddingValues,
    signUp: (email: String, password: String) -> Unit,
    navigation: INavigationRouter
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(NO_VALUE)) }
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(NO_VALUE)) }
    val keyboard = LocalSoftwareKeyboardController.current

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
        PasswordField(
            password = password,
            onPasswordValueChange = { newValue ->
                password = newValue
            }
        )
        SmallSpacer()
        Button(
            onClick = {
                keyboard?.hide()
                signUp(email.text, password.text)
            },
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(
                containerColor = button_color,
                contentColor = Color.White
            ),
            content = { Text(text = SIGN_UP, style = MaterialTheme.typography.bodySmall) },
        )
            Text(
                modifier = Modifier.clickable {
                    navigation.returnBack()
                },
                text = ALREADY_USER,
                fontSize = 15.sp
            )
    }
}