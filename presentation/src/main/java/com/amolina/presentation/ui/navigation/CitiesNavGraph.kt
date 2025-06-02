package com.amolina.presentation.ui.navigation

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.amolina.presentation.ui.components.CitiesListScreen
import com.amolina.presentation.ui.components.LandscapeCitiesMapScreen
import com.amolina.presentation.ui.components.MapScreen
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
                LandscapeCitiesMapScreen(
                    viewModel = viewModel
                )
            } else {
                CitiesListScreen(
                    viewModel = viewModel,
                    usePaging = false,
                    onCityClicked = { city ->
                        navController.navigate(Screen.Map.createRoute(city))
                    }
                )
            }
        }

        composable(
            Screen.Map.route,
            arguments = listOf(navArgument("cityId") { type = NavType.StringType })
        ) { backStackEntry ->
            if (isLandscape) {
                LandscapeCitiesMapScreen(viewModel = viewModel)
            } else {
                val cityIdString = backStackEntry.arguments?.getString("cityId")
                val cityId = cityIdString?.toIntOrNull()
                cityId?.let {
                    val city = viewModel.getCityById(it)
                    city?.let {
                        MapScreen(
                            city = it,
                            onBackPressed = { navController.navigateUp() }
                        )
                    }
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
}
