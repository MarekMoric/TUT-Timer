package com.mendelu.xmoric.tuttimer.ui.screens.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mendelu.xmoric.tuttimer.R
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.BottomNavBar
import com.mendelu.xmoric.tuttimer.ui.theme.*
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.viewModel
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(navigation: INavigationRouter, viewModel: TimerScreenViewModel = getViewModel()) {

    val activities = remember { mutableListOf<Activity>()}
    val timerUIState: TimerUIState? by viewModel.timerUIState.collectAsState()

    var isNotLoading: Boolean by rememberSaveable { mutableStateOf(false) }

    timerUIState?.let {
        when(it){
            TimerUIState.Default -> {
                LaunchedEffect(it){
                    viewModel.loadActivitiesFirebase()
//                    viewModel.loadActivities()
                }
            }
            is TimerUIState.ActivitiesLoaded -> {
                activities.clear()
                activities.addAll(it.activities)
                if (activities.isEmpty()){
                    viewModel.addIfEmptyFirebase()
//                    viewModel.addIfEmpty()
                    isNotLoading = true
                }

            }
        }
    }

    Scaffold(
        content =  { paddingValues ->
            TimerScreenContent(modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding()),
                navigation = navigation, activities = activities, isNotLoading = isNotLoading, viewModel = viewModel)},
        bottomBar = { BottomNavBar(navigation = navigation) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreenContent(modifier: Modifier, navigation: INavigationRouter, activities: List<Activity>, isNotLoading: Boolean, viewModel: TimerScreenViewModel) {

    //MARK: Dropdown Menu properties
    val context = LocalContext.current
    var expanded: Boolean by rememberSaveable { mutableStateOf(false) }
    var selectedActivity by rememberSaveable { mutableStateOf(Activity("")) }
    var selectedText by rememberSaveable { mutableStateOf("Select an Activity") }

    LaunchedEffect(activities) {
        if (activities.isNotEmpty()) {
            selectedActivity = activities.first()
        }
    }

    //MARK: Switch properties
    var speech: Boolean by rememberSaveable { mutableStateOf(false) }

    //MARK: Timer properties
    val timerModifier = Modifier.size(300.dp)
    val handleColor: Color = Color.Black
    var inactiveBarColor: Color by remember { mutableStateOf(orange_timer_color) }
    var activeBarColor: Color by remember { mutableStateOf(orange_timer_color) }
    val strokeWidth: Dp = 8.dp
    var size by remember { mutableStateOf(IntSize.Zero) }

    var goalTime by rememberSaveable { mutableStateOf(30L) }
    var value: Float by rememberSaveable { mutableStateOf(1f) }
    var pointerTime by rememberSaveable { mutableStateOf(goalTime) }
    var realTime by rememberSaveable { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    var middleTextMinutes by rememberSaveable { mutableStateOf("00") }
    var middleTextSeconds by rememberSaveable { mutableStateOf("00") }
    var minutesAchieved by rememberSaveable { mutableStateOf(0) }
    var secondsAchieved by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        //MARK: Dropdown Menu from https://alexzh.com/jetpack-compose-dropdownmenu/
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    enabled = !isTimerRunning,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor().fillMaxWidth(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = md_theme_light_primaryContainer,
                            shape = CircleShape
                        )
                        .border(1.dp, color = Color.Black, shape = RoundedCornerShape(80))
                        .menuAnchor(),
                    shape = RoundedCornerShape(80),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(color = md_theme_light_primaryContainer)
                ) {
                    activities.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.name!!) },
                            onClick = {
                                selectedActivity = item
                                Log.d("minutes", selectedActivity.minutes.toString())
                                Log.d("seconds", selectedActivity.seconds.toString())
                                selectedText = item.name!!
                                expanded = false
                                inactiveBarColor = if (selectedActivity.type == "Surpass" || selectedActivity.type == "TUT") {
                                    green_timer_color
                                } else {
                                    red_timer_color
                                }

                                activeBarColor = if (selectedActivity.type == "Surpass" || selectedActivity.type == "TUT") {
                                    red_timer_color
                                } else {
                                    green_timer_color
                                }

                                middleTextMinutes = if (selectedActivity.minutes!! in 0..9) {
                                    "0${selectedActivity.minutes!!}"
                                } else {
                                    "${selectedActivity.minutes!!}"
                                }

                                middleTextSeconds = if (selectedActivity.seconds!! in 0..9) {
                                    "0${selectedActivity.seconds!!}"
                                } else {
                                    "${selectedActivity.seconds!!}"
                                }

                                goalTime = ((selectedActivity.seconds!! + 60*selectedActivity.minutes!!) * 10L)

                            },
                            enabled = !isTimerRunning
                        )
                    }
                }
            }
        }
        //MARK: End of Dropdown Menu

        //MARK: Speech Switch
//        Row(horizontalArrangement = Arrangement.End,
//        modifier = Modifier
//            .padding(end = 16.dp)
//            .fillMaxWidth()){
//
//            Switch(
//                checked = speech,
//                onCheckedChange = {
//                    speech = !speech
//                    if (speech) {
//                        startSpeechToText(viewModel, context){
//                            speech = false
//                        }
//                    }
//                },
//                thumbContent = {
//                    Image(
//                        painter = painterResource(if (speech) R.drawable.ic_mic else R.drawable.ic_mic_off),
//                        contentDescription = "Switch icon"
//                    )
//                },
//                colors = SwitchDefaults.colors(/*todo*/)
//
//            )
//        }


        Spacer(modifier = Modifier.size(40.dp))

        //MARK: Timer from https://www.geeksforgeeks.org/how-to-create-a-timer-using-jetpack-compose-in-android/
        LaunchedEffect(key1 = pointerTime, key2 = isTimerRunning) {
            if (isTimerRunning && pointerTime > 0) {
                delay(77L)
                pointerTime -= 1
                value = pointerTime / goalTime.toFloat()
//                Log.d("valju", value.toString())
            }
        }

//        LaunchedEffect(key1 = pointerTime, key2 = isTimerRunning) { //funguje skokovo ale aspon presne
//            if (isTimerRunning && pointerTime > 0) {
//                repeat((pointerTime).toInt()) {
//                    delay(1000L)
//                    pointerTime -= 1
//                    value = pointerTime / goalTime.toFloat()
//                    Log.d("valju", value.toString())
//                }
//            }
//        }

        LaunchedEffect(key1 = realTime, key2 = isTimerRunning) {
            if(isTimerRunning) {
                delay(1000L)
                realTime += 1
                secondsAchieved += 1
                if(secondsAchieved > 59){
                    minutesAchieved += 1
                    secondsAchieved = 0
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = timerModifier
                .onSizeChanged {
                    size = it
                }
        ) {
            // draw the timer
            Canvas(modifier = timerModifier) {
                // draw the inactive arc with following parameters
                drawArc(
                    color = inactiveBarColor, // assign the color
                    startAngle = -215f, // assign the start angle
                    sweepAngle = 250f, // arc angles
                    useCenter = false, // prevents our arc to connect at te ends
                    size = Size(size.width.toFloat(), size.height.toFloat()),

                    // to make ends of arc round
                    style = Stroke(strokeWidth.toPx() - 1f, cap = StrokeCap.Round)
                )
                // draw the active arc with following parameters
                drawArc(
                    color = activeBarColor, // assign the color
                    startAngle = -215f,  // assign the start angle
                    sweepAngle = 250f * value, // reduce the sweep angle
                    // with the current value
                    useCenter = false, // prevents our arc to connect at te ends
                    size = Size(size.width.toFloat(), size.height.toFloat()),

                    // to make ends of arc round
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                // calculate the value from arc pointer position
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta = (250f * value + 145f) * (PI / 180f).toFloat()
                val r = size.width / 2f
                val a = cos(beta) * r
                val b = sin(beta) * r
                // draw the circular pointer/ cap
                drawPoints(
                    listOf(Offset(center.x + a, center.y + b)),
                    pointMode = PointMode.Points,
                    color = handleColor,
                    strokeWidth = (strokeWidth * 3f).toPx(),
                    cap = StrokeCap.Round  // make the pointer round
                )
            }
            if (isTimerRunning){
                Text(
                    text = "Goal: $middleTextMinutes:$middleTextSeconds",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 120.dp)
                )
            }

            // MARK: Middle text
            Text(
                text = if(!isTimerRunning) "$middleTextMinutes:$middleTextSeconds"
                    else if (secondsAchieved in 0 .. 9 && minutesAchieved in 0 .. 9) "0$minutesAchieved:0$secondsAchieved"
                    else if (minutesAchieved in 0 .. 9) "0$minutesAchieved:$secondsAchieved"
                    else if (secondsAchieved in 0 .. 9) "$minutesAchieved:0$secondsAchieved"
                    else "$minutesAchieved:$secondsAchieved",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // create button to start or stop the timer
        if(!isTimerRunning){
            Button(
                onClick = {
                    Log.d("activity", "${selectedActivity.minutes} and ${selectedActivity.seconds}")
                    pointerTime = goalTime
                    realTime = 0
                    secondsAchieved = 0
                    minutesAchieved = 0
                    isTimerRunning = true

                },
                // change button color
                colors = ButtonDefaults.buttonColors(
                    containerColor = button_color
                ),
                enabled = selectedText != "Select an Activity"
            ) {
                Text(
                    // change the text of button based on values
                    text = "Start"
                )
            }
        }else{
            Spacer(modifier = Modifier.size(80.dp))

            Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        isTimerRunning = !isTimerRunning
                        pointerTime = goalTime
                        realTime = 0
                        secondsAchieved = 0
                        minutesAchieved = 0
                        value = 1f
                    },
                    // change button color
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text(
                        // change the text of button based on values
                        text = "Cancel"
                    )
                }

                Button(
                    onClick = {
                        viewModel.result.id = Random.nextLong(1L, 1000000L)
                        viewModel.result.name = selectedActivity.name
                        viewModel.result.type = selectedActivity.type
                        viewModel.result.goalMinutes = selectedActivity.minutes
                        viewModel.result.goalSeconds = selectedActivity.seconds
                        viewModel.result.goal = selectedActivity.goal
                        viewModel.result.metric = selectedActivity.metric
                        viewModel.result.time = if (secondsAchieved in 0 .. 9 && minutesAchieved in 0 .. 9) "0$minutesAchieved:0$secondsAchieved"
                        else if (minutesAchieved in 0 .. 9) "0$minutesAchieved:$secondsAchieved"
                        else if (secondsAchieved in 0 .. 9) "$minutesAchieved:0$secondsAchieved"
                        else "$minutesAchieved:$secondsAchieved"
                        viewModel.result.achievedMinutes = minutesAchieved
                        viewModel.result.achievedSeconds = secondsAchieved
                        viewModel.result.achieved =
                            if (selectedActivity.type == "TUT" || selectedActivity.type == "Surpass") {
                                realTime > (selectedActivity.minutes!!*60 + selectedActivity.seconds!!)
                            } else {
                                realTime < (selectedActivity.minutes!!*60 + selectedActivity.seconds!!)
                            }

                        viewModel.saveResultFirebase()
                        Toast.makeText(context, "Result saved!", Toast.LENGTH_SHORT).show()

                        isTimerRunning = !isTimerRunning
                        pointerTime = goalTime
                        realTime = 0
                        secondsAchieved = 0
                        minutesAchieved = 0
                        value = 1f
                    },
                    // change button color
                    colors = ButtonDefaults.buttonColors(
                        containerColor = button_color
                    )
                ) {
                    Text(
                        // change the text of button based on values
                        text = "Save"
                    )
                }
            }

        }

        //MARK: End of Timer

    }

}

fun startSpeechToText(vm: TimerScreenViewModel, ctx: Context, finished: ()-> Unit) {
    Log.d("start", "start")
    val isSpeechAvailable = SpeechRecognizer.isRecognitionAvailable(ctx)
    Log.d("start", "$isSpeechAvailable")
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx)
    val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    speechRecognizerIntent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
    )

    // Optionally you can add another language
//    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el_GR")

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle?) {
            Log.d("on ready", "ready")
        }
        override fun onBeginningOfSpeech() {
            Log.d("on beginning", "begin")
        }
        override fun onRmsChanged(v: Float) {
            Log.d("rms change", "change")
        }
        override fun onBufferReceived(bytes: ByteArray?) {
            Log.d("received buffer", "buffer")
        }
        override fun onEndOfSpeech() {
            Log.d("end of speech", "end")
            finished()
            // changing the color of your mic icon to
            // gray to indicate it is not listening or do something you want
        }

        override fun onError(i: Int) {
            Log.d("error", "$i")
        }

        override fun onResults(bundle: Bundle) {
            val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Log.d("result", result?.get(0) ?: "nothing")
            if (result != null) {
                // attaching the output
                // to our viewmodel
                vm.textFromSpeech = result[0]
            }
        }

        override fun onPartialResults(bundle: Bundle) {}
        override fun onEvent(i: Int, bundle: Bundle?) {}

    })
    Handler(Looper.getMainLooper()).postDelayed({
        speechRecognizer.startListening(speechRecognizerIntent)
    }, 500)
//    speechRecognizer.startListening(speechRecognizerIntent)
}