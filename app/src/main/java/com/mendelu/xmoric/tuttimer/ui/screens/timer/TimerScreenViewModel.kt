package com.mendelu.xmoric.tuttimer.ui.screens.timer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.authentication.AuthRepository
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.database.Result
import com.mendelu.xmoric.tuttimer.database.IActivityLocalRepository
import com.mendelu.xmoric.tuttimer.database.IResultLocalRepository
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerScreenViewModel(
    private val activityRepository: IActivityLocalRepository,
    private val resultRepository: IResultLocalRepository
): BaseViewModel() {

    private val _timerUIState
            = MutableStateFlow<TimerUIState>(TimerUIState.Default)

    val timerUIState: StateFlow<TimerUIState> = _timerUIState

    private val planking: Activity = Activity("")
    private val running: Activity = Activity("")

    val userID = FirebaseAuth.getInstance().currentUser?.uid //na zistenie id prihlaseneho uzivatela

    val db = Firebase.firestore

    var textFromSpeech: String? by mutableStateOf("")

    fun addIfEmptyFirebase(){
        launch {
            db.collection("$userID")
                .get()
                .addOnSuccessListener { result ->
                    val activitiesExist = result.documents.isNotEmpty()

                    if (!activitiesExist) {
                        planking.minutes = 2
                        planking.seconds = 30
                        planking.goal = "02:30"
                        planking.name = "Planking"
                        planking.type = "Surpass"
                        planking.metric = ""
                        planking.id = 1

                        running.minutes = 4
                        running.seconds = 0
                        running.goal = "04:00"
                        running.name = "Running"
                        running.type = "Speedy"
                        running.metric = "1 km run"
                        running.id = 2

                        db.collection("$userID")
                            .add(planking)
                            .addOnSuccessListener { documentReference ->
                                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error adding activity", e)
                            }
                        db.collection("$userID")
                            .add(running)
                            .addOnSuccessListener { documentReference ->
                                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error adding activity", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error getting documents.", e)
                }
        }
    }

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
                    _timerUIState.value = TimerUIState.ActivitiesLoaded(activities)
                }
        }
    }

    fun loadActivities() {
        launch {
            activityRepository.getAll().collect{
                _timerUIState.value = TimerUIState.ActivitiesLoaded(it)
            }
        }
    }

    fun addIfEmpty() {
        planking.minutes = 2
        planking.seconds = 30
        planking.goal = "02:30"
        planking.name = "Planking"
        planking.type = "Surpass"
        planking.metric = ""
        planking.id = 1

        running.minutes = 4
        running.seconds = 0
        running.goal = "04:00"
        running.name = "Running"
        running.type = "Speedy"
        running.metric = "1 km run"
        running.id = 2
        launch {
            activityRepository.insert(planking)
            activityRepository.insert(running)
        }
    }

    var result: Result = Result("")

    fun saveResultFirebase() {
        launch {
            db.collection("${userID}result")
                .add(result)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        }
    }

    fun saveResult() {
        launch {
            resultRepository.insert(result)
        }
    }

}