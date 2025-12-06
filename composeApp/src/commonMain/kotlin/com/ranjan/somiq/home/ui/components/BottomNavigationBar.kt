package com.ranjan.somiq.home.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ranjan.somiq.core.presentation.navigation.Screen

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    fun isSelected(route: Screen.Home): Boolean {
        val routeName = route::class.simpleName ?: ""
        return currentRoute?.contains(routeName, ignoreCase = true) == true
    }

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = isSelected(Screen.Home.Feed),
            onClick = { onNavigate(Screen.Home.Feed) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = isSelected(Screen.Home.Search),
            onClick = { onNavigate(Screen.Home.Search) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Reels") },
            label = { Text("Reels") },
            selected = isSelected(Screen.Home.Reels),
            onClick = { onNavigate(Screen.Splash) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = isSelected(Screen.Home.Profile),
            onClick = { onNavigate(Screen.Home.Profile) }
        )
    }
}

