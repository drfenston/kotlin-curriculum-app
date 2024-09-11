package com.cyrilmaquaire.curriculum.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.Cases
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.cvList
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.Projet
import com.cyrilmaquaire.curriculum.model.viewmodels.UpdateCvViewModel
import com.cyrilmaquaire.curriculum.ui.theme.ExtendedText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TabItem(
    val title: String, val unselectedIcon: ImageVector, val selectedIcon: ImageVector
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditCvScreen(
    cvId: Long?, viewModel: UpdateCvViewModel
) {
    val cv = cvList.find { it.id == cvId }
    val tabItems = listOf(
        TabItem(
            title = "Contact",
            unselectedIcon = Icons.Outlined.Contacts,
            selectedIcon = Icons.Filled.Contacts
        ), TabItem(
            title = "Competences Techniques",
            unselectedIcon = Icons.Outlined.Computer,
            selectedIcon = Icons.Filled.Computer
        ), TabItem(
            title = "Langues",
            unselectedIcon = Icons.Outlined.Language,
            selectedIcon = Icons.Filled.Language
        ), TabItem(
            title = "Activités",
            unselectedIcon = Icons.Outlined.AssignmentTurnedIn,
            selectedIcon = Icons.Filled.AssignmentTurnedIn
        ), TabItem(
            title = "Expériences",
            unselectedIcon = Icons.Outlined.Cases,
            selectedIcon = Icons.Filled.Cases
        ), TabItem(
            title = "Formation",
            unselectedIcon = Icons.Outlined.School,
            selectedIcon = Icons.Filled.School
        )
    )
    var selectedTabIdex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIdex = pagerState.currentPage

    }
    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = (selectedTabIdex)) {
            tabItems.forEachIndexed { index, item ->
                Tab(selected = index == selectedTabIdex, onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                    selectedTabIdex = index
                }, icon = {
                    Icon(
                        imageVector = if (index == selectedTabIdex) {
                            item.selectedIcon
                        } else item.unselectedIcon, contentDescription = item.title
                    )
                })
            }

        }

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) { index ->
            when (index) {
                0 -> cv?.let { EditProfileScreen(it, viewModel = viewModel) }
                1 -> cv?.let { ComptTechScreen(it) }
                2 -> cv?.let { LanguesScreen(it) }
                3 -> cv?.let { ActiviteScreen(it) }
                4 -> cv?.let { ExperienceScreen(it) }
                5 -> cv?.let { FormationScreen(it) }
            }

        }
    }
}


