package com.schibsted.nde.feature.routes

import kotlinx.serialization.Serializable

@Serializable
data object Meals

@Serializable
data class MealDetails(val id: Int)
