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
package com.cyrilmaquaire.curriculum.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.viewmodels.GetCvListViewModel
import com.cyrilmaquaire.curriculum.ui.NavigationItem
import com.cyrilmaquaire.curriculum.ui.theme.FenstonBlue
import com.cyrilmaquaire.curriculum.user


@Composable
fun CvListScreen(
    navController: NavController, viewModel: GetCvListViewModel
) {

    val loadingState = viewModel.loadingState.collectAsState()
    val cvList = viewModel.response.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCvList(user?.id)
    }

    when (loadingState.value) {
        LoadingStates.LOADING -> {
            // Affichage d'un loader
            LoadingScreen()
        }

        LoadingStates.LOADED -> {
            // Affichage de la liste
            cvList.value?.let {
                com.cyrilmaquaire.curriculum.cvList = it.data
                ListScreen(
                    navController, it.data
                )
            }
        }

        LoadingStates.ERROR -> {
            // Affichage d'un message d'erreur
            ErrorScreen()
        }
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ListScreen(
    navController: NavController, cvList: List<CV>
) {
    LazyColumn {
        items(cvList) { cv ->
            Card(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(FenstonBlue)
                    ) {
                        AsyncImage(
                            model = "https://www.cyrilmaquaire.com/curriculum/uploads/" + cv.profileImage,
                            contentDescription = "Translated description of what the image contains",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 12.dp)
                    ) {
                        Text(
                            text = cv.nom + " " + cv.prenom,
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = fontOrbitronFamily
                        )
                        Text(text = cv.poste, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = stringResource(R.string._15ans_d_experience),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(
                        onClick = {
                            navController.navigate(NavigationItem.Profile.route + "/" + cv.id)
                        }
                    ) {
                        Text("Consulter")
                    }

                    TextButton(
                        onClick = {
                            navController.navigate(NavigationItem.Edit.route+"/"+cv.id)
                        }
                    ) {
                        Text("Modifier")
                    }
                }
            }
        }
    }
}

