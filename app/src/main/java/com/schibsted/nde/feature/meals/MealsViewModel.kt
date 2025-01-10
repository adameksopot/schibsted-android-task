package com.schibsted.nde.feature.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.data.MealsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val mealsRepository: MealsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MealsViewState(isLoading = true))

    val state: StateFlow<MealsViewState>
        get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        val newState = _state.value.copy(isLoading = false)
        _state.value = newState
    }

    init {
        loadMeals()
    }

    fun loadMeals() {
        viewModelScope.launch(exceptionHandler) {
            _state.emit(_state.value.copy(isLoading = true))

            mealsRepository.getMeals()
                .collect { mealsFromDb ->
                    if (mealsFromDb.isNotEmpty()) {
                        _state.emit(
                            _state.value.copy(
                                meals = mealsFromDb,
                                filteredMeals = mealsFromDb,
                                isLoading = false
                            )
                        )
                    } else {
                        fetchMealsFromNetworkAndSave()
                    }
                }
        }
    }

    private suspend fun fetchMealsFromNetworkAndSave() = mealsRepository.fetchMeals()

    fun submitQuery(query: String?) {
        viewModelScope.launch(Dispatchers.Default) {
            val filteredMeals = if (query?.isNotBlank() == true) {
                _state.value.meals.filter {
                    it.strMeal.lowercase().contains(query.lowercase())
                }
            } else {
                _state.value.meals
            }
            _state.emit(_state.value.copy(query = query, filteredMeals = filteredMeals))
        }
    }
}