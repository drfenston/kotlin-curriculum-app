package com.cyrilmaquaire.curriculum.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.viewmodels.UpdateCvViewModel
import com.cyrilmaquaire.curriculum.cvList

@Composable
fun EditCvScreen(
    cvId: Long?,
    viewModel: UpdateCvViewModel,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val cv = cvList.find { it.id == cvId }
    cv?.let {
        var poste by remember { mutableStateOf(it.poste) }
        var description by remember { mutableStateOf(it.description) }
        var nom by remember { mutableStateOf(it.nom) }
        var prenom by remember { mutableStateOf(it.prenom) }
        var adresse1 by remember { mutableStateOf(it.adresse1) }
        var adresse2 by remember { mutableStateOf(it.adresse2) }
        var zipCode by remember { mutableStateOf(it.zipCode) }
        var ville by remember { mutableStateOf(it.city) }
        var telephone by remember { mutableStateOf(it.telephone) }
        var email by remember { mutableStateOf(it.mail) }
        var siteWeb by remember { mutableStateOf(it.website) }
        Column(modifier = modifier) {
            OutlinedTextField(
                value = poste,
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
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
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text(stringResource(R.string.description)) },
                onValueChange = {
                    description = it
                    cv.description = it
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = nom,
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text(stringResource(R.string.nom)) },
                onValueChange = {
                    nom = it
                    cv.nom = it
                }
            )
            OutlinedTextField(
                value = prenom,
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text(stringResource(R.string.prenom)) },
                onValueChange = {
                    prenom = it
                    cv.prenom = it
                }
            )
            OutlinedTextField(
                value = adresse1,
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
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
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text(stringResource(R.string.adresse_2)) },
                onValueChange = {
                    adresse2 = it
                    cv.adresse2 = it
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = zipCode,
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text(stringResource(R.string.code_postal)) },
                onValueChange = {
                    zipCode = it
                    cv.zipCode = it
                }
            )
            OutlinedTextField(
                value = ville,
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text(stringResource(R.string.ville)) },
                onValueChange = {
                    ville = it
                    cv.city = it
                }
            )
            OutlinedTextField(
                value = telephone,
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
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
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
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
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
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
                    cvId?.let {
                        viewModel.updateCv(
                            cvId = it,
                            cv = cv,
                            onLoginSuccess = { response ->
                                Log.d("coucou", "UpdateCV:  " + response.message)
                            },
                            onLoginError = {error -> Log.d("coucou", "Error: $error")})

                        if (loadingState.value == LoadingStates.LOADING) {
                            Log.d("coucou", "Update CV Loading")
                        } else if (loadingState.value == LoadingStates.ERROR) {
                            Log.d("coucou", "Update CV Error")
                        }
                    }
                },
                icon = { Icon(Icons.Filled.Check, "Enregistrer les changements") },
                text = { Text(text = "Enregistrer les changements") },
            )
        }
    }
}