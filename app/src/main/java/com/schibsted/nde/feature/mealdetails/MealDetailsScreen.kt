package com.schibsted.nde.feature.mealdetails

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schibsted.nde.feature.common.MealImage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalFoundationApi
@Composable
fun MealDetailsScreen(
    id: Int,
    viewModel: MealDetailsViewModel =
        hiltViewModel<MealDetailsViewModel, MealDetailsViewModel.Factory>(
            creationCallback = { factory -> factory.create(id) })
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Food App Details") }) },
        content = { MealDetailsContent(viewModel) }
    )
}

@ExperimentalFoundationApi
@Composable
fun MealDetailsContent(viewModel: MealDetailsViewModel) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isError) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Failed to load meal details. Please try again.",
                            modifier = Modifier.padding(16.dp)
                        )

                    }
                }
        } else {
            state.meal?.let { meal ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    MealImage(
                        thumb = meal.strMealThumb,
                        modifier = Modifier
                            .height(240.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = meal.strMeal,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Cooking Instructions:",
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        val instructions = meal.strInstructions?.split(". ")
                            ?: listOf("No instructions available.")
                        items(instructions) { step ->
                            Text(
                                text = step,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading meal details...")
                }
            }
        }
    }
}