@Composable
fun EditProfileScreen(
    cv: CV, viewModel: UpdateCvViewModel
) {
    val focusManager = LocalFocusManager.current

    var poste by remember { mutableStateOf(cv.poste) }
    var description by remember { mutableStateOf(cv.description) }
    var nom by remember { mutableStateOf(cv.nom) }
    var prenom by remember { mutableStateOf(cv.prenom) }
    var adresse1 by remember { mutableStateOf(cv.adresse1) }
    var adresse2 by remember { mutableStateOf(cv.adresse2) }
    var zipCode by remember { mutableStateOf(cv.zipCode) }
    var ville by remember { mutableStateOf(cv.city) }
    var telephone by remember { mutableStateOf(cv.telephone) }
    var email by remember { mutableStateOf(cv.mail) }
    var siteWeb by remember { mutableStateOf(cv.website) }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ExtendedText(text = stringResource(R.string.profile))
        OutlinedTextField(
            value = poste,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.poste)) },
            onValueChange = {
                poste = it
                cv.poste = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            maxLines = 4,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.description)) },
            onValueChange = {
                description = it
                cv.description = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = nom,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.nom)) },
            onValueChange = {
                nom = it
                cv.nom = it
            })
        OutlinedTextField(value = prenom,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.prenom)) },
            onValueChange = {
                prenom = it
                cv.prenom = it
            })
        OutlinedTextField(
            value = adresse1,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.adresse_1)) },
            onValueChange = {
                adresse1 = it
                cv.adresse1 = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = adresse2,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.adresse_2)) },
            onValueChange = {
                adresse2 = it
                cv.adresse2 = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = zipCode,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.code_postal)) },
            onValueChange = {
                zipCode = it
                cv.zipCode = it
            })
        OutlinedTextField(value = ville,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.ville)) },
            onValueChange = {
                ville = it
                cv.city = it
            })
        OutlinedTextField(
            value = telephone,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.telephone)) },
            onValueChange = {
                telephone = it
                cv.telephone = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email,
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.email)) },
            onValueChange = {
                email = it
                cv.mail = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = siteWeb,
            maxLines = 1,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text(stringResource(R.string.site_web)) },
            onValueChange = {
                siteWeb = it
                cv.website = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        val loadingState = viewModel.loadingState.collectAsState()
        ExtendedFloatingActionButton(
            onClick = {
                viewModel.updateCv(cvId = cv.id, cv = cv, onLoginSuccess = { response ->
                    Log.d("coucou", "UpdateCV:  " + response.message)
                }, onLoginError = { error -> Log.d("coucou", "Error: $error") })

                if (loadingState.value == LoadingStates.LOADING) {
                    Log.d("coucou", "Update CV Loading")
                } else if (loadingState.value == LoadingStates.ERROR) {
                    Log.d("coucou", "Update CV Error")
                }
            },
            icon = { Icon(Icons.Filled.Check, "Enregistrer les changements") },
            text = { Text(text = "Enregistrer les changements") },
        )
    }

}

@Composable
fun ComptTechScreen(cv: CV) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ExtendedText(text = stringResource(R.string.competences_techniques))
        for (compTech in cv.competenceTechniques) {
            var libelle by remember { mutableStateOf(compTech.libelle) }
            var competence by remember { mutableStateOf(compTech.competence) }
            var sliderPosition by remember { mutableFloatStateOf(compTech.percent.toFloat()) }
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(all = 16.dp)) {
                    OutlinedTextField(value = libelle,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Nom de la compétence") },
                        onValueChange = {
                            libelle = it
                            compTech.libelle = it
                        })
                    OutlinedTextField(
                        value = competence,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Description de la compétence") },
                        onValueChange = {
                            competence = it
                            compTech.competence = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Slider(
                        value = sliderPosition,
                        steps = 5,
                        valueRange = 0f..100f,
                        onValueChange = {
                            sliderPosition = it
                            compTech.percent = it.toLong()
                        })
                }
            }
        }
    }
}

@Composable
fun LanguesScreen(cv: CV) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ExtendedText(text = stringResource(R.string.langues))
        for (langue in cv.langues) {
            var origine by remember { mutableStateOf(langue.origine) }
            var niveau by remember { mutableStateOf(langue.niveau) }
            var sliderPosition by remember { mutableFloatStateOf(langue.percent.toFloat()) }
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(all = 16.dp)) {
                    OutlinedTextField(value = origine,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Origine") },
                        onValueChange = {
                            origine = it
                            langue.origine = it
                        })
                    OutlinedTextField(
                        value = niveau,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Description") },
                        onValueChange = {
                            niveau = it
                            langue.niveau = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Slider(
                        value = sliderPosition,
                        steps = 5,
                        valueRange = 0f..100f,
                        onValueChange = {
                            sliderPosition = it
                            langue.percent = it.toLong()
                        })
                }
            }
        }
    }
}

