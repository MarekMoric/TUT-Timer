package com.mendelu.xmoric.tuttimer.ui.screens.activities

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.database.IActivityLocalRepository
import com.mendelu.xmoric.tuttimer.ui.screens.timer.TimerUIState
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActivitiesScreenViewModel(
    private val activityRepository: IActivityLocalRepository
): BaseViewModel() {

    private val _activitiesUIState
            = MutableStateFlow<ActivitiesUIState>(ActivitiesUIState.Default)

    val activitiesUIState: StateFlow<ActivitiesUIState> = _activitiesUIState

    val userID = FirebaseAuth.getInstance().currentUser?.uid //na zistenie id prihlaseneho uzivatela

    val db = Firebase.firestore

    fun loadActivitiesFirebase() {
        launch {
            db.collection("$userID")
                .get()
                .addOnSuccessListener { result ->
                    val activities = result.map { document ->
                        val activityId = document.getLong("id")
                        val activityName = document.getString("name")
                        val activityType = document.getString("type")
                        val activityMinutes = document.getLong("minutes")?.toInt()
                        val activitySeconds = document.getLong("seconds")?.toInt()
                        val activityGoal = document.getString("goal")
                        val activityMetric = document.getString("metric")
                        Activity("").apply {
                            id = activityId
                            name = activityName
                            type = activityType
                            minutes = activityMinutes
                            seconds = activitySeconds
                            goal = activityGoal
                            metric = activityMetric
                        }
                    }
                    _activitiesUIState.value = ActivitiesUIState.ActivitiesLoaded(activities)
                }
        }
    }

    fun loadActivities() {
        launch {
            activityRepository.getAll().collect{
                _activitiesUIState.value = ActivitiesUIState.ActivitiesLoaded(it)
            }
        }
    }
}