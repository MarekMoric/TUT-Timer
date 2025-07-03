package com.mendelu.xmoric.tuttimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mendelu.xmoric.tuttimer.constants.Constants
import com.mendelu.xmoric.tuttimer.ui.screens.activities.ActivitiesScreen
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.forgotpass.ForgotPasswordScreen
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signup.SignUpScreen
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.signin.SignInScreen
import com.mendelu.xmoric.tuttimer.ui.screens.authenticationScreens.verify.VerifyEmailScreen
import com.mendelu.xmoric.tuttimer.ui.screens.creation.ActivityCreationScreen
import com.mendelu.xmoric.tuttimer.ui.screens.detail.ActivityDetailScreen
import com.mendelu.xmoric.tuttimer.ui.screens.profile.ProfileScreen
import com.mendelu.xmoric.tuttimer.ui.screens.resultDetail.ResultDetailScreen
import com.mendelu.xmoric.tuttimer.ui.screens.results.ResultsScreen
import com.mendelu.xmoric.tuttimer.ui.screens.timer.TimerScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination){

        composable(Destination.ProfileScreen.route) {
            ProfileScreen(navigation = navigation)
        }

        composable(Destination.TimerScreen.route){
            TimerScreen(navigation = navigation)
        }
        
        composable(Destination.ActivitiesScreen.route){
            ActivitiesScreen(navigation = navigation)
        }

        composable(Destination.ActivityCreationScreen.route){
            ActivityCreationScreen(navigation = navigation)
        }

        composable(Destination.ActivityDetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument(Constants.ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )){
            val id = it.arguments?.getLong(Constants.ID)
            ActivityDetailScreen(navigation = navigation, id = id!!)
        }

        composable(Destination.ResultsScreen.route){
            ResultsScreen(navigation = navigation)
        }
        
        composable(Destination.ResultDetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument(Constants.ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )){
            val id = it.arguments?.getLong(Constants.ID)
            ResultDetailScreen(navigation = navigation, id = id!!)
        }

        composable(Destination.SignUpScreen.route) {
            SignUpScreen(navigation = navigation)
        }

        composable(Destination.SignInScreen.route){
            SignInScreen(navigation = navigation)
        }

        composable(Destination.ForgotPasswordScreen.route){
            ForgotPasswordScreen(navigation = navigation)
        }

        composable(Destination.VerifyEmailScreen.route){
            VerifyEmailScreen(navigation = navigation)
        }
    }
}