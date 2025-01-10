package com.schibsted.nde

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.schibsted.nde.feature.mealdetails.MealDetailsScreen
import com.schibsted.nde.feature.meals.MealsScreen
import com.schibsted.nde.feature.meals.MealsViewModel
import com.schibsted.nde.feature.routes.MealDetails
import com.schibsted.nde.feature.routes.Meals
import com.schibsted.nde.ui.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph(navController)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Meals) {
        composable<Meals> {
            val mealsViewModel = hiltViewModel<MealsViewModel>()
            MealsScreen(mealsViewModel) { navController.navigate(MealDetails(it)) }
        }
            composable<MealDetails> { entry ->
            val args = entry.toRoute<MealDetails>()
            MealDetailsScreen(args.id)
        }
    }
}