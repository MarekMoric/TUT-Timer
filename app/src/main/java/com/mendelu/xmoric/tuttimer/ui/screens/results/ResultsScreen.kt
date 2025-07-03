package com.mendelu.xmoric.tuttimer.ui.screens.results

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.mendelu.xmoric.tuttimer.database.Result
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.BottomNavBar
import com.mendelu.xmoric.tuttimer.ui.elements.ResultRow
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navigation: INavigationRouter, viewModel: ResultsScreenViewModel = getViewModel()) {

    val results = remember { mutableListOf<Result>()}
    val resultsUIState: ResultsUIState? by viewModel.resultsUIState.collectAsState()

    val onReturnRefresh = navigation.getNavController().currentBackStackEntry?.savedStateHandle
        ?.getLiveData<Boolean>(Constants.REFRESH_SCREEN)
        ?.observeAsState() //refresh ked sa vratim z deletu napriklad

    onReturnRefresh?.value?.let {
        if (it) {
            LaunchedEffect(it) {
//                viewModel.loadResults()
                viewModel.loadResultsFirebase()
            }
        }
    }

    resultsUIState?.let {
        when(it){
            ResultsUIState.Default -> {
                LaunchedEffect(it){
//                    viewModel.loadResults()
                    viewModel.loadResultsFirebase()
                }
            }
            is ResultsUIState.ResultsLoaded -> {
                results.clear()
                results.addAll(it.results)
            }
        }
    }

    Scaffold(
        content =  { paddingValues ->
            ResultsScreenContent(modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding()),
                navigation = navigation, results = results)
        },
        bottomBar = { BottomNavBar(navigation = navigation) },
    )
}

@Composable
fun ResultsScreenContent(modifier: Modifier, navigation: INavigationRouter, results: List<Result>) {

    LazyColumn(modifier = modifier.fillMaxHeight())
    {
        results.forEach{
            item(key = it.id){
                ResultRow(
                    result = it,
                    onRowClick = {
                        navigation.navigateToResultDetail(it.id)
                    }
                )
            }
        }
    }

}




