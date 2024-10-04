package com.cyrilmaquaire.curriculum.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.cvList
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.Projet
import com.cyrilmaquaire.curriculum.model.viewmodels.UpdateCvViewModel
import com.cyrilmaquaire.curriculum.network.CvApi
import com.cyrilmaquaire.curriculum.ui.theme.ExtendedText
import com.cyrilmaquaire.curriculum.ui.theme.FenstonBlue
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TabItem(
    val title: String, val unselectedIcon: ImageVector, val selectedIcon: ImageVector
)

var hasSomethingChanged = mutableStateOf(false)

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

    Scaffold(topBar = {/**/ }, bottomBar = {/**/ }, floatingActionButton = {
        if (hasSomethingChanged.value) {
            cv?.let {
                val loadingState = viewModel.loadingState.collectAsState()
                LargeFloatingActionButton(
                    modifier = Modifier.size(64.dp),
                    onClick = {
                        viewModel.updateCv(cvId = it.id, cv = it, onUpdateSuccess = {
                            hasSomethingChanged.value = false
                        }, onUpdateError = { error -> Log.d("coucou", "Error: $error") })

                        if (loadingState.value == LoadingStates.LOADING) {
                            Log.d("coucou", "Update CV Loading")
                        } else if (loadingState.value == LoadingStates.ERROR) {
                            Log.d("coucou", "Update CV Error")
                        }
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(
                        defaultElevation = 24.dp
                    )
                ) {
                    Icon(Icons.Filled.Check, "Enregistrer les changements")
                }
            }
        }
    }, snackbarHost = {/**/ }, content = { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
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
                state = pagerState,
                beyondBoundsPageCount = 1,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) { index ->
                when (index) {
                    0 -> cv?.let { EditProfileScreen(it, viewModel) }
                    1 -> cv?.let { ComptTechScreen(it, viewModel) }
                    2 -> cv?.let { LanguesScreen(it, viewModel) }
                    3 -> cv?.let { ActiviteScreen(it, viewModel) }
                    4 -> cv?.let { ExperienceScreen(it, viewModel) }
                    5 -> cv?.let { FormationScreen(it, viewModel) }
                }
            }
        }
    })
}


@Composable
fun EditProfileScreen(
    cv: CV,
    viewModel: UpdateCvViewModel
) {
    val focusManager = LocalFocusManager.current

    var poste by remember { mutableStateOf(cv.poste) }
    var profileImage: String by remember { mutableStateOf(cv.profileImage ?: "") }
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ExtendedText(text = stringResource(R.string.profile))
        Box(
            modifier = Modifier
                .fillMaxWidth() // Prendre toute la largeur disponible
                .wrapContentWidth()
        ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(FenstonBlue)
                .align(Alignment.Center)
        ) {
            AsyncImage(
                model = "https://www.cyrilmaquaire.com/curriculum/uploads/$profileImage",
                contentDescription = "Profile picture of the resume.",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
            )
        }
    }

    CapturePhotoScreen(apiService = CvApi.retrofitService) { data ->
        data.filename?.let {
            profileImage = it
            cv.profileImage = it
            viewModel.updateCv(cvId = cv.id, cv = cv, onUpdateSuccess = {
                hasSomethingChanged.value = false
            }, onUpdateError = { error -> Log.d("coucou", "Error: $error") })
        }
    }

    OutlinedTextField(
        value = poste,
        maxLines = 1,
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text(stringResource(R.string.poste)) },
        onValueChange = {
            poste = it
            cv.poste = it
            hasSomethingChanged.value = true
        },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = description ?: "",
        maxLines = 4,
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text(stringResource(R.string.description)) },
        onValueChange = {
            description = it
            cv.description = it
            hasSomethingChanged.value = true
        },
        modifier = Modifier.fillMaxWidth()
    )
    Row(modifier = Modifier.padding(top = 24.dp)) {
        OutlinedTextField(
            value = nom ?: "",
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.weight(.5f),
            label = { Text(stringResource(R.string.nom)) },
            onValueChange = {
                nom = it
                cv.nom = it
                hasSomethingChanged.value = true
            })
        OutlinedTextField(
            value = prenom ?: "",
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.weight(.5f),
            label = { Text(stringResource(R.string.prenom)) },
            onValueChange = {
                prenom = it
                cv.prenom = it
                hasSomethingChanged.value = true
            })
    }

    OutlinedTextField(
        value = adresse1 ?: "",
        maxLines = 1,
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text(stringResource(R.string.adresse_1)) },
        onValueChange = {
            adresse1 = it
            cv.adresse1 = it
            hasSomethingChanged.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
    )
    OutlinedTextField(
        value = adresse2 ?: "",
        maxLines = 1,
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text(stringResource(R.string.adresse_2)) },
        onValueChange = {
            adresse2 = it
            cv.adresse2 = it
            hasSomethingChanged.value = true
        },
        modifier = Modifier.fillMaxWidth()
    )
    Row {
        OutlinedTextField(value = zipCode ?: "",
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.code_postal)) },
            modifier = Modifier.weight(.5f),
            onValueChange = {
                zipCode = it
                cv.zipCode = it
                hasSomethingChanged.value = true
            })
        OutlinedTextField(value = ville ?: "",
            maxLines = 1,
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.weight(1f),
            label = { Text(stringResource(R.string.ville)) },
            onValueChange = {
                ville = it
                cv.city = it
                hasSomethingChanged.value = true
            })
    }

    OutlinedTextField(
        value = telephone ?: "",
        maxLines = 1,
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text(stringResource(R.string.telephone)) },
        onValueChange = {
            telephone = it
            cv.telephone = it
            hasSomethingChanged.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
    )
    OutlinedTextField(
        value = email ?: "",
        maxLines = 1,
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text(stringResource(R.string.email)) },
        onValueChange = {
            email = it
            cv.mail = it
            hasSomethingChanged.value = true
        },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = siteWeb ?: "",
        maxLines = 1,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        label = { Text(stringResource(R.string.site_web)) },
        onValueChange = {
            siteWeb = it
            cv.website = it
            hasSomethingChanged.value = true
        },
        modifier = Modifier.fillMaxWidth()
    )
}
}

