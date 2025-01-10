package com.schibsted.nde.feature.mealdetails

import com.schibsted.nde.model.MealResponse

data class SingleMealViewState(
    val meal: MealResponse? = null,
)