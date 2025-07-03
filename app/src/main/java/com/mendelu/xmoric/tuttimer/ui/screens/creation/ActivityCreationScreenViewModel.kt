package com.mendelu.xmoric.tuttimer.ui.screens.creation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.database.IActivityLocalRepository
import com.mendelu.xmoric.ukol2.architecture.BaseViewModel
import kotlinx.coroutines.launch

class ActivityCreationScreenViewModel(
    private val activityRepository: IActivityLocalRepository
    ): BaseViewModel() {

    var activity: Activity = Activity("")

    val userID = FirebaseAuth.getInstance().currentUser?.uid //na zistenie id prihlaseneho uzivatela

    val db = Firebase.firestore

    fun saveActivityFirebase() {
        launch {
            db.collection("$userID")
                .add(activity)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        }
    }

    fun saveActivity() {
        launch {
            activityRepository.insert(activity)
        }
    }
}
