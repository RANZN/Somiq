package com.ranjan.somiq.app.home.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
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
import androidx.navigation3.runtime.NavKey
import com.ranjan.somiq.core.presentation.navigation.Home

@Composable
fun BottomNavigationBar(
    currentKey: NavKey?,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val homeKey = currentKey as? Home

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = homeKey == Home.Feed,
            onClick = { onNavigate(Home.Feed) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = homeKey == Home.Search,
            onClick = { onNavigate(Home.Search) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Create Post") },
            label = { Text("Create") },
            selected = homeKey == Home.CreatePost,
            onClick = { onNavigate(Home.CreatePost) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
            label = { Text("Chat") },
            selected = homeKey == Home.Chat,
            onClick = { onNavigate(Home.Chat) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "Reels") },
            label = { Text("Reels") },
            selected = homeKey == Home.Reels,
            onClick = { onNavigate(Home.Reels) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = homeKey is Home.Profile,
            onClick = { onNavigate(Home.Profile(null)) }
        )
    }
}
