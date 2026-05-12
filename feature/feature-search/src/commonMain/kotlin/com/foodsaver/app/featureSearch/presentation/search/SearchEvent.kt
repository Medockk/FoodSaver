package com.foodsaver.app.featureSearch.presentation.search

import androidx.compose.ui.text.input.TextFieldValue
import com.foodsaver.app.featureSearch.domain.model.RecentKeywordsModel

sealed interface SearchEvent {

    data class OnQueryChange(val query: TextFieldValue): SearchEvent
    data class OnRecentKeywordsClick(val word: RecentKeywordsModel): SearchEvent

    data object OnClearQueryClick: SearchEvent
    data class OnSearch(val query: String): SearchEvent
    data object OnSearchIconClick: SearchEvent
    data object OnFilterClick: SearchEvent
}