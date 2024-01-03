package com.askolds.homeinventory.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

private const val DURATION: Int = 200

fun defaultEnterTransition(context: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return  fadeIn(
        animationSpec = tween(
            DURATION, easing = LinearEasing
        )
    ) + context.slideIntoContainer(
        animationSpec = tween(DURATION),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )

}

fun defaultExitTransition(context: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return  fadeOut(
        animationSpec = tween(
            DURATION, easing = LinearEasing
        )
    ) + context.slideOutOfContainer(
        animationSpec = tween(DURATION),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )

}