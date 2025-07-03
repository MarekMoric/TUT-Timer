package com.mendelu.xmoric.tuttimer.ui.screens.detail

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.database.IActivityLocalRepository
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ActivityDetailScreenViewModel(
    private val activityRepository: IActivityLocalRepository
): BaseViewModel() {

    var activityId: Long? = null
    var activity: Activity = Activity("")

    private val _detailUIState
            = MutableStateFlow<DetailUIState>(DetailUIState.Default)

    val detailUIState: StateFlow<DetailUIState> = _detailUIState

    val userID = FirebaseAuth.getInstance().currentUser?.uid //na zistenie id prihlaseneho uzivatela

    val db = Firebase.firestore

    fun initActivityFirebase() {
        if (activityId != null) {
            val collectionRef = db.collection("$userID")

            launch {
                try {
                    val querySnapshot = collectionRef.get().await()
                    for (document in querySnapshot.documents) {
                        if (document.getLong("id") == activityId){
                            activity.id = document.getLong("id")
                            activity.name = document.getString("name")
                            activity.type = document.getString("type")
                            activity.minutes = document.getLong("minutes")?.toInt()
                            activity.seconds = document.getLong("seconds")?.toInt()
                            activity.goal = document.getString("goal")
                            activity.metric = document.getString("metric")
                        }
                    }
                    _detailUIState.value = DetailUIState.ActivityLoaded
                } catch (e: Exception) {
                    // Handle any exceptions that occur
                    println("Error: ${e.message}")
                }
            }
        }
    }

    fun updateActivityFirebase() {
        if (activityId != null) {
            val collectionRef = db.collection("$userID")

            launch {
                try {
                    val querySnapshot = collectionRef.get().await()
                    for (document in querySnapshot.documents) {
                        if (document.getLong("id") == activityId) {
                            val docRef = collectionRef.document(document.id)
                            docRef.update("name", activity.name)
                            docRef.update("type", activity.type)
                            docRef.update("minutes", activity.minutes)
                            docRef.update("seconds", activity.seconds)
                            docRef.update("goal", activity.goal)
                            docRef.update("metric", activity.metric)
                        }
                    }
                    _detailUIState.value = DetailUIState.ActivitySaved
                } catch (e: Exception) {
                    // Handle any exceptions that occur
                    println("Error: ${e.message}")
                }
            }
        }
    }

    fun deleteActivityFirebase() {
        if (activityId != null) {
            val collectionRef = db.collection("$userID")

            launch {
                try {
                    val querySnapshot = collectionRef.get().await()
                    for (document in querySnapshot.documents) {
                        if (document.getLong("id") == activityId) {
                            val docRef = collectionRef.document(document.id)
                            docRef.delete()
                        }
                    }
                    _detailUIState.value = DetailUIState.ActivityRemoved
                } catch (e: Exception) {
                    // Handle any exceptions that occur
                    println("Error: ${e.message}")
                }
            }
        }
    }

    fun initActivity() {
        if (activityId != null){
            launch {
                activity = activityRepository.findById(id = activityId!!)
                _detailUIState.value = DetailUIState.ActivityLoaded
            }
        }else{
            _detailUIState.value = DetailUIState.ActivityError(-666)
        }
    }

    fun updateActivity() {
        launch {
            //activityRepository.update(activity)
            _detailUIState.value = DetailUIState.ActivitySaved
        }
    }

    fun deleteActivity() {
        launch {
            //activityRepository.delete(activity)
            _detailUIState.value = DetailUIState.ActivityRemoved
        }
    }
}