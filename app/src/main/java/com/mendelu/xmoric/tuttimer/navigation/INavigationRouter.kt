package com.mendelu.xmoric.tuttimer.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()
    fun navigateToProfile()
    fun navigateToTimer()
    fun navigateToActivities()
    fun navigateToActivityCreation()
    fun navigateToActivityDetail(id: Long?)
    fun navigateToResults()
    fun navigateToResultDetail(id: Long?)

    fun navigateToSignIn()
    fun navigateToForgot()
    fun navigateToSignUp()
    fun navigateToVerify()
}