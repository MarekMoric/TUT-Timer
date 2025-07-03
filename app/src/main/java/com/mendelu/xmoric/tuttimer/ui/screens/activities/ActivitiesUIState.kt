package com.mendelu.xmoric.tuttimer.ui.screens.activities

import com.mendelu.xmoric.tuttimer.database.Activity

sealed class ActivitiesUIState{
    object Default : ActivitiesUIState()
    class ActivitiesLoaded(val activities: List<Activity>) : ActivitiesUIState()

}
