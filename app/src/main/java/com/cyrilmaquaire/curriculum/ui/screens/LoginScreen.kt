package com.cyrilmaquaire.curriculum.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.requests.LoginRequest
import com.cyrilmaquaire.curriculum.model.viewmodels.LoginViewModel
import com.cyrilmaquaire.curriculum.token
import com.cyrilmaquaire.curriculum.ui.NavigationItem
import com.cyrilmaquaire.curriculum.user
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var loginState = mutableStateOf(true)
var showError = mutableStateOf(false)

@Composable
fun LoginScreen(navController: NavController) {
    if (loginState.value) {
        LoginForm(navController)
    } else {
        CreateAccountForm()
    }
}

@Composable
fun LoginForm(navController: NavController) {
    val localContext = LocalContext.current
    val viewModel: LoginViewModel = viewModel()
    val loadingState = viewModel.loadingState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var login by remember { mutableStateOf("Fenston") }
        var password by remember { mutableStateOf("Molyneux31.") }
        OutlinedTextField(
            value = login,
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.login)) },
            onValueChange = {
                login = it
            }
        )
        OutlinedTextField(
            value = password,
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(stringResource(R.string.password)) },
            onValueChange = {
                password = it
            }
        )
        Button(onClick = {
            val loginRequest = LoginRequest(login, password)

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    localContext,
                    localContext.getString(R.string.veuillez_remplir_tous_les_champs),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                viewModel.login(loginRequest) { response ->
                    Log.d("coucou", "Dans la réponse du login")
                    token = response.token
                    user = response.data
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate(NavigationItem.CvList.route)
                    }
                }
            }
        }, Modifier.padding(10.dp)) {
            if (loadingState.value == LoadingStates.LOADING) {
                Text(stringResource(R.string.chargement))
            } else {
                Text(stringResource(R.string.se_connecter))
            }
        }

        TextButton(
            onClick = {
                loginState.value = false
            }
        ) {
            Text("Créer un compte")
        }
    }
}

@Composable
fun CreateAccountForm() {
    val localContext = LocalContext.current
    val viewModel: LoginViewModel = viewModel()
    val loadingState = viewModel.loadingState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var login by remember { mutableStateOf("Fenston") }
        var password by remember { mutableStateOf("Molyneux31.") }
        OutlinedTextField(
            value = login,
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            label = { Text(stringResource(R.string.login)) },
            onValueChange = {
                login = it
            }
        )
        OutlinedTextField(
            value = password,
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(stringResource(R.string.password)) },
            onValueChange = {
                password = it
            }
        )
        Button(onClick = {
            val loginRequest = LoginRequest(login, password)

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    localContext,
                    localContext.getString(R.string.veuillez_remplir_tous_les_champs),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                viewModel.create(loginRequest, {
                    loginState.value = true
                    showError.value = false
                }, { error ->
                    showError.value = true
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            localContext,
                            error, Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
            }
        }, Modifier.padding(10.dp)) {
            if (loadingState.value == LoadingStates.LOADING) {
                Text(stringResource(R.string.chargement))
            } else {
                Text(stringResource(R.string.creer_compte))
            }
        }

        if (showError.value) Text("Il y a une erreur.", color = MaterialTheme.colorScheme.error)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginForm(navController = rememberNavController())
}
