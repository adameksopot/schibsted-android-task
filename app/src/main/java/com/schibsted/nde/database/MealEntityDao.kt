package com.schibsted.nde.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealEntityDao {
    @Query("SELECT * FROM meal ORDER BY id")
    fun getAll(): Flow<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Query("SELECT * FROM meal WHERE id = :mealId")
    suspend fun getMealById(mealId: Int): MealEntity
}
