package com.mendelu.xmoric.tuttimer.ui.elements

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import com.mendelu.xmoric.tuttimer.navigation.INavigationRouter

@Composable
fun BackIcon(
    navigation: INavigationRouter
) {
    IconButton(
        onClick = { navigation.returnBack() }
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = null,
        )
    }
}