package com.amolina.presentation.ui.navigation

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.amolina.presentation.ui.components.CitiesListScreen
import com.amolina.presentation.ui.components.CityDetailScreen
import com.amolina.presentation.ui.components.maps.LandscapeCitiesMapScreen
import com.amolina.presentation.ui.components.maps.MapScreen
import com.amolina.presentation.ui.viewmodel.CitiesViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CitiesNavGraph(navController: NavHostController) {
    val viewModel: CitiesViewModel = koinViewModel()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    NavHost(
        navController = navController,
        startDestination = Screen.CitiesList.route
    ) {
        composable(Screen.CitiesList.route) {
            if (isLandscape) {
                LandscapeCitiesMapScreen(viewModel = viewModel, onInfoClicked = { cityId ->
                    navController.navigate(Screen.CityDetail.createRoute(cityId))
                })
            } else {
                CitiesListScreen(
                    viewModel = viewModel,
                    onCityClicked = { cityId ->
                        navController.navigate(Screen.Map.createRoute(cityId))
                    },
                    onInfoClicked = { cityId ->
                        navController.navigate(Screen.CityDetail.createRoute(cityId))
                    }
                )
            }
        }

        composable(
            Screen.Map.route,
            arguments = listOf(navArgument("cityId") { type = NavType.StringType })
        ) { backStackEntry ->
            if (isLandscape) {
                LandscapeCitiesMapScreen(viewModel = viewModel, onInfoClicked = { cityId ->
                    navController.navigate(Screen.CityDetail.createRoute(cityId))
                })
            } else {
                val cityIdString = backStackEntry.arguments?.getString("cityId")
                val cityId = cityIdString?.toIntOrNull()
                cityId?.let {
                    viewModel.selectCity(it)
                    val selectedCity = viewModel.selectedCity.collectAsState().value
                    selectedCity?.let {
                        MapScreen(
                            city = it,
                            onBackPressed = { navController.navigateUp() }
                        )
                    }
                }
            }
        }

        composable(
            Screen.CityDetail.route,
            arguments = listOf(navArgument("cityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityIdString = backStackEntry.arguments?.getString("cityId")
            val cityId = cityIdString?.toIntOrNull()
            cityId?.let {
                viewModel.selectCity(it)
                val selectedCity = viewModel.selectedCity.collectAsState().value
                selectedCity?.let {
                    CityDetailScreen(
                        city = it,
                        onToggleFavourite = { cityId -> viewModel.toggleFavourite(cityId) },
                        onBackPressed = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object CitiesList : Screen("cities_list")
    object Map : Screen("map/{cityId}") {
        fun createRoute(cityId: Int) = "map/$cityId"
    }

    object CityDetail : Screen("city_detail/{cityId}") {
        fun createRoute(cityId: Int) = "city_detail/$cityId"
    }
}