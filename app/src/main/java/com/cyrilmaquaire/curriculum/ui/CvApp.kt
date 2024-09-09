/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package com.cyrilmaquaire.curriculum.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.ui.screens.CvListScreen
import com.cyrilmaquaire.curriculum.model.viewmodels.GetCvListViewModel
import com.cyrilmaquaire.curriculum.ui.screens.LoginScreen
import com.cyrilmaquaire.curriculum.ui.screens.ProfileScreen
import com.cyrilmaquaire.curriculum.model.viewmodels.GetCvViewModel


enum class Screen {
    PROFILE, CVLIST, LOGIN,
}

sealed class NavigationItem(val route: String) {
    data object Profile : NavigationItem(Screen.PROFILE.name)
    data object CvList : NavigationItem(Screen.CVLIST.name)
    data object Login : NavigationItem(Screen.LOGIN.name)
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Login.route,
) {
    val getCvViewModel: GetCvViewModel = viewModel()
    val getCvListViewModel: GetCvListViewModel = viewModel()
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(route = NavigationItem.Login.route) {
            LoginScreen(
                navController
            )
        }
        composable(route = NavigationItem.CvList.route) {
            CvListScreen(
                navController = navController,
                viewModel= getCvListViewModel
            )
        }
        composable(
            route = NavigationItem.Profile.route + "/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.LongType; defaultValue = 1
            })
        ) { backstackEntry ->
            ProfileScreen(
                userId = backstackEntry.arguments?.getLong("userId"),
                viewModel = getCvViewModel,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun CvApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { CvTopAppBar(scrollBehavior = scrollBehavior) }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxSize()
                .padding(it)
        ) {
            AppNavHost(navController = rememberNavController())
        }
    }
}

@Composable
fun CvTopAppBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.title),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        modifier = modifier,
    )
}
