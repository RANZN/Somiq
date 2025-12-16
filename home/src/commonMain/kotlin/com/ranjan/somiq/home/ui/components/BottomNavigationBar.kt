package com.ranjan.somiq.home.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
        // For Profile, check if route contains "Profile" (with or without userId)
        if (routeName == "Profile") {
            return currentRoute?.contains("Profile", ignoreCase = true) == true
        }
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
            icon = { Icon(Icons.Default.Add, contentDescription = "Create Post") },
            label = { Text("Create") },
            selected = isSelected(Screen.Home.CreatePost),
            onClick = { onNavigate(Screen.Home.CreatePost) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Reels") },
            label = { Text("Reels") },
            selected = isSelected(Screen.Home.Reels),
            onClick = { onNavigate(Screen.Home.Reels) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = isSelected(Screen.Home.Profile(null)),
            onClick = { onNavigate(Screen.Home.Profile(null)) }
        )
    }
}

