package com.ranjan.somiq.app.home.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.ranjan.somiq.core.presentation.navigation.Home

private data class BottomNavItem(
    val icon: ImageVector,
    val value: Home,
)

private val bottomNavItems = listOf(
    BottomNavItem(
        icon = Icons.AutoMirrored.Filled.Chat,
        value = Home.ChatLists,
    ),
    BottomNavItem(
        icon = Icons.Default.Home,
        value = Home.Updates,
    ),
    BottomNavItem(
        icon = Icons.Default.Person,
        value = Home.UserProfile,
    )
)

@Composable
fun BottomNavigationBar(
    currentTab: Home,
    onTabSelected: (Home) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        bottomNavItems.forEach { item ->
            val navItem = item.value
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = navItem.name,
                    )
                },
                label = { Text(navItem.name) },
                selected = navItem == currentTab,
                onClick = { onTabSelected(navItem) },
            )
        }
    }
}
