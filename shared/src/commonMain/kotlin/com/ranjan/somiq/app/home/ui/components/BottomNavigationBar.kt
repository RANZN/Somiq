package com.ranjan.somiq.app.home.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ranjan.somiq.app.home.ui.HomeTab
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(
    tabs: PersistentList<HomeTab> = HomeTab.items,
    currentTab: () -> HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        tabs.forEach { item ->
            val selected = item == currentTab()

            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(item) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = stringResource(item.title),
                    )
                },
                label = {
                    Text(stringResource(item.title))
                },
            )
        }
    }
}
