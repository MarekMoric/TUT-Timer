package com.mendelu.xmoric.tuttimer.ui.screens.creation

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.constants.Constants
//import com.chargemap.compose.numberpicker.NumberPicker
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter
import com.mendelu.xmoric.tuttimer.ui.elements.BackArrowScreen
import com.mendelu.xmoric.tuttimer.ui.elements.NumberPicker
import com.mendelu.xmoric.tuttimer.ui.elements.SegmentedControl
import com.mendelu.xmoric.tuttimer.ui.theme.bottom_bar_selected_color
import com.mendelu.xmoric.tuttimer.ui.theme.button_color
import com.mendelu.xmoric.tuttimer.ui.theme.md_theme_light_error
import org.koin.androidx.compose.getViewModel
import kotlin.random.Random

@Composable
fun ActivityCreationScreen(navigation: INavigationRouter,
                           viewModel: ActivityCreationScreenViewModel = getViewModel()
) {

//    Scaffold(
//        content =  { paddingValues ->
//            CreationScreenContent(modifier = Modifier.padding(
//                bottom = paddingValues.calculateBottomPadding()),
//                navigation = navigation)},
//        bottomBar = { BottomNavBar(navigation = navigation) },
//        )

    BackArrowScreen(
        topBarText = "Create Activity",
        content = {
            CreationScreenContent(
                navigation = navigation,
            viewModel = viewModel) },
        onBackClick = {
            navigation.getNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.REFRESH_SCREEN, true)
            navigation.returnBack()
                      },
        actions = {

        },
        showBackArrow = true)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationScreenContent(navigation: INavigationRouter, viewModel: ActivityCreationScreenViewModel) {

    val activities = listOf("Surpass", "Speedy", "TUT")
    val goals = listOf("30s", "60s", "90s")
    var actType by rememberSaveable { mutableStateOf("Surpass") }
    var actName by rememberSaveable { mutableStateOf("") }
    var actMetric by rememberSaveable { mutableStateOf("") }
    var actMinutes by rememberSaveable { mutableStateOf(2) }
    var actSeconds by rememberSaveable { mutableStateOf(45) }

    val context = LocalContext.current

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
            defaultSelectedItemIndex = 0,
            cornerRadius = 80
        ) { selectedItemIndex ->
            actType = activities[selectedItemIndex]
        }

        Row(horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)) {
            Text(
                text = "Name your activity",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 24.dp))

        TextField(
            value = actName,
            onValueChange = { actName = it},
            singleLine = true,
            label = { Text(text = "Activity name")},
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = bottom_bar_selected_color
            ),
        )

        Row(horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)) {
            Text(
                text = "Set goal",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 24.dp))

        if (actType != "TUT"){
            Row {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(bottom_bar_selected_color)
                    .padding(8.dp)){
                    NumberPicker(
                        state = remember { mutableStateOf(2) },
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
                        state = remember { mutableStateOf(45) },
                        range = 0..59,
                        onStateChanged = { newNumber -> actSeconds = newNumber
                        }
                    )
                }
            }
        } else {
            SegmentedControl(
                items = goals,
                defaultSelectedItemIndex = 0,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)) {
            Text(
                text = "Activity metric",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 8.dp))

        OutlinedTextField(
            value = actMetric,
            onValueChange = { actMetric = it},
            label = { Text(text = "Input distance, weight or height if applicable.")},
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
                viewModel.activity.id = Random.nextLong(3L, 1000000L)
                viewModel.activity.type = actType
                viewModel.activity.metric = actMetric
                viewModel.activity.name = actName
                viewModel.activity.minutes = actMinutes
                viewModel.activity.seconds = actSeconds
                viewModel.activity.goal = if (actSeconds in 0 .. 9 && actMinutes in 0 .. 9) "0$actMinutes:0$actSeconds"
                else if (actMinutes in 0 .. 9) "0$actMinutes:$actSeconds"
                else if (actSeconds in 0 .. 9) "$actMinutes:0$actSeconds"
                else "$actMinutes:$actSeconds"

                Toast.makeText(context, "$actName created!", Toast.LENGTH_SHORT).show()
                viewModel.saveActivityFirebase()
//                viewModel.saveActivity()
                navigation.getNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.REFRESH_SCREEN, true)
                navigation.returnBack()
                      },
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .padding(end = 8.dp, start = 8.dp, top = 8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = button_color,
                contentColor = Color.White),
            enabled = actName.isNotBlank(),
            content = { Text(text = "Save", style = MaterialTheme.typography.bodySmall) },
        )
    }
}

//uncomment implementation in gradle
//@Composable
//private fun NumberPicker() {
//    var state by remember { mutableStateOf(0) }
//    NumberPicker(
//        value = state,
//        onValueChange = {
//        state = it },
//        range = 0..10)
//}

//od GPT solution, treba tweaking na velkost
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MinutesInputField() {
//    var text by remember { mutableStateOf("") }
//    val change: (String) -> Unit = { value ->
//        if (value.length <= 2) {
//            text = value.filter { it.isDigit() }
//        }
//    }
//    TextField(
//        value = text,
//        modifier = Modifier.width(60.dp).size(80.dp),
//        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
//        onValueChange = change
//    )
//}






