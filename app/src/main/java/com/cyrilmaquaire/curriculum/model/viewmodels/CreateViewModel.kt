package com.cyrilmaquaire.curriculum.model.viewmodels

import androidx.lifecycle.ViewModel
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.requests.LoginRequest
import com.cyrilmaquaire.curriculum.model.responses.CreateResponse
import com.cyrilmaquaire.curriculum.network.CvApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateViewModel : ViewModel() {
    val loadingState = MutableStateFlow(LoadingStates.LOADED)

    fun create(
        loginResquest: LoginRequest,
        onCreateSuccess: (CreateResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        // Passer le loadingState à LOADING pour afficher le chargement
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.createUser(loginResquest).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onCreateSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                loadingState.value = LoadingStates.ERROR
                onCreateError(e.message)
            }
        }
    }
}