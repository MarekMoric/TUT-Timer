package com.mendelu.xmoric.tuttimer.ui.screens.resultDetail

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.database.Result
import com.mendelu.xmoric.tuttimer.ui.screens.detail.DetailUIState
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ResultDetailViewModel: BaseViewModel()  {

    var resultId: Long? = null
    var result: Result = Result("")

    private val _detailUIState
            = MutableStateFlow<DetailUIState>(DetailUIState.Default)

    val detailUIState: StateFlow<DetailUIState> = _detailUIState

    val userID = FirebaseAuth.getInstance().currentUser?.uid //na zistenie id prihlaseneho uzivatela

    val db = Firebase.firestore

    fun initResultFirebase() {
        if (resultId != null) {
            val collectionRef = db.collection("${userID}result")

            launch {
                try {
                    val querySnapshot = collectionRef.get().await()
                    for (document in querySnapshot.documents) {
                        if (document.getLong("id") == resultId){
                            result.id = document.getLong("id")
                            result.name = document.getString("name")
                            result.type = document.getString("type")
                            result.goalMinutes = document.getLong("goalMinutes")?.toInt()
                            result.goalSeconds = document.getLong("goalSeconds")?.toInt()
                            result.goal = document.getString("goal")
                            result.metric = document.getString("metric")
                            result.time = document.getString("time")
                            result.achievedMinutes = document.getLong("achievedMinutes")?.toInt()
                            result.achievedSeconds = document.getLong("achievedSeconds")?.toInt()
                            result.achieved = document.getBoolean("achieved")
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

    fun deleteResultFirebase() {
        if (resultId != null) {
            val collectionRef = db.collection("${userID}result")

            launch {
                try {
                    val querySnapshot = collectionRef.get().await()
                    for (document in querySnapshot.documents) {
                        if (document.getLong("id") == resultId) {
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

    var allSeconds: Int = 0
    var numberOf: Int = 0

    fun getAllByName() {
        if (resultId != null) {
            val collectionRef = db.collection("${userID}result")

            launch {
                try {
                    val querySnapshot = collectionRef.get().await()
                    for (document in querySnapshot.documents) {
                        if (document.getString("name") == result.name) {
                            allSeconds += (document.getLong("achievedMinutes")?.toInt()!! * 60 + document.getLong("achievedSeconds")?.toInt()!!)
                            numberOf++
                        }
                    }
                    allSeconds /= numberOf
                } catch (e: Exception) {
                    // Handle any exceptions that occur
                    println("Error: ${e.message}")
                }
            }
        }
    }
}