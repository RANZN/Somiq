package com.ranjan.somiq.profile.ui

import androidx.compose.runtime.Stable
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiIntent
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiEffect
import com.ranjan.somiq.core.presentation.viewmodel.BaseUiState
import com.ranjan.somiq.feed.data.model.Post
import com.ranjan.somiq.feed.data.model.Story
import com.ranjan.somiq.profile.data.model.ProfileResponse

enum class ProfileTab { MyStories, Saved }

object ProfileContract {
    @Stable
    data class UiState(
        val profile: ProfileResponse? = null,
        val myPosts: List<Post> = emptyList(),
        val myStories: List<Story> = emptyList(),
        val savedPosts: List<Post> = emptyList(),
        val selectedTab: ProfileTab = ProfileTab.MyStories,
        val isLoading: Boolean = false,
        val error: String? = null,
        val refreshing: Boolean = false,
        val showAppBar: Boolean = false,
        val appBarTitle: String? = null
    ) : BaseUiState {
        val isOwnProfile: Boolean
            get() = profile != null && appBarTitle == null
    }

    sealed interface Intent : BaseUiIntent {
        object LoadProfile : Intent
        object RefreshProfile : Intent
        data class SetAppBarConfig(val show: Boolean, val title: String?) : Intent
        data class SelectTab(val tab: ProfileTab) : Intent
        object OnLogoutClick : Intent
        object ClearError : Intent
        object Retry : Intent
    }

    sealed interface Effect : BaseUiEffect {
        data class NavigateToEditProfile(val userId: String) : Effect
        data class NavigateToSettings(val userId: String) : Effect
        data class NavigateToFollowers(val userId: String) : Effect
        data class NavigateToFollowing(val userId: String) : Effect
        object Logout : Effect
    }
}
