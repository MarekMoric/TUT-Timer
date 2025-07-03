package com.mendelu.xmoric.tuttimer.ui.elements

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.ui.theme.md_theme_light_primaryContainer
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox(
    activities: List<Activity>
) {

    val context = LocalContext.current
    var expanded: Boolean by rememberSaveable { mutableStateOf(false) }
    var selectedText by rememberSaveable { mutableStateOf("Select an Activity") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor().fillMaxWidth(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xffb9b9b9),
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
                            selectedText = item.name!!
                            expanded = false
                            Toast.makeText(context, item.name!!, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
