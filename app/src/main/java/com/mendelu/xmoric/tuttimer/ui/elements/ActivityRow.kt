package com.mendelu.xmoric.tuttimer.ui.elements

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mendelu.xmoric.tuttimer.R
import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.ui.theme.bottom_bar_selected_color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityRow(activity: Activity,
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
                when(activity.type){
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
                Text(text = activity.name!!,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge)

                Text(text = activity.goal.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge)

                activity.metric?.let {
                    Spacer(modifier = Modifier.padding(end = 10.dp))
                    Text(text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.labelMedium
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
