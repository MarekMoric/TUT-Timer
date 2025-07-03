package com.mendelu.xmoric.tuttimer.ui.screens.detail

sealed class DetailUIState{
    object Default : DetailUIState()
    object ActivityLoaded : DetailUIState()
    object ActivitySaved : DetailUIState()
    class ActivityError(val error: Int) : DetailUIState()
    object ActivityRemoved : DetailUIState()

}
