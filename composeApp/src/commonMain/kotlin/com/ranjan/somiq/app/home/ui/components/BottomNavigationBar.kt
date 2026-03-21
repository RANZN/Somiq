package com.ranjan.somiq.app.home.ui.components

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
import com.ranjan.somiq.core.presentation.navigation.Home

@Composable
fun BottomNavigationBar(
    currentTab: Home,
    onTabSelected: (Home) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentTab == Home.Feed,
            onClick = { onTabSelected(Home.Feed) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = currentTab == Home.Search,
            onClick = { onTabSelected(Home.Search) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Reels") },
            label = { Text("Reels") },
            selected = currentTab == Home.Reels,
            onClick = { onTabSelected(Home.Reels) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentTab is Home.Profile,
            onClick = { onTabSelected(Home.Profile(null)) }
        )
    }
}
