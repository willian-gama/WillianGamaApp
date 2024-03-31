package com.android.dev.engineer.kotlin.compose.feature.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.dev.engineer.kotlin.compose.data.domain.local.MainNavGraph
import com.android.dev.engineer.kotlin.compose.feature.intro.IntroScreen
import com.android.dev.engineer.kotlin.compose.feature.movie.MovieScreen
import com.android.dev.engineer.kotlin.compose.feature.movie.MovieViewModel.Companion.ID_ARG
import com.android.dev.engineer.kotlin.compose.feature.sign_in.SignInScreen
import com.android.dev.engineer.kotlin.compose.feature.upcoming_movies.UpcomingMoviesScreen

@Composable
fun MainNavHostComposable(
    startDestination: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = MainNavGraph.Intro.route) {
            IntroScreen(
                onSkipClicked = { mainNavGraph ->
                    navController.navigate(route = mainNavGraph.route) {
                        popUpTo(route = MainNavGraph.Intro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(MainNavGraph.SignIn.route) {
            SignInScreen(
                onLoggedIn = { mainNavGraph ->
                    navController.navigate(route = mainNavGraph.route) {
                        popUpTo(route = MainNavGraph.SignIn.route) { inclusive = true }
                    }
                }
            )
        }
        composable(MainNavGraph.UpcomingMovies.route) {
            UpcomingMoviesScreen(
                onClickMovie = { movieItem ->
                    navController.navigate(route = MainNavGraph.Movie.route + "/${movieItem.id}")
                }
            )
        }
        composable(
            route = MainNavGraph.Movie.route + "/{$ID_ARG}",
            arguments = listOf(
                navArgument(name = ID_ARG, builder = { type = NavType.IntType })
            )
        ) {
            MovieScreen()
        }
    }
}