package com.kurostream.tv.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kurostream.tv.ui.home.HomeScreen
import com.kurostream.tv.ui.discover.DiscoverScreen
import com.kurostream.tv.ui.mylist.MyListScreen
import com.kurostream.tv.ui.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("discover") {
            DiscoverScreen(navController = navController)
        }
        composable("my_list") {
            MyListScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}
