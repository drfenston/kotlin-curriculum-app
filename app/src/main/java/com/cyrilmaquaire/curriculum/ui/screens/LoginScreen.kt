package com.cyrilmaquaire.curriculum.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.cvList
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.StoreData
import com.cyrilmaquaire.curriculum.model.requests.LoginRequest
import com.cyrilmaquaire.curriculum.model.viewmodels.CreateViewModel
import com.cyrilmaquaire.curriculum.model.viewmodels.MainViewModel
import com.cyrilmaquaire.curriculum.ui.NavigationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

var loginState = mutableStateOf(true)
var showError = mutableStateOf(false)

@Composable
fun LoginScreen(navController: NavController, viewModel: MainViewModel?) {
    if (loginState.value) {
        LoginForm(navController, viewModel)
    } else {
        CreateAccountForm()
    }
}

@Composable
fun LoginForm(navController: NavController, viewModel: MainViewModel?) {

    val localContext = LocalContext.current
    val storeData = StoreData(localContext)

    val loadingState = viewModel?.loadingState?.collectAsState()
    val focusManager = LocalFocusManager.current

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checkSave by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        storeData.getData(StoreData.APP_LOGIN).first()?.let { login = it }
        storeData.getData(StoreData.APP_PASSWORD).first()?.let { password = it }
        storeData.getData(StoreData.LOGIN_CHECK_SAVE).first()?.let { checkSave = it.toBoolean() }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
                if (checkSave) {
                    CoroutineScope(Dispatchers.Main).launch {
                        storeData.saveData(StoreData.APP_LOGIN, it)
                    }
                }
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
                if (checkSave) {
                    CoroutineScope(Dispatchers.Main).launch {
                        storeData.saveData(StoreData.APP_PASSWORD, it)
                    }
                }
            }
        )
        Button(onClick = {
            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    localContext,
                    localContext.getString(R.string.veuillez_remplir_tous_les_champs),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                tryToLogin(
                    login,
                    password,
                    navController = navController,
                    viewModel = viewModel,
                    context = localContext
                )
            }
        }, Modifier.padding(10.dp)) {
            if (loadingState?.value == LoadingStates.LOADING) {
                Text(stringResource(R.string.chargement))
            } else {
                Text(stringResource(R.string.se_connecter))
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(text = "Se souvenir de moi", modifier = Modifier.padding(end = 12.dp))
            Switch(
                checked = checkSave,
                onCheckedChange = {
                    checkSave = it
                    CoroutineScope(Dispatchers.Main).launch {
                        storeData.saveData(StoreData.LOGIN_CHECK_SAVE, it.toString())
                    }
                }
            )
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.padding(24.dp)
    ) {
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
    val viewModel: CreateViewModel = viewModel()
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.padding(24.dp)
    ) {
        TextButton(
            onClick = {
                loginState.value = true
            }
        ) {
            Text("Revenir à l'écran de connexion")
        }
    }
}

fun tryToLogin(
    login: String, password: String,
    viewModel: MainViewModel?,
    navController: NavController,
    context: Context
) {
    viewModel?.callLoginWebService(login = login, password = password,
        onSuccess = {
            // Si le login est correct, charger les données
            viewModel.loadData(
                onSuccess = { dataList ->
                    cvList = dataList
                    // Naviguer vers la liste des données avec succès
                    navController.navigate(NavigationItem.CvList.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onError = { errorMessage ->
                    // Afficher l'écran de login en cas d'erreur lors du chargement
                    navController.navigate(NavigationItem.Login.route)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            )
        },
        onError = { errorMessage ->
            // Naviguer vers l'écran de login en cas d'erreur lors du login
            navController.navigate(NavigationItem.Login.route)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    )
}
