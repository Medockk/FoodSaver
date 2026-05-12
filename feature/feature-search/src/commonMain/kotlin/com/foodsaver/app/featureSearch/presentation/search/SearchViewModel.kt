package com.foodsaver.app.featureSearch.presentation.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.pagination.BasePaginator
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreEnterprises.domain.repository.RestaurantRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.featureSearch.domain.repository.SearchRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val searchRepository: SearchRepository,
    private val restaurantRepository: RestaurantRepository,
    private val productRepository: ReadProductRepository,
    private val cartRepository: CartRepository,
) : BaseViewModel<SearchAction>() {

    private val navArgs = savedStateHandle.toRoute<Route.MainGraph.SearchScreen>()
    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null
    private val pageSize = 15
    private val searchByQueryPaginator = { query: String ->
        BasePaginator(
            initialKey = 0,
            onRequest = { searchRepository.search(query, it, pageSize) },
            onSuccess = { _, result ->
                _state.update {
                    it.copy(
                        searchedProducts = it.searchedProducts + result
                    )
                }
            },
            onError = { },
            onNextKey = { key, _ -> key + 1 },
            onLoadUpdated = { },
            onEndReaching = { _, result -> result.size < pageSize }
        )
    }
    private val searchByCategoryIdPaginator = { id: String ->
        BasePaginator(
            initialKey = 0,
            onRequest = { searchRepository.searchByCategoryId(id, it, pageSize) },
            onSuccess = { _, result ->
                _state.update {
                    it.copy(
                        searchedProducts = it.searchedProducts + result
                    )
                }
            },
            onError = { },
            onNextKey = { key, _ -> key + 1 },
            onLoadUpdated = { },
            onEndReaching = { _, result -> result.size < pageSize }
        )
    }

    init {

        navArgs.categoryName?.let { name ->
            _state.update {
                it.copy(
                    query = TextFieldValue(name),
                    isFirstSearchingScreen = false
                )
            }
        }

        getRecentKeywords()
        search()

        getSuggestedRestaurants()
        getSuggestedProducts()
    }

    private fun getSuggestedProducts() {
        TODO("Not yet implemented")
    }

    private fun getSuggestedRestaurants() {

    }

    private fun search() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            val searchCategoryId = navArgs.searchCategoryId
            val searchQuery = navArgs.query
            if (searchCategoryId != null) {
                searchByCategoryIdPaginator(searchCategoryId)
            } else if (searchQuery != null) {
                searchByQueryPaginator.invoke(searchQuery)
            }
        }
    }

    private fun getRecentKeywords() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            searchRepository.getRecentKeywords()
                .collect { recentKeywords ->
                    _state.update {
                        it.copy(
                            recentKeywords = recentKeywords
                        )
                    }
                }
        }
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            SearchEvent.OnClearQueryClick -> {
                searchJob?.cancel()
                _state.update { it.copy(query = TextFieldValue()) }
            }

            is SearchEvent.OnQueryChange -> {
                _state.update { it.copy(query = event.query) }
            }

            is SearchEvent.OnSearch -> {
                if (event.query.isEmpty()) return

                _state.update {
                    it.copy(
                        isFirstSearchingScreen = false,
                    )
                }
                viewModelScope.launch {
                    val paginator = searchByQueryPaginator.invoke(event.query)
                    _state.update { it.copy(
                        searchedProducts = emptyList()
                    ) }
                    paginator.reset()
                    paginator.loadPage()
                }
            }

            is SearchEvent.OnRecentKeywordsClick -> {
                viewModelScope.launch {
                    val paginator = searchByQueryPaginator.invoke(event.word.value)
                    _state.update { it.copy(
                        searchedProducts = emptyList()
                    ) }
                    paginator.reset()
                    paginator.loadPage()
                }

                _state.update {
                    it.copy(
                        isFirstSearchingScreen = false,
                        query = TextFieldValue(event.word.value)
                    )
                }
            }

            SearchEvent.OnFilterClick -> TODO()
            SearchEvent.OnSearchIconClick -> {
                _state.update {
                    it.copy(
                        isFirstSearchingScreen = true
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): SearchAction {
        return SearchAction.OnError(message)
    }
}