package com.cyrilmaquaire.curriculum.model.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.StoreData
import com.cyrilmaquaire.curriculum.model.requests.LoginRequest
import com.cyrilmaquaire.curriculum.model.responses.LoginResponse
import com.cyrilmaquaire.curriculum.network.CvApi
import com.cyrilmaquaire.curriculum.token
import com.cyrilmaquaire.curriculum.user
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val userPreferences: StoreData) : ViewModel() {
    val loadingState = MutableStateFlow(LoadingStates.LOADED)

    // Fonction pour vérifier le login et mot de passe
    fun checkLoginAndLoginWebService(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val login = userPreferences.login.first()
                val password = userPreferences.password.first()

                if (login != null && password != null) {
                    // Faire la requête réseau pour obtenir le token et userID
                    val response = loginWebService(login, password)
                    if (response.token.isNotEmpty()) {
                        token = response.token
                        user = response.data

                        // Sauvegarde les données du token et userID (si nécessaire)
                        onSuccess()
                    } else {
                        onError("Erreur lors de la connexion")
                    }
                } else {
                    onError("Pas de login et password trouvés")
                }
            } catch (e: Exception) {
                onError("Erreur: ${e.message}")
            }
        }
    }

    fun callLoginWebService(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        viewModelScope.launch {
            try {
                // Faire la requête réseau pour obtenir le token et userID
                val response = loginWebService(login, password)
                if (response.token.isNotEmpty()) {
                    token = response.token
                    user = response.data

                    // Sauvegarde les données du token et userID (si nécessaire)
                    onSuccess()
                    loadingState.value = LoadingStates.LOADED
                } else {
                    onError("Erreur lors de la connexion")
                    loadingState.value = LoadingStates.ERROR
                }
            } catch (e: Exception) {
                onError("Erreur: ${e.message}")
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    // Fonction suspendue pour appeler l'API de login
    private suspend fun loginWebService(login: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            // Appel à Retrofit ou un autre service web pour obtenir le token et l'userID
            CvApi.retrofitService.login(LoginRequest(login, password))
        }
    }

    // Fonction pour charger la liste de données après login
    fun loadData(onSuccess: (List<CV>) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (token != null && user != null) {
                    val dataList = loadDataWebService(user?.id ?: 0)
                    onSuccess(dataList)
                } else {
                    onError("Token $token ou UserId ${user?.id} sont null")
                }
            } catch (e: Exception) {
                onError("Erreur lors du chargement des données: ${e.message}")
            }
        }
    }

    // Fonction suspendue pour charger les données de la liste
    private suspend fun loadDataWebService(userID: Long): List<CV> {
        return withContext(Dispatchers.IO) {
            // Appel à l'API pour récupérer les données
            CvApi.retrofitService.getAllCV(userID).data
        }
    }
}
