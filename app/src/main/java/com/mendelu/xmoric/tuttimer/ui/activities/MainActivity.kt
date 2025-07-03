package com.mendelu.xmoric.tuttimer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.mendelu.xmoric.tuttimer.navigation.Destination
import com.mendelu.xmoric.tuttimer.navigation.NavGraph
import com.mendelu.xmoric.tuttimer.ui.theme.TUTTimerTheme
import org.koin.androidx.compose.getViewModel

class MainActivity() : ComponentActivity() {

    companion object {
        /**
         * Creates intent for the activity.
         * @param context the context to run the intent
         */
        fun createIntent(context: AppCompatActivity): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TUTTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //NavGraph(startDestination = Destination.TimerScreen.route)
                    AuthState()
                }
            }
        }
    }

    @Composable
    private fun AuthState() {
        val viewModel: MainViewModel = getViewModel()
        val isUserSignedOut = viewModel.getAuthState().collectAsState().value
        if (isUserSignedOut) {
            NavigateToSignInScreen()
        } else {
//            if (viewModel.isEmailVerified) {
                NavigateToTimerScreen()
//            } else {
//                NavigateToVerifyEmailScreen()
//            }
        }
    }

    @Composable
    private fun NavigateToSignInScreen() {
        NavGraph(startDestination = Destination.SignInScreen.route)
//        popUpTo(navController.graph.id) {
//            inclusive = true
//        }
    }

    @Composable
    private fun NavigateToProfileScreen() {
        NavGraph(startDestination = Destination.ProfileScreen.route)
//        popUpTo(navController.graph.id) {
//            inclusive = true
//        }
    }

    @Composable
    private fun NavigateToTimerScreen() {
        NavGraph(startDestination = Destination.TimerScreen.route)
//        popUpTo(navController.graph.id) {
//            inclusive = true
//        }
    }

    @Composable
    private fun NavigateToVerifyEmailScreen() {
//        NavGraph(startDestination = Destination.VerifyEmailScreen.route)
//        popUpTo(navController.graph.id) {
//            inclusive = true
//        }
    }

}