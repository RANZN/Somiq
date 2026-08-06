package com.ranjan.somiq.app.home.ui

import androidx.lifecycle.viewModelScope
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToComments
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToConversation
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToCreatePost
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToCreateStory
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToEditProfile
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToFollowers
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToFollowing
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToNewChat
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToNotifications
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToPost
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToSettings
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToStory
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.NavigateToUser
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.ShowMoreOptions
import com.ranjan.somiq.app.home.ui.HomeContract.Effect.ShowShareDialog
import com.ranjan.somiq.app.home.ui.HomeContract.Intent
import com.ranjan.somiq.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
) : BaseViewModel<HomeContract.UiState, Intent, HomeContract.Effect>(
    HomeContract.UiState()
) {

    override fun onIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.SelectTab -> {
                    val currentTab = state.value.selectedTab
                    setState {
                        copy(
                            selectedTab = intent.tab,
                            scrollToTopKey = if (intent.tab == currentTab) scrollToTopKey + 1 else scrollToTopKey
                        )
                    }
                }

                is Intent.SearchQueryChange -> {
                    setState { copy(searchQuery = intent.query) }
                }

                Intent.Setting -> {
                    emitEffect(HomeContract.Effect.Setting)
                }

                // Navigation
                is Intent.NavigateToUser -> emitEffect(NavigateToUser(intent.userId))
                is Intent.NavigateToPost -> emitEffect(NavigateToPost(intent.postId))
                is Intent.NavigateToComments -> emitEffect(NavigateToComments(intent.postId))
                is Intent.NavigateToStory -> emitEffect(NavigateToStory(intent.storyId))
                is Intent.ShowShareDialog -> emitEffect(ShowShareDialog(intent.postId))
                is Intent.ShowMoreOptions -> emitEffect(ShowMoreOptions(intent.postId))
                is Intent.NavigateToEditProfile -> emitEffect(NavigateToEditProfile(intent.userId))
                is Intent.NavigateToSettings -> emitEffect(NavigateToSettings(intent.userId))
                is Intent.NavigateToFollowers -> emitEffect(NavigateToFollowers(intent.userId))
                is Intent.NavigateToFollowing -> emitEffect(NavigateToFollowing(intent.userId))
                is Intent.NavigateToConversation -> emitEffect(NavigateToConversation(intent.userId))
                Intent.NavigateToNotifications -> emitEffect(NavigateToNotifications)
                Intent.CreatePost -> emitEffect(NavigateToCreatePost)
                Intent.NavigateToCreateStory -> emitEffect(NavigateToCreateStory)
                Intent.NewChat -> emitEffect(NavigateToNewChat)
            }
        }
    }
}
