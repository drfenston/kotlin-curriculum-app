package com.cyrilmaquaire.curriculum.model.viewmodels

import androidx.lifecycle.ViewModel
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.responses.GetCvResponse
import com.cyrilmaquaire.curriculum.network.CvApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UpdateCvViewModel : ViewModel() {
    val loadingState = MutableStateFlow(LoadingStates.LOADED)

    fun updateCv(cvId: Long, cv: CV , onLoginSuccess: (GetCvResponse) -> Unit, onLoginError: (String?) -> Unit) {
        // Passer le loadingState à LOADING pour afficher le chargement
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.updateCV(cvId, cv).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onLoginSuccess(it)
                }
            } catch (e: Throwable) {

                onLoginError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }
}