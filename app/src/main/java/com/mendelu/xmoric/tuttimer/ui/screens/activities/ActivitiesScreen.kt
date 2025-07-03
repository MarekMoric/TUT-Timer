package com.mendelu.xmoric.tuttimer.ui.screens.activities

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mendelu.xmoric.tuttimer.constants.Constants
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.ActivityRow
import com.mendelu.xmoric.tuttimer.ui.elements.BottomNavBar
import com.mendelu.xmoric.tuttimer.ui.theme.button_color
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(navigation: INavigationRouter, viewModel: ActivitiesScreenViewModel = getViewModel()) {

    val activities = remember { mutableListOf<Activity>()}
    val activitiesUIState: ActivitiesUIState? by viewModel.activitiesUIState.collectAsState()

    val onReturnRefresh = navigation.getNavController().currentBackStackEntry?.savedStateHandle
        ?.getLiveData<Boolean>(Constants.REFRESH_SCREEN)
        ?.observeAsState() //refresh ked sa vratim z deletu napriklad

    onReturnRefresh?.value?.let {
        if (it) {
            LaunchedEffect(it) {
//                viewModel.loadActivities()
                viewModel.loadActivitiesFirebase()
                Log.d("return", "som tu")
            }
        }
    }

    activitiesUIState?.let {
        when(it){
            ActivitiesUIState.Default -> {
                LaunchedEffect(it){
//                    viewModel.loadActivities()
                    viewModel.loadActivitiesFirebase()
                }
            }
            is ActivitiesUIState.ActivitiesLoaded -> {
                activities.clear()
                activities.addAll(it.activities)
            }
        }
    }

    Scaffold(
        content =  { paddingValues ->
            ActivitiesScreenContent(modifier = Modifier.padding(
                                        bottom = paddingValues.calculateBottomPadding()),
                            navigation = navigation, activities = activities)},
        bottomBar = { BottomNavBar(navigation = navigation) },
        )
}

@Composable
fun ActivitiesScreenContent(modifier: Modifier, navigation: INavigationRouter, activities: List<Activity>) {

    LazyColumn(modifier = modifier.fillMaxHeight())
    {
        activities.forEach{
            item(key = it.id){
                ActivityRow(
                    activity = it,
                    onRowClick = {navigation.navigateToActivityDetail(it.id)}/*navigation.navigateToActivityDetail(it.id)*/)
            }
        }
    }

    Column(
        // on below line we are adding a modifier to it
        // and setting max size, max height and max width
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(8.dp),

        // on below line we are adding vertical
        // arrangement and horizontal alignment.
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = { navigation.navigateToActivityCreation()},
            containerColor = button_color,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
            Icon(Icons.Filled.Add, "")
        }
    }
}




