package com.ranjan.somiq.app.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import com.ranjan.somiq.shared.resources.Res
import com.ranjan.somiq.shared.resources.calls
import com.ranjan.somiq.shared.resources.chats
import com.ranjan.somiq.shared.resources.profile
import com.ranjan.somiq.shared.resources.updates
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.StringResource

@Stable
sealed interface HomeTab {

    val title: StringResource
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector

    data object ChatLists : HomeTab {
        override val title = Res.string.chats
        override val selectedIcon = Icons.AutoMirrored.Filled.Chat
        override val unselectedIcon = Icons.AutoMirrored.Outlined.Chat
    }

    data object Updates : HomeTab {
        override val title = Res.string.updates
        override val selectedIcon = Icons.Default.Home
        override val unselectedIcon = Icons.Outlined.Home
    }

    data object Calls : HomeTab {
        override val title = Res.string.calls
        override val selectedIcon = Icons.Filled.Call
        override val unselectedIcon = Icons.Outlined.Search
    }

    data object Profile : HomeTab {
        override val title = Res.string.profile
        override val selectedIcon = Icons.Filled.Person
        override val unselectedIcon = Icons.Outlined.Person
    }

    companion object {
        val items = persistentListOf(
            ChatLists,
            Updates,
            Calls,
            Profile
        )

        fun fromIndex(index: Int): HomeTab {
            return items[index]
        }
    }
}

fun HomeTab.getIndex(): Int {
    return HomeTab.items.indexOf(this)
}