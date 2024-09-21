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

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.Autre
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.CompetenceTechnique
import com.cyrilmaquaire.curriculum.model.Experience
import com.cyrilmaquaire.curriculum.model.Formation
import com.cyrilmaquaire.curriculum.model.Langue
import com.cyrilmaquaire.curriculum.model.viewmodels.GetCvViewModel
import com.cyrilmaquaire.curriculum.provider
import com.cyrilmaquaire.curriculum.ui.elements.LinkText
import com.cyrilmaquaire.curriculum.ui.elements.LinkTextData
import com.cyrilmaquaire.curriculum.ui.theme.CurriculumTheme
import com.cyrilmaquaire.curriculum.ui.theme.DateText
import com.cyrilmaquaire.curriculum.ui.theme.ExtendedText
import com.cyrilmaquaire.curriculum.ui.theme.FenstonBlue


val fontOrbitron = GoogleFont("Orbitron")
val fontAntonio = GoogleFont("Antonio")

val fontOrbitronFamily = FontFamily(
    Font(googleFont = fontOrbitron, fontProvider = provider),
)

val fontAntonioFamily = FontFamily(
    Font(googleFont = fontAntonio, fontProvider = provider)
)

@Composable
fun ProfileScreen(
    userId: Long?, viewModel: GetCvViewModel, modifier: Modifier = Modifier
) {
    val cv = viewModel.cv.collectAsState()
    // Variables qui changeront d'état lors du chargement des données
    val loadingState = viewModel.loadingState.collectAsState()

    LaunchedEffect(Unit) {
        userId?.let { viewModel.getCV(it) }
    }

    when (loadingState.value) {
        LoadingStates.LOADING -> {
            // Affichage d'un loader
            LoadingScreen()
        }
        LoadingStates.LOADED -> {
            // Affichage de la liste
            cv.value?.let {
                ResultScreen(it.data, modifier)
            }
        }
        LoadingStates.ERROR -> {
            // Affichage d'un message d'erreur
            ErrorScreen()
        }
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(cv: CV?, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
    ) {
        cv.let {
            Column {
                it?.let { data ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .clip(CircleShape)
                                .background(FenstonBlue)
                        ) {
                            AsyncImage(
                                model = "https://www.cyrilmaquaire.com/curriculum/uploads/" + data.profileImage,
                                contentDescription = "Translated description of what the image contains",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .height(300.dp)
                                    .width(300.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    it?.let { data ->
                        Text(
                            text = data.nom + " " + data.prenom,
                            style = MaterialTheme.typography.headlineMedium,
                            fontFamily = fontOrbitronFamily
                        )
                        Text(text = data.poste, style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = stringResource(R.string._15ans_d_experience),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                it?.let { data ->
                    ContactCard(cv = data)
                    CompetenceTechniqueCard(competencesTechniques = data.competenceTechniques)
                    LangueCard(data.langues)
                    AutresCard(autres = data.autres)
                    DescriptionCard(data)
                    ExperienceCard(experiences = data.experiences)
                    FormationCard(formations = data.formations)
                }
            }
        }

    }
}

@Composable
fun DescriptionCard(cv: CV) {
    Text(
        text = cv.description,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.headlineSmall,
        fontFamily = fontAntonioFamily
    )
}

@Composable
fun LangueCard(langues: List<Langue>) {
    Column {
        ExtendedText(text = stringResource(R.string.langues))
        for (langue in langues) {
            Row(Modifier.padding(all = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = langue.origine,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 16.dp)
                )
                LinearProgressIndicator(
                    progress = { langue.percent.toFloat() / 100 }, modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompetenceTechniqueCard(competencesTechniques: List<CompetenceTechnique>) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        ExtendedText(text = stringResource(R.string.competences_techniques))
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            for (competenceTechnique in competencesTechniques) {
                Column(horizontalAlignment = CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://www.cyrilmaquaire.com/curriculum/uploads/" + competenceTechnique.libelle + ".png",
                            contentDescription = "Translated description of what the image contains",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(60.dp)
                                .width(60.dp),
                        )
                    }
                    Text(
                        text = competenceTechnique.libelle,
                        style = MaterialTheme.typography.titleMedium
                    )
                    competenceTechnique.competence?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AutresCard(autres: List<Autre>) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        ExtendedText(text = stringResource(R.string.activites))
        FlowRow(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            maxItemsInEachRow = 2
        ) {
            for (autre in autres) {
                Column(horizontalAlignment = CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(color = Color.White), contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://www.cyrilmaquaire.com/curriculum/uploads/" + autre.libelle + ".png",
                            contentDescription = "Image de " + autre.libelle,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(80.dp)
                                .width(80.dp),
                        )
                    }
                    Text(
                        text = autre.libelle,
                        modifier = Modifier.padding(bottom = 12.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ContactCard(cv: CV) {
    val context = LocalContext.current
    Row(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Column {
            ExtendedText(text = stringResource(R.string.contact))

            Row {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Phone",
                    Modifier.padding(horizontal = 16.dp)
                )
                Text(text = cv.telephone)
            }

            Row {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email",
                    Modifier.padding(horizontal = 16.dp)
                )
                Text(text = cv.mail)
            }
            Row {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Website",
                    Modifier.padding(horizontal = 16.dp)
                )
                LinkText(
                    linkTextData = listOf(
                        LinkTextData(
                            text = cv.website,
                            tag = "icon_1_author",
                            annotation = "http://" + cv.website,
                            onClick = {
                                val i = Intent(Intent.ACTION_VIEW)
                                i.setData(Uri.parse(it.item))
                                context.startActivity(i)
                            },
                        )
                    )
                )
            }
            Row {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Mail",
                    Modifier.padding(horizontal = 16.dp)
                )
                Box {
                    Column {
                        Text(text = cv.adresse1)
                        if (cv.adresse2.isNotEmpty()) Text(text = cv.adresse2)
                        Text(text = cv.zipCode + " " + cv.city)
                    }
                }
            }
        }
    }
}

@Composable
fun ExperienceCard(experiences: List<Experience>) {
    Column {
        ExtendedText(text = stringResource(R.string.experience))
        for (experience in experiences) {
            DateText(text = experience.dateDebut + " - " + experience.dateFin)
            Text(text = experience.entreprise, style = MaterialTheme.typography.titleLarge)
            Text(
                text = experience.poste,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Box(modifier = Modifier.padding(all = 8.dp)) {
                Column {
                    for (projet in experience.projets) {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Email",
                                Modifier.padding(end = 16.dp)
                            )
                            Text(
                                text = "Projet " + projet.nom,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        Text(
                            text = projet.description, modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun FormationCard(formations: List<Formation>) {
    Column {
        ExtendedText(text = stringResource(R.string.education))
        for (formation in formations) {
            DateText(text = formation.dateDebut)
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = formation.etablissement,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = formation.description,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    CurriculumTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    CurriculumTheme {
        ErrorScreen()
    }
}