@Composable
fun ActiviteScreen(cv: CV) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
    ) {
        ExtendedText(text = stringResource(R.string.activites))
        for (activite in cv.autres) {
            var libelle by remember { mutableStateOf(activite.libelle) }
            var description by remember { mutableStateOf(activite.description) }
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(all = 16.dp)) {
                    OutlinedTextField(
                        value = libelle,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Activité") },
                        onValueChange = {
                            libelle = it
                            activite.libelle = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Description") },
                        onValueChange = {
                            description = it
                            activite.description = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ExperienceScreen(cv: CV) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ExtendedText(text = stringResource(R.string.experience))
        for (experience in cv.experiences) {
            var dateDebut by remember { mutableStateOf(experience.dateDebut) }
            var dateFin by remember { mutableStateOf(experience.dateFin) }
            var entreprise by remember { mutableStateOf(experience.entreprise) }
            var poste by remember { mutableStateOf(experience.poste) }
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(all = 16.dp)) {
                    CustomDatePicker(titre = "Date début", date = dateDebut) { date ->
                        date?.let {
                            dateDebut = it
                            experience.dateDebut = it
                        }
                    }
                    CustomDatePicker(titre = "Date fin", date = dateFin) { date ->
                        date?.let {
                            dateFin = it
                            experience.dateFin = it
                        }
                    }
                    OutlinedTextField(
                        value = entreprise,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Entreprise") },
                        onValueChange = {
                            entreprise = it
                            experience.entreprise = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = poste,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Poste") },
                        onValueChange = {
                            poste = it
                            experience.poste = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                ProjetScreen(projets = experience.projets)
            }
        }
    }
}

@Composable
fun ProjetScreen(projets: List<Projet>) {
    val focusManager = LocalFocusManager.current

    Column(Modifier.padding(horizontal = 16.dp)) {
        ExtendedText(text = stringResource(R.string.projets))
        for (projet in projets) {
            var nomProjet by remember { mutableStateOf(projet.nom) }
            var descriptionProjet by remember { mutableStateOf(projet.description) }

            Card(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(horizontal = 8.dp)
            ) {
                OutlinedTextField(
                    value = nomProjet,
                    maxLines = 1,
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    label = { Text("Nom du projet") },
                    onValueChange = {
                        nomProjet = it
                        projet.nom = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descriptionProjet,
                    maxLines = 4,
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    label = { Text("Description du projet") },
                    onValueChange = {
                        descriptionProjet = it
                        projet.description = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FormationScreen(cv: CV) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ExtendedText(text = stringResource(R.string.education))
        for (formation in cv.formations) {
            var dateDebut by remember { mutableStateOf(formation.dateDebut) }
            var dateFin by remember { mutableStateOf(formation.dateFin) }
            var etablissement by remember { mutableStateOf(formation.etablissement) }
            var description by remember { mutableStateOf(formation.description) }

            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(all = 16.dp)) {
                    CustomDatePicker(titre = "Date début", date = dateDebut) { date ->
                        date?.let {
                            dateDebut = it
                            formation.dateDebut = it
                        }
                    }
                    CustomDatePicker(titre = "Date fin", date = dateFin) { date ->
                        date?.let {
                            dateFin = it
                            formation.dateFin = it
                        }
                    }

                    OutlinedTextField(
                        value = etablissement,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Etablissement") },
                        onValueChange = {
                            etablissement = it
                            formation.etablissement = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text("Description") },
                        onValueChange = {
                            description = it
                            formation.description = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDatePicker(titre: String, date: String, onDateSelected: (String?) -> Unit) {
    var showModalDate by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                titre,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            if (selectedDate != null) {
                val sDate = Date(selectedDate!!)
                val formattedDate = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(sDate)
                Text(formattedDate, textAlign = TextAlign.Start)
            } else {
                if (date.isNotEmpty()) {
                    Text(text = date, textAlign = TextAlign.Start)
                } else {
                    Text("Sélectionner une date", textAlign = TextAlign.Start)
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Button(onClick = { showModalDate = true }) {
                Text("Modifier")
            }
        }
    }

    if (showModalDate) {
        DatePickerModalInput(onDateSelected = {
            selectedDate = it
            showModalDate = false
            onDateSelected(SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(it))
        }, onDismiss = { showModalDate = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDateSelected(datePickerState.selectedDateMillis)
            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}
