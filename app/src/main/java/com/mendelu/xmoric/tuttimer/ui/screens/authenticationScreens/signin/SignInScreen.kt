package com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signin

import androidx.compose.foundation.background
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import com.mendelu.xmoric.tuttimer.constants.Constants.FORGOT_PASSWORD
import com.mendelu.xmoric.tuttimer.constants.Constants.NO_ACCOUNT
import com.mendelu.xmoric.tuttimer.constants.Constants.NO_VALUE
import com.mendelu.xmoric.tuttimer.constants.Constants.SIGN_IN
import com.mendelu.xmoric.tuttimer.constants.Constants.VERTICAL_DIVIDER
import com.mendelu.xmoric.tuttimer.models.Response
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.constants.Constants
import com.mendelu.xmoric.tuttimer.constants.Constants.SIGN_IN_SCREEN
import com.mendelu.xmoric.tuttimer.constants.Utils.Companion.showMessage
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.*
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signup.SignUpContent
import com.mendelu.xmoric.tuttimer.ui.theme.button_color

@Composable
@ExperimentalComposeUiApi
fun SignInScreen(
    navigation: INavigationRouter,
    viewModel: SignInViewModel = getViewModel(),
) {
    val context = LocalContext.current

//    Scaffold(
//        topBar = {
//            SignInTopBar()
//        },
//        content = { padding ->
//            SignInContent(
//                padding = padding,
//                signIn = { email, password ->
//                    viewModel.signInWithEmailAndPassword(email, password)
//                },
//                navigation = navigation
//            )
//        }
//    )

    BackArrowScreen(
        topBarText = SIGN_IN_SCREEN,
        content = { padding ->
            SignInContent(
                padding = padding,
                signIn = { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password)
                },
                navigation = navigation
            )
        },
    )

    SignIn(
        viewModel = viewModel,
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        }
    )
}

@Composable
fun SignIn(
    viewModel: SignInViewModel,
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val signInResponse = viewModel.signInResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> signInResponse.apply {
            LaunchedEffect(e) {
                print(e)
                showErrorMessage(e.message)
            }
        }
    }
}

@Composable
@ExperimentalComposeUiApi
fun SignInContent(
    padding: PaddingValues,
    signIn: (email: String, password: String) -> Unit,
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
        horizontalAlignment = Alignment.CenterHorizontally,

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
                signIn(email.text, password.text)
            },
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(
                containerColor = button_color,
                contentColor = Color.White
            ),
            content = {
                Text(
                    text = Constants.SIGN_IN,
                    style = MaterialTheme.typography.bodySmall
                )
            },
        )
        Row {
            Text(
                modifier = Modifier.clickable {
                    navigation.navigateToForgot()
                },
                text = FORGOT_PASSWORD,
                fontSize = 15.sp
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                text = VERTICAL_DIVIDER,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    navigation.navigateToSignUp()
                },
                text = NO_ACCOUNT,
                fontSize = 15.sp
            )
        }
    }
}