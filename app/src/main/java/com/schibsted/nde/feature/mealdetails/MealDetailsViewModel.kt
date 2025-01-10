package com.schibsted.nde.feature.mealdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schibsted.nde.data.MealsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel(assistedFactory = MealDetailsViewModel.Factory::class)
class MealDetailsViewModel @AssistedInject constructor(
    @Assisted val id: Int,
    private val mealsRepository: MealsRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Int): MealDetailsViewModel
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        val newState = _state.value.copy(isError = true)
        _state.value = newState
    }

    private val _state = MutableStateFlow(SingleMealViewState())

    val state: StateFlow<SingleMealViewState>
        get() = _state

    init {
        loadMeal(id)
    }

    private fun loadMeal(id: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val meal = mealsRepository.getMealById(id)
            _state.value = SingleMealViewState(meal)
        }
    }
}