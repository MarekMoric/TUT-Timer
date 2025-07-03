package com.mendelu.xmoric.tuttimer.navigation

sealed class Destination(
    val route: String
){
    object ProfileScreen: Destination(route = "profile_screen")
    object TimerScreen: Destination(route = "timer_screen")
    object ActivitiesScreen: Destination(route = "list_of_activities_screen")
    object ActivityCreationScreen: Destination(route = "activity_creation_screen")
    object ActivityDetailScreen: Destination(route = "activity_detail_screen")
    object ResultsScreen: Destination(route = "list_of_results_screen")
    object ResultDetailScreen: Destination(route = "result_detail_screen")

    //Authentication
    object SignInScreen: Destination(route = "signin_screen")
    object ForgotPasswordScreen: Destination(route = "forgot_screen")
    object SignUpScreen: Destination(route = "signup_screen")
    object VerifyEmailScreen: Destination(route = "verify_screen")
}
