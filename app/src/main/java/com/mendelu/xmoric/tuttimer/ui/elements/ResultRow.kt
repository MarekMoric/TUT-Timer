package com.mendelu.xmoric.tuttimer.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.R
import com.mendelu.xmoric.tuttimer.database.Result
import com.mendelu.xmoric.tuttimer.ui.theme.bottom_bar_selected_color
import com.mendelu.xmoric.tuttimer.ui.theme.green_timer_color
import com.mendelu.xmoric.tuttimer.ui.theme.orange_timer_color
import com.mendelu.xmoric.tuttimer.ui.theme.red_timer_color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultRow(result: Result,
                onRowClick: () -> Unit = {}) {

    Surface(shape = RoundedCornerShape(20),
        shadowElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(70.dp),
        color = bottom_bar_selected_color,
        onClick = onRowClick

    ) {

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {

            Column(
                modifier = Modifier
                    .weight(0.10f)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.Start) {
                //Icon
                when(result.type){
                    "Surpass" -> {
                        Icon(painter = painterResource(id = R.drawable.surpass_icon),
                            contentDescription = "surpass_icon",
                            tint = Color.Unspecified)
                    }
                    "Speedy" -> {
                        Icon(painter = painterResource(id = R.drawable.speedy_icon),
                            contentDescription = "speedy_icon",
                            tint = Color.Unspecified)
                    }
                    "TUT" -> {
                        Icon(painter = painterResource(id = R.drawable.tut_icon),
                            contentDescription = "speedy_icon",
                            tint = Color.Unspecified)
                    }
                    else -> {
                        Icon(painter = painterResource(id = R.drawable.tut_icon),
                            contentDescription = "tut_icon",
                            tint = Color.Unspecified)
                    }
                }
            }

            Column(modifier = Modifier
                .weight(0.50f)
                .padding(start = 10.dp)) {
                //Text
                Text(text = result.name!!,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge)

                Text(text = result.goal.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge)
                result.time?.let {
                    Text(text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (result.achieved == true && result.time != result.goal) green_timer_color
                                else if (result.achieved == false && result.time != result.goal) red_timer_color
                                else orange_timer_color
                    )
                }
            }

//            Column(modifier = Modifier.weight(0.25f),  horizontalAlignment = Alignment.End) {
//                if (image != null){
//
//                    Box(modifier = Modifier
//                        .padding(10.dp)
//                        .border(BorderStroke(1.dp, color = Color.Black), shape = RoundedCornerShape(10.dp))
//                        .clip(RoundedCornerShape(10.dp))
//                        .width(100.dp)
//                        .height(100.dp)
//
//                    ) {
//                        Image(bitmap = image.asImageBitmap(),
//                            contentScale = ContentScale.FillBounds,
//                            contentDescription = activity.name)
//                    }
//
//                }else{
//                    Box(modifier = Modifier
//                        .padding(10.dp)
//                        .clip(RoundedCornerShape(10.dp))
//                        .width(100.dp)
//                        .height(100.dp)
//                        .background(Color.Black)
//                    ) {
//                        Text(text = "PLACEHOLDER", color = Color.White, fontSize = 10.sp)
//                    }
//                }
//
//            }
        }
    }
}