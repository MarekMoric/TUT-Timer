package com.mendelu.xmoric.tuttimer.ui.screens.results

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.database.IResultLocalRepository
import com.mendelu.xmoric.tuttimer.database.Result
import com.mendelu.xmoric.tuttimer.ui.screens.activities.ActivitiesUIState
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResultsScreenViewModel(
    private val resultsRepository: IResultLocalRepository
): BaseViewModel() {

    private val _resultsUIState
            = MutableStateFlow<ResultsUIState>(ResultsUIState.Default)

    val userID = FirebaseAuth.getInstance().currentUser?.uid

    val db = Firebase.firestore

    fun loadResultsFirebase() {
        launch {
            db.collection("${userID}result")
                .get()
                .addOnSuccessListener { result ->
                    val results = result.map { document ->
                        val resultId = document.getLong("id")
                        val resultName = document.getString("name")
                        val resultType = document.getString("type")
                        val resultGoalMinutes = document.getLong("goalMinutes")?.toInt()
                        val resultGoalSeconds = document.getLong("goalSeconds")?.toInt()
                        val resultGoal = document.getString("goal")
                        val resultMetric = document.getString("metric")
                        val resultTime = document.getString("time")
                        val resultAchievedMinutes = document.getLong("achievedMinutes")?.toInt()
                        val resultAchievedSeconds = document.getLong("achievedSeconds")?.toInt()
                        val resultAchieved = document.getBoolean("achieved")
                        Result("").apply {
                            id = resultId
                            name = resultName
                            type = resultType
                            goalMinutes = resultGoalMinutes
                            goalSeconds = resultGoalSeconds
                            goal = resultGoal
                            metric = resultMetric
                            time = resultTime
                            achievedMinutes = resultAchievedMinutes
                            achievedSeconds = resultAchievedSeconds
                            achieved = resultAchieved

                        }
                    }
                    _resultsUIState.value = ResultsUIState.ResultsLoaded(results)
                }
        }
    }

    val resultsUIState: StateFlow<ResultsUIState> = _resultsUIState

    fun loadResults() {
        launch {
            resultsRepository.getAll().collect{
                _resultsUIState.value = ResultsUIState.ResultsLoaded(it)
            }
        }
    }
}