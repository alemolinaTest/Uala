package com.amolina.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amolina.presentation.ui.components.CitiesListScreen
import com.amolina.presentation.ui.components.CityDetailScreen
import com.amolina.presentation.ui.viewmodel.CitiesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CitiesNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.CitiesList.route,
        modifier = modifier //paddding all childs below top bar
    ) {
        composable(route = Screen.CitiesList.route) {
            val viewModel: CitiesViewModel = koinViewModel()
            CitiesListScreen(
                viewModel = viewModel,
                onCityClicked = { cityId ->
                    navController.navigate(Screen.CityDetail.createRoute(cityId))
                }
            )
        }
        composable(route = Screen.CityDetail.route) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getString("cityId")?.toIntOrNull()
            cityId?.let {
                CityDetailScreen(cityId = it)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object CitiesList : Screen("cities_list")
    object CityDetail : Screen("city_detail/{cityId}") {
        fun createRoute(cityId: Int) = "city_detail/$cityId"
    }
}
