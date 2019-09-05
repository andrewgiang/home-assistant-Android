package io.homeassistant.android.core

import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDirections

sealed class NavigationEvent {
    data class NavById(@IdRes val id: Int) : NavigationEvent()
    data class NavByDirection(val direction: NavDirections) : NavigationEvent()
}

fun LiveData<Event<NavigationEvent>>.observeNavigationEvents(
    viewLifecycleOwner: LifecycleOwner,
    navController: NavController
) {
    this.observe(viewLifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let { navEvent ->
            when (navEvent) {
                is NavigationEvent.NavById -> navController.navigate(navEvent.id)
                is NavigationEvent.NavByDirection -> navController.navigate(navEvent.direction)
            }
        }
    }

}