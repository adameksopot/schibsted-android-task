package com.schibsted.nde.data

import com.schibsted.nde.api.BackendApi
import com.schibsted.nde.database.MealEntity
import com.schibsted.nde.database.MealEntityDao
import com.schibsted.nde.model.MealResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealsRepository @Inject constructor(
    private val backendApi: BackendApi,
    private val mealEntityDao: MealEntityDao,
) {
    suspend fun fetchMeals() {
        val meals = backendApi.getMeals().meals
        saveMeals(meals)
    }

    fun getMeals(): Flow<List<MealResponse>> =
        mealEntityDao.getAll().map { it.map { mapMealEntityToResponse(it) } }

    private suspend fun saveMeals(meals: List<MealResponse>) {
        val mappedMeals = meals.map { mapMealResponseToEntity(it) }
        mealEntityDao.insertMeals(mappedMeals)
    }

    suspend fun getMealById(id: Int): MealResponse = mapMealEntityToResponse(mealEntityDao.getMealById(id))

    private fun mapMealResponseToEntity(meal: MealResponse): MealEntity {
        return MealEntity(
            id = meal.idMeal,
            strMeal = meal.strMeal,
            strCategory = meal.strCategory,
            strMealThumb = meal.strMealThumb,
            strYoutube = meal.strYoutube,
            strInstructions = meal.strInstructions,
        )
    }

    private fun mapMealEntityToResponse(meal: MealEntity): MealResponse {
        return MealResponse(
            idMeal = meal.id,
            strMeal = meal.strMeal,
            strCategory = meal.strCategory,
            strMealThumb = meal.strMealThumb,
            strYoutube = meal.strYoutube,
            strInstructions = meal.strInstructions,
        )
    }
}