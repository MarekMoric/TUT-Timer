package com.mendelu.xmoric.tuttimer.ui.screens.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.constants.Constants
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.BackArrowScreen
import com.mendelu.xmoric.tuttimer.ui.elements.LoadingScreen
import com.mendelu.xmoric.tuttimer.ui.elements.NumberPicker
import com.mendelu.xmoric.tuttimer.ui.elements.SegmentedControl
import com.mendelu.xmoric.tuttimer.ui.theme.bottom_bar_selected_color
import com.mendelu.xmoric.tuttimer.ui.theme.button_color
import org.koin.androidx.compose.getViewModel

@Composable
fun ActivityDetailScreen(navigation: INavigationRouter,
                 viewModel: ActivityDetailScreenViewModel = getViewModel(),
                 id: Long?) {

    viewModel.activityId = id

    val detailUIState: DetailUIState? by viewModel.detailUIState.collectAsState()

    var activityErrorMessage: String by rememberSaveable{ mutableStateOf("") }

    var activity: Activity by rememberSaveable { mutableStateOf(viewModel.activity) }

    var isNotLoading: Boolean by rememberSaveable { mutableStateOf(false) }

    detailUIState?.let {
        when(it){
            DetailUIState.Default -> {
                LaunchedEffect(it){
                    viewModel.initActivityFirebase()
//                    viewModel.initActivity()
                }
            }
            is DetailUIState.ActivityError -> {
                activityErrorMessage = stringResource(id = it.error)
            }
            DetailUIState.ActivityLoaded -> {
                activity = viewModel.activity
                isNotLoading = true
            }
            DetailUIState.ActivityRemoved -> {
                navigation.getNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.REFRESH_SCREEN, true)
                LaunchedEffect(it){ navigation.returnBack() }
            }
            DetailUIState.ActivitySaved -> {
                navigation.getNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.REFRESH_SCREEN, true)
                LaunchedEffect(it){ navigation.returnBack() }
            }
        }
    }

    BackArrowScreen(
        topBarText = viewModel.activity.name!!,
        content = {  DetailScreenContent(
            viewModel = viewModel,
            id = id,
            isNotLoading = isNotLoading,
            navigation = navigation,
            activity = activity)},
        onBackClick = {navigation.returnBack()},
        actions = {
            IconButton(onClick = {
                /*viewModel.deleteActivity()*/
                viewModel.deleteActivityFirebase()
            }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
            }
        },
        showBackArrow = true)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(viewModel: ActivityDetailScreenViewModel,
                        id: Long?,
                        isNotLoading: Boolean,
                        navigation: INavigationRouter,
                        activity: Activity) {

    val activities = listOf("Surpass", "Speedy", "TUT")
    val goals = listOf("30s", "60s", "90s")

    val context = LocalContext.current
    val segmentedPickType = when(activity.type){
        "Surpass" -> 0
        "Speedy" -> 1
        "TUT" -> 2
        else -> 0
    }

    val segmentedPickGoal = when {
        activity.minutes == 0 && activity.seconds == 30 -> 0
        activity.minutes == 1 && activity.seconds == 0 -> 1
        activity.minutes == 1 && activity.seconds == 30 -> 2
        else -> 0
    }

    if(isNotLoading){
        var actType by rememberSaveable { mutableStateOf(activity.type) }
        var actName by rememberSaveable { mutableStateOf(activity.name) }
        var actMetric by rememberSaveable { mutableStateOf(activity.metric) }
        var actMinutes by rememberSaveable { mutableStateOf(activity.minutes) }
        var actSeconds by rememberSaveable { mutableStateOf(activity.seconds) }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select type of activity",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 24.dp))
            SegmentedControl(
                items = activities,
                defaultSelectedItemIndex = segmentedPickType,
                cornerRadius = 80
            ) { selectedItemIndex ->
                actType = activities[selectedItemIndex]
            }
            Row(horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                Text(
                    text = "Name your activity",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 24.dp))

            TextField(
                value = actName!!,
                onValueChange = { actName = it},
                singleLine = true,
                label = { Text(text = "Activity name") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = bottom_bar_selected_color
                ),
            )

            Row(horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                Text(
                    text = "Set goal",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 24.dp))

            if (actType != "TUT"){
                Row() {
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bottom_bar_selected_color)
                        .padding(8.dp)){
                        NumberPicker(
                            state = remember { mutableStateOf(activity.minutes!!) },
                            range = 0..59,
                            onStateChanged = { newNumber -> actMinutes = newNumber
                            }
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = ":",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)) // cut the corners with 8 dp radius
                        .background(bottom_bar_selected_color) // set the background color
                        .padding(8.dp)){
                        NumberPicker(
                            state = remember { mutableStateOf(activity.seconds!!) },
                            range = 0..59,
                            onStateChanged = { newNumber -> actSeconds = newNumber
                            }
                        )
                    }
                }
            } else {
                SegmentedControl(
                    items = goals,
                    defaultSelectedItemIndex = segmentedPickGoal,
                    cornerRadius = 80
                ) { selectedItemIndex ->
                    when(selectedItemIndex) {
                        0 -> {
                            actSeconds = 30
                            actMinutes = 0
                        }
                        1 -> {
                            actSeconds = 0
                            actMinutes = 1
                        }
                        else -> {
                            actSeconds = 30
                            actMinutes = 1
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                Text(
                    text = "Activity metric",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = actMetric!!,
                onValueChange = { actMetric = it},
                label = { Text(text = "Input distance, weight or height if applicable.") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 8.dp, end = 8.dp),
                shape = RoundedCornerShape(10)
            )

            Button(
                onClick = {
                    viewModel.activity.type = actType
                    viewModel.activity.metric = actMetric
                    viewModel.activity.name = actName
                    viewModel.activity.minutes = actMinutes
                    viewModel.activity.seconds = actSeconds
                    viewModel.activity.goal = if (actSeconds in 0 .. 9 && actMinutes in 0 .. 9) "0$actMinutes:0$actSeconds"
                    else if (actMinutes in 0 .. 9) "0$actMinutes:$actSeconds"
                    else if (actSeconds in 0 .. 9) "$actMinutes:0$actSeconds"
                    else "$actMinutes:$actSeconds"

                    Toast.makeText(context, "$actName updated!", Toast.LENGTH_SHORT).show()
//                    viewModel.updateActivity()
                    viewModel.updateActivityFirebase()
                },
                shape = RoundedCornerShape(100),
                modifier = Modifier
                    .padding(end = 8.dp, start = 8.dp ,top = 8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = button_color,
                    contentColor = Color.White),
                enabled = actName!!.isNotBlank(),
                content = { Text(text = "Save", style = MaterialTheme.typography.bodySmall) },
            )
        }
    } else {
        LoadingScreen()
    }


}