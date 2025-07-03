package com.mendelu.xmoric.tuttimer.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.R
import com.mendelu.xmoric.tuttimer.navigation.Destination
import com.mendelu.xmoric.tuttimer.ui.theme.*

@Composable
fun BottomNavBar(navigation: INavigationRouter) {
    val currentDestination: String? =
        navigation.getNavController()
        .currentBackStackEntry?.destination
        ?.route

    NavigationBar(
        containerColor = bottom_bar_light_color_2,

    ) {
        NavigationBarItem(
            icon = {
                Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally)    {
                    if (isDestinationProfile(currentDestination)){
                        Icon(painter = painterResource(R.drawable.ic_person_selected) ,
                            contentDescription = "Icon Person Selected")
                    }else{
                        Icon(painter = painterResource(R.drawable.ic_person) ,
                            contentDescription = "Icon Person")
                    }
                    Text(text = "Profile")
                }

                 },
            //label = { Text("Profile") },
            selected = isDestinationProfile(currentDestination),
            onClick = { navigation.navigateToProfile()},
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = bottom_bar_selected_color_2)

        )

        NavigationBarItem(
            icon = {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isDestinationTimer(currentDestination)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_timer_selected),
                            contentDescription = "Icon Timer Selected"
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_timer),
                            contentDescription = "Icon Timer"
                        )
                    }
                    Text(text = "Timer")
                }
            },
            //label = { Text("Timer") },
            selected = isDestinationTimer(currentDestination),
            onClick = { navigation.navigateToTimer() },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = bottom_bar_selected_color_2)
        )

        NavigationBarItem(
            icon = {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)    {
                    if (isDestinationActivities(currentDestination)){
                        Icon(painter = painterResource(R.drawable.ic_activities_add_selected) ,
                            contentDescription = "Icon Activities Add Selected")
                    }else{
                        Icon(painter = painterResource(R.drawable.ic_activities_add) ,
                            contentDescription = "Icon Activities Add")
                    }
                    Text(text = "Activities")
                }
            },
            selected = isDestinationActivities(currentDestination),
            onClick = { navigation.navigateToActivities() },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = bottom_bar_selected_color_2)
        )

        NavigationBarItem(
            icon = {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)    {
                    if (isDestinationResults(currentDestination)){
                        Icon(painter = painterResource(R.drawable.ic_results_list_selected) ,
                            contentDescription = "Icon Results List Selected")
                    }else{
                        Icon(painter = painterResource(R.drawable.ic_results_list) ,
                            contentDescription = "Icon Results List")
                    }
                    Text(text = "Results")
                }
            },
            selected = isDestinationResults(currentDestination),
            onClick = { navigation.navigateToResults() },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = bottom_bar_selected_color_2)
        )


    }
}

fun isDestinationProfile(currentDestination: String?): Boolean{
    return currentDestination == Destination.ProfileScreen.route
}

fun isDestinationTimer(currentDestination: String?): Boolean{
    return currentDestination == Destination.TimerScreen.route
}

fun isDestinationActivities(currentDestination: String?):Boolean{
    return currentDestination == Destination.ActivitiesScreen.route
}

fun isDestinationResults(currentDestination: String?):Boolean{
    return currentDestination == Destination.ResultsScreen.route
}