package com.mendelu.xmoric.tuttimer.ui.screens.resultDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.mendelu.xmoric.tuttimer.constants.Constants
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.database.Result
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.BackArrowScreen
import com.mendelu.xmoric.tuttimer.ui.elements.LoadingScreen
import com.mendelu.xmoric.tuttimer.ui.screens.detail.ActivityDetailScreenViewModel
import com.mendelu.xmoric.tuttimer.ui.screens.detail.DetailScreenContent
import com.mendelu.xmoric.tuttimer.ui.screens.detail.DetailUIState
import com.mendelu.xmoric.tuttimer.ui.theme.green_timer_color
import com.mendelu.xmoric.tuttimer.ui.theme.orange_timer_color
import com.mendelu.xmoric.tuttimer.ui.theme.red_timer_color
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.viewModel

@Composable
fun ResultDetailScreen(navigation: INavigationRouter,
                         viewModel: ResultDetailViewModel = getViewModel(),
                         id: Long?) {

    viewModel.resultId = id

    val detailUIState: DetailUIState? by viewModel.detailUIState.collectAsState()

    var activityErrorMessage: String by rememberSaveable{ mutableStateOf("") }

    var result: Result by rememberSaveable { mutableStateOf(viewModel.result) }

    var isNotLoading: Boolean by rememberSaveable { mutableStateOf(false) }

    detailUIState?.let {
        when(it){
            DetailUIState.Default -> {
                LaunchedEffect(it){
                    viewModel.initResultFirebase()
                }
            }
            is DetailUIState.ActivityError -> {
                activityErrorMessage = stringResource(id = it.error)
            }
            DetailUIState.ActivityLoaded -> {
                result = viewModel.result
                viewModel.getAllByName()
                isNotLoading = true
            }
            DetailUIState.ActivityRemoved -> {
                navigation.getNavController().previousBackStackEntry?.savedStateHandle?.set(
                    Constants.REFRESH_SCREEN, true)
                LaunchedEffect(it){ navigation.returnBack() }
            }
            DetailUIState.ActivitySaved -> {
                navigation.getNavController().previousBackStackEntry?.savedStateHandle?.set(
                    Constants.REFRESH_SCREEN, true)
                LaunchedEffect(it){ navigation.returnBack() }
            }
        }
    }

    BackArrowScreen(
        topBarText = viewModel.result.name!!,
        content = {  ResultDetailScreenContent(
            viewModel = viewModel,
            id = id,
            isNotLoading = isNotLoading,
            navigation = navigation,
            result = result)
        },
        onBackClick = {navigation.returnBack()},
        actions = {
            IconButton(onClick = {
                /*viewModel.deleteActivity()*/
                viewModel.deleteResultFirebase()
            }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
            }
        },
        showBackArrow = true)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultDetailScreenContent(viewModel: ResultDetailViewModel,
                        id: Long?,
                        isNotLoading: Boolean,
                        navigation: INavigationRouter,
                        result: Result
) {

    if (isNotLoading){
        Column(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(8.dp)) {

//            Row(modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center){
//                Text(
//                    text = result.type!!,
////                modifier = Modifier.padding(8.dp)
//                )
//            }

            MainInfo(header = "Type:", info = result.type!!)
            MainInfo(header = "Metric:",
                info = if (result.metric!! == "") "Without metric"
                        else result.metric!!)
            MainInfo(header = "Goal:", info = result.goal!!)
            MainInfo(header = "Result:", info = result.time!!)

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                if (result.type == "Surpass" && result.achieved == true && getDiff(result) != "00:00"){
                    Text(
                        text = "You have surpassed the goal by ${getDiff(result)}",
                        color = green_timer_color
                    )
                } else if (result.type == "Surpass" && result.achieved == false && getDiff(result) != "00:00"){
                    Text(
                        text = "You were slower than your goal by ${getDiff(result)}",
                        color = red_timer_color
                    )
                } else if (result.type == "Speedy" && result.achieved == true && getDiff(result) != "00:00"){
                    Text(
                        text = "You were faster than your goal by ${getDiff(result)}",
                        color = green_timer_color
                    )
                } else if (result.type == "Speedy" && result.achieved == false && getDiff(result) != "00:00") {
                    Text(
                        text = "You were slower than your goal by ${getDiff(result)}",
                        color = red_timer_color
                    )
                } else if (result.type == "TUT" && result.achieved == true && getDiff(result) != "00:00") {
                    Text(
                        text = "You were faster than your goal by ${getDiff(result)}",
                        color = green_timer_color
                    )
                } else if (result.type == "TUT" && result.achieved == false && getDiff(result) != "00:00") {
                    Text(
                        text = "You were slower than your goal by ${getDiff(result)}",
                        color = red_timer_color
                    )
                } else {
                    Text(
                        text = "You hit your predefined goal exactly!",
                        color = orange_timer_color,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center){
                Text(text = "Your average achieved time for this activity is ${getAverage(viewModel)}", fontFamily = FontFamily.SansSerif)
            }


        }
    } else {
        LoadingScreen()
    }


}

fun getDiff(result: Result): String{

    val difference: String
    var diffSeconds: Int = 0
    var diffMinutes: Int = 0
    val goalSec = result.goalMinutes!! * 60 + result.goalSeconds!!
    val achievedSec = result.achievedMinutes!! * 60 + result.achievedSeconds!!
    val diffTotal = if (goalSec > achievedSec) goalSec - achievedSec else achievedSec - goalSec

    diffSeconds = diffTotal % 60
    diffMinutes = (diffTotal - diffSeconds) / 60

    difference = if (diffSeconds in 0 .. 9 && diffMinutes in 0 .. 9) "0$diffMinutes:0$diffSeconds"
    else if (diffMinutes in 0 .. 9) "0$diffMinutes:$diffSeconds"
    else if (diffSeconds in 0 .. 9) "$diffMinutes:0$diffSeconds"
    else "$diffMinutes:$diffSeconds"

    return difference
}

fun getAverage(viewModel: ResultDetailViewModel): String{

    val average: String
    var averageSeconds: Int = 0
    var averageMinutes: Int = 0
    val diffTotal = viewModel.allSeconds

    averageSeconds = diffTotal % 60
    averageMinutes = (diffTotal - averageSeconds) / 60

    average = if (averageSeconds in 0 .. 9 && averageMinutes in 0 .. 9) "0$averageMinutes:0$averageSeconds"
    else if (averageMinutes in 0 .. 9) "0$averageMinutes:$averageSeconds"
    else if (averageSeconds in 0 .. 9) "$averageMinutes:0$averageSeconds"
    else "$averageMinutes:$averageSeconds"

    return average
}

@Composable
fun MainInfo(header: String, info: String) {
    if(info != ""){
        Text(text = header, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif, fontSize = 20.sp)
        Text(info, fontFamily = FontFamily.SansSerif, modifier = Modifier.padding(start = 10.dp, bottom = 8.dp), fontSize = 16.sp)
    }

}