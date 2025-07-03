package com.mendelu.xmoric.tuttimer.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController)
    : INavigationRouter {
    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToProfile() {
        navController.navigate(Destination.ProfileScreen.route)
    }

    override fun navigateToTimer() {
        navController.navigate(Destination.TimerScreen.route)
    }

    override fun navigateToActivities() {
        navController.navigate(Destination.ActivitiesScreen.route)
    }

    override fun navigateToActivityCreation() {
        navController.navigate(Destination.ActivityCreationScreen.route)
    }

    override fun navigateToActivityDetail(id: Long?) {
        navController.navigate(Destination.ActivityDetailScreen.route  + "/" + id )
    }

    override fun navigateToResults() {
        navController.navigate(Destination.ResultsScreen.route)
    }

    override fun navigateToResultDetail(id: Long?) {
        navController.navigate(Destination.ResultDetailScreen.route   + "/" + id )
    }

    override fun navigateToSignIn() {
        navController.navigate(Destination.SignInScreen.route)
    }

    override fun navigateToForgot() {
        navController.navigate(Destination.ForgotPasswordScreen.route)
    }

    override fun navigateToSignUp() {
        navController.navigate(Destination.SignUpScreen.route)
    }

    override fun navigateToVerify() {
        navController.navigate(Destination.VerifyEmailScreen.route)
    }


}