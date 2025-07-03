package com.mendelu.xmoric.tuttimer.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.mendelu.xmoric.tuttimer.constants.Constants.ACCESS_REVOKED_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Constants.REVOKE_ACCESS_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Constants.SENSITIVE_OPERATION_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Constants.SIGN_OUT
import com.mendelu.xmoric.tuttimer.constants.Constants.WELCOME_MESSAGE
import com.mendelu.xmoric.tuttimer.constants.Utils.Companion.showMessage
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.BottomNavBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import com.mendelu.xmoric.tuttimer.models.Response.Success
import com.mendelu.xmoric.tuttimer.models.Response.Loading
import com.mendelu.xmoric.tuttimer.models.Response.Failure
import com.mendelu.xmoric.tuttimer.ui.elements.ProgressBar
import com.mendelu.xmoric.tuttimer.ui.theme.button_color
import com.mendelu.xmoric.tuttimer.ui.theme.red_timer_color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navigation: INavigationRouter, viewModel: ProfileScreenViewModel = getViewModel()) {

    Scaffold(
        content =  { paddingValues ->
            ProfileScreenContent(viewModel = viewModel,
                modifier = Modifier.padding(
                    bottom = paddingValues.calculateBottomPadding()
                ),
                navigation = navigation
            )
        },
        bottomBar = { BottomNavBar(navigation = navigation) },

        )
}

@Composable
fun ProfileScreenContent(modifier: Modifier, navigation: INavigationRouter, viewModel: ProfileScreenViewModel) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var revokeAccessClicked by remember { mutableStateOf(false) }

    if (revokeAccessClicked) {
        RevokeAccess(
            viewModel = viewModel,
            scaffoldState = scaffoldState,
            coroutineScope = coroutineScope,
            signOut = { viewModel.signOut() }
        )
    }

    val context = LocalContext.current
    val mail = FirebaseAuth.getInstance().currentUser!!.email.toString()

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.signOut()
            },
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp, top = 8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = button_color,
                contentColor = Color.White),
            content = { Text(text = "Sign Out", style = MaterialTheme.typography.bodySmall) },
        )

        Button(
            onClick = {
                viewModel.sendPasswordResetEmail(mail)
                Toast.makeText(context, "Email has been sent to: $mail", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp, top = 8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = button_color,
                contentColor = Color.White),
            content = { Text(text = "Reset password", style = MaterialTheme.typography.bodySmall) },
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                revokeAccessClicked = true
                viewModel.revokeAccess()
            },
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp, top = 8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = red_timer_color,
                contentColor = Color.White),
            content = { Text(text = "Delete Account", style = MaterialTheme.typography.bodySmall) },
        )

        Text(
            text = "Created by Marek Moric for sake of Bachelor Thesis",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }

}


@Composable
fun RevokeAccess(
    viewModel: ProfileScreenViewModel,
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