@Composable
fun ComptTechScreen(cv: CV, viewModel: UpdateCvViewModel) {
    val focusManager = LocalFocusManager.current
    val competenceTechniques = remember { cv.competenceTechniques.toMutableStateList() }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExtendedText(text = stringResource(R.string.competences_techniques))
            Button(onClick = {
                viewModel.createCompetenceTechnique(cv.id, { response ->
                    response.data?.let {
                        cv.competenceTechniques.add(it)
                        competenceTechniques.add(it)
                    }
                }, { error ->
                    Log.d("Coucou", "Error $error")
                })
            }) {
                Icon(Icons.Filled.Add, "Ajouter une compétence technique")
            }
        }
        LazyColumn {
            items(items = competenceTechniques, key = { it.id }) { compTech ->
                var libelle by remember { mutableStateOf(compTech.libelle) }
                var competence by remember { mutableStateOf(compTech.competence) }
                var sliderPosition by remember {
                    mutableFloatStateOf(
                        compTech.percent?.toFloat() ?: 0f
                    )
                }
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        OutlinedTextField(
                            value = libelle ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        competence?.let {
                            OutlinedTextField(
                                value = it,
                                maxLines = 1,
                                keyboardActions = KeyboardActions(onNext = {
                                    focusManager.moveFocus(
                                        FocusDirection.Down
                                    )
                                }),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                label = { Text("Description de la compétence") },
                                onValueChange = { comp ->
                                    competence = comp
                                    compTech.competence = comp
                                    hasSomethingChanged.value = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Slider(value = sliderPosition,
                            steps = 5,
                            valueRange = 0f..100f,
                            onValueChange = {
                                sliderPosition = it
                                compTech.percent = it.toLong()
                                hasSomethingChanged.value = true
                            })
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                viewModel.deleteCompetenceTechnique(compTech.id, { response ->
                                    response.data?.let { competence ->
                                        cv.competenceTechniques.removeAll { it.id == competence.id }
                                        competenceTechniques.removeAll { it.id == competence.id }
                                    }
                                }, { error ->
                                    Log.d("Coucou", "Error $error")
                                })
                            }) {
                                Icon(Icons.Filled.Delete, "Supprimer une compétence")
                                Text(text = "Supprimer cette compétence")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LanguesScreen(cv: CV, viewModel: UpdateCvViewModel) {
    val focusManager = LocalFocusManager.current
    val langues = remember { cv.langues.toMutableStateList() }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExtendedText(text = stringResource(R.string.langues))
            Button(onClick = {
                viewModel.createLangue(cv.id, { response ->
                    response.data?.let {
                        cv.langues.add(it)
                        langues.add(it)
                    }
                }, { error ->
                    Log.d("Coucou", "Error $error")
                })
            }) {
                Icon(Icons.Filled.Add, "Ajouter une langue")
            }
        }
        LazyColumn {
            items(items = langues, key = { it.id }) { langue ->
                var origine by remember { mutableStateOf(langue.origine) }
                var niveau by remember { mutableStateOf(langue.niveau) }
                var sliderPosition by remember { mutableFloatStateOf(langue.percent.toFloat()) }
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        OutlinedTextField(
                            value = origine ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = niveau ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Slider(value = sliderPosition,
                            steps = 5,
                            valueRange = 0f..100f,
                            onValueChange = {
                                sliderPosition = it
                                langue.percent = it.toLong()
                                hasSomethingChanged.value = true
                            })
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                viewModel.deleteLangue(langue.id, { response ->
                                    response.data?.let { langue ->
                                        cv.langues.removeAll { it.id == langue.id }
                                        langues.removeAll { it.id == langue.id }
                                    }
                                }, { error ->
                                    Log.d("Coucou", "Error $error")
                                })
                            }) {
                                Icon(Icons.Filled.Delete, "Supprimer une langue")
                                Text(text = "Supprimer cette langue")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiviteScreen(cv: CV, viewModel: UpdateCvViewModel) {
    val focusManager = LocalFocusManager.current
    val autres = remember { cv.autres.toMutableStateList() }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExtendedText(text = stringResource(R.string.activites))
            Button(onClick = {
                viewModel.createAutre(cv.id, { response ->
                    response.data?.let {
                        cv.autres.add(it)
                        autres.add(it)
                    }
                }, { error ->
                    Log.d("Coucou", "Error $error")
                })
            }) {
                Icon(Icons.Filled.Add, "Ajouter une activité")
            }
        }
        LazyColumn {
            items(items = autres, key = { it.id }) { activite ->
                var libelle by remember { mutableStateOf(activite.libelle) }
                var description by remember { mutableStateOf(activite.description) }
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        OutlinedTextField(
                            value = libelle ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                viewModel.deleteAutre(activite.id, { response ->
                                    response.data?.let { autre ->
                                        cv.autres.removeAll { it.id == autre.id }
                                        autres.removeAll { it.id == autre.id }
                                    }
                                }, { error ->
                                    Log.d("Coucou", "Error $error")
                                })
                            }) {
                                Icon(Icons.Filled.Delete, "Supprimer une activité")
                                Text(text = "Supprimer cette activité")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExperienceScreen(cv: CV, viewModel: UpdateCvViewModel) {
    val focusManager = LocalFocusManager.current
    val experiences = remember { cv.experiences.toMutableStateList() }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExtendedText(text = stringResource(R.string.experience))
            Button(onClick = {
                viewModel.createExperience(cv.id, { response ->
                    response.data?.let {
                        cv.experiences.add(it)
                        experiences.add(it)
                    }
                }, { error ->
                    Log.d("Coucou", "Error $error")
                })
            }) {
                Icon(Icons.Filled.Add, "Ajouter une expérience")
            }
        }

        LazyColumn {
            items(items = experiences, key = { it.id }) { experience ->
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
                        CustomDatePicker(titre = "Date début", date = dateDebut ?: "") { date ->
                            date?.let {
                                dateDebut = it
                                experience.dateDebut = it
                                hasSomethingChanged.value = true
                            }
                        }
                        CustomDatePicker(titre = "Date fin", date = dateFin ?: "") { date ->
                            date?.let {
                                dateFin = it
                                experience.dateFin = it
                                hasSomethingChanged.value = true
                            }
                        }
                        OutlinedTextField(
                            value = entreprise ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = poste ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                viewModel.deleteExperience(experience.id, { response ->
                                    response.data?.let { exp ->
                                        cv.experiences.removeAll { it.id == exp.id }
                                        experiences.removeAll { it.id == exp.id }
                                    }
                                }, { error ->
                                    Log.d("Coucou", "Error $error")
                                })
                            }) {
                                Icon(Icons.Filled.Delete, "Supprimer une expérience")
                                Text(text = "Supprimer cette expérience")
                            }
                        }
                    }
                    ProjetScreen(projets = experience.projets)
                }
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
                    value = nomProjet ?: "",
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
                        hasSomethingChanged.value = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descriptionProjet ?: "",
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
                        hasSomethingChanged.value = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FormationScreen(cv: CV, viewModel: UpdateCvViewModel) {
    val focusManager = LocalFocusManager.current
    val formations = remember { cv.formations.toMutableStateList() }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExtendedText(text = stringResource(R.string.education))
            Button(onClick = {
                viewModel.createFormation(cv.id, { response ->
                    response.data?.let {
                        cv.formations.add(it)
                        formations.add(it)
                    }
                }, { error ->
                    Log.d("Coucou", "Error $error")
                })
            }) {
                Icon(Icons.Filled.Add, "Ajouter une formation")
            }
        }
        LazyColumn {
            items(items = formations, key = { it.id }) { formation ->
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
                        CustomDatePicker(titre = "Date début", date = dateDebut ?: "") { date ->
                            date?.let {
                                dateDebut = it
                                formation.dateDebut = it
                                hasSomethingChanged.value = true
                            }
                        }
                        CustomDatePicker(titre = "Date fin", date = dateFin ?: "") { date ->
                            date?.let {
                                dateFin = it
                                formation.dateFin = it
                                hasSomethingChanged.value = true
                            }
                        }

                        OutlinedTextField(
                            value = etablissement ?: "",
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description ?: "",
                            maxLines = 3,
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
                                hasSomethingChanged.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            viewModel.deleteFormation(formation.id, { response ->
                                response.data?.let { forma ->
                                    cv.formations.removeAll { it.id == forma.id }
                                    formations.removeAll { it.id == forma.id }
                                }
                            }, { error ->
                                Log.d("Coucou", "Error $error")
                            })
                        }) {
                            Icon(Icons.Filled.Delete, "Supprimer une formation")
                            Text(text = "Supprimer cette formation")
                        }
                    }
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
                titre, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Start
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
