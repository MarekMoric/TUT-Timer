package com.mendelu.xmoric.tuttimer.ui.screens.timer

import com.mendelu.xmoric.tuttimer.database.Activity
import com.mendelu.xmoric.tuttimer.ui.screens.activities.ActivitiesUIState

sealed class TimerUIState {
    object Default : TimerUIState()
    class ActivitiesLoaded(val activities: List<Activity>) : TimerUIState()
}