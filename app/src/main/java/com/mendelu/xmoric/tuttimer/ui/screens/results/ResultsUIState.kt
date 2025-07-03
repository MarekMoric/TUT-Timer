package com.mendelu.xmoric.tuttimer.ui.screens.results

import com.mendelu.xmoric.tuttimer.database.Result
import com.mendelu.xmoric.tuttimer.ui.screens.activities.ActivitiesUIState

sealed class ResultsUIState{
    object Default : ResultsUIState()
    class ResultsLoaded(val results: List<Result>) : ResultsUIState()

}