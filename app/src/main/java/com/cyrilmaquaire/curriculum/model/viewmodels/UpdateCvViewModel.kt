package com.cyrilmaquaire.curriculum.model.viewmodels

import androidx.lifecycle.ViewModel
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.responses.AutreResponse
import com.cyrilmaquaire.curriculum.model.responses.CompTechResponse
import com.cyrilmaquaire.curriculum.model.responses.ExperienceResponse
import com.cyrilmaquaire.curriculum.model.responses.FormationResponse
import com.cyrilmaquaire.curriculum.model.responses.LangueResponse
import com.cyrilmaquaire.curriculum.model.responses.ProjetResponse
import com.cyrilmaquaire.curriculum.model.responses.GetCvResponse
import com.cyrilmaquaire.curriculum.network.CvApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UpdateCvViewModel : ViewModel() {
    val loadingState = MutableStateFlow(LoadingStates.LOADED)

    fun updateCv(
        cvId: Long,
        cv: CV,
        onLoginSuccess: (GetCvResponse) -> Unit,
        onLoginError: (String?) -> Unit
    ) {
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

    fun createAutre(
        cvId: Long,
        onCreateSuccess: (AutreResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.createAutre(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun createLangue(
        cvId: Long,
        onCreateSuccess: (LangueResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.createLangue(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun createCompetenceTechnique(
        cvId: Long,
        onCreateSuccess: (CompTechResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.createCompetenceTechnique(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun createFormation(
        cvId: Long,
        onCreateSuccess: (FormationResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.createFormation(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun createExperience(
        cvId: Long,
        onCreateSuccess: (ExperienceResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.createExperience(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }


    fun deleteAutre(
        cvId: Long,
        onCreateSuccess: (AutreResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.deleteAutre(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun deleteLangue(
        cvId: Long,
        onCreateSuccess: (LangueResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.deleteLangue(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun deleteCompetenceTechnique(
        cvId: Long,
        onCreateSuccess: (CompTechResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.deleteCompetenceTechnique(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun deleteFormation(
        cvId: Long,
        onCreateSuccess: (FormationResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.deleteFormation(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun deleteExperience(
        cvId: Long,
        onCreateSuccess: (ExperienceResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.deleteExperience(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }

    fun createProjet(
        cvId: Long,
        onCreateSuccess: (ProjetResponse) -> Unit,
        onCreateError: (String?) -> Unit
    ) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel réseau
                CvApi.retrofitService.createProjet(cvId).let {
                    // Cette partie du code est appelée lorsque l'appel réseau est terminé
                    loadingState.value = LoadingStates.LOADED

                    // On déclenche la fonction onLoginSuccess, celle-ci est passée en paramètre
                    onCreateSuccess(it)
                }
            } catch (e: Throwable) {
                onCreateError(e.message)
                loadingState.value = LoadingStates.ERROR
            }
        }
    }
}