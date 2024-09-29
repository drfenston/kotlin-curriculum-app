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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.cvList
import com.cyrilmaquaire.curriculum.model.StoreData
import com.cyrilmaquaire.curriculum.model.viewmodels.GetCvViewModel
import com.cyrilmaquaire.curriculum.model.viewmodels.MainViewModel
import com.cyrilmaquaire.curriculum.model.viewmodels.UpdateCvViewModel
import com.cyrilmaquaire.curriculum.model.viewmodels.factories.MainViewModelFactory
import com.cyrilmaquaire.curriculum.ui.screens.CvListScreen
import com.cyrilmaquaire.curriculum.ui.screens.EditCvScreen
import com.cyrilmaquaire.curriculum.ui.screens.LoadingScreen
import com.cyrilmaquaire.curriculum.ui.screens.LoginScreen
import com.cyrilmaquaire.curriculum.ui.screens.ProfileScreen


enum class Screen {
    LOADING, PROFILE, CVLIST, LOGIN, EDIT
}

sealed class NavigationItem(val route: String) {
    data object Loading : NavigationItem(Screen.LOADING.name)
    data object Profile : NavigationItem(Screen.PROFILE.name)
    data object CvList : NavigationItem(Screen.CVLIST.name)
    data object Login : NavigationItem(Screen.LOGIN.name)
    data object Edit : NavigationItem(Screen.EDIT.name)
}

var viewModel: MainViewModel? = null

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Loading.route,
) {
    val getCvViewModel: GetCvViewModel = viewModel()
    val updateCvViewModel: UpdateCvViewModel = viewModel()

    LaunchedEffect(Unit) {
        // Démarrage de l'application, vérification du login
        attemptLogin(navController)
    }

    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(route = NavigationItem.Login.route) {
            LoginScreen(
                navController, viewModel
            )
        }
        composable(route = NavigationItem.Loading.route) {
            LoadingScreen()
        }
        composable(route = NavigationItem.CvList.route) {
            CvListScreen(
                navController = navController
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
        composable(
            route = NavigationItem.Edit.route + "/{cvId}",
            arguments = listOf(navArgument("cvId") {
                type = NavType.LongType; defaultValue = 1
            })
        ) { backstackEntry ->
            EditCvScreen(
                cvId = backstackEntry.arguments?.getLong("cvId"),
                viewModel = updateCvViewModel
            )
        }
    }
}

@Composable
fun CvApp(userPreferences: StoreData) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    viewModel = viewModel(factory = MainViewModelFactory(userPreferences))

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CvTopAppBar(scrollBehavior = scrollBehavior)
        }) {
        Surface(
            modifier = Modifier
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

fun attemptLogin(navController: NavController) {
    viewModel?.checkLoginAndLoginWebService(
        onSuccess = {
            // Si le login est correct, charger les données
            viewModel?.loadData(
                onSuccess = { dataList ->
                    cvList = dataList
                    // Naviguer vers la liste des données avec succès
                    navController.navigate(NavigationItem.CvList.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onError = {
                    // Afficher l'écran de login en cas d'erreur lors du chargement
                    navController.navigate(NavigationItem.Login.route)
                }
            )
        },
        onError = {
            // Naviguer vers l'écran de login en cas d'erreur lors du login
            navController.navigate(NavigationItem.Login.route)
        }
    )
}