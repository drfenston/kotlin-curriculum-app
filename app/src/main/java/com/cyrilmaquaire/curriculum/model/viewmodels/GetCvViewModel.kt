package com.cyrilmaquaire.curriculum.model.viewmodels

import androidx.lifecycle.ViewModel
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.responses.GetCvResponse
import com.cyrilmaquaire.curriculum.network.CvApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetCvViewModel : ViewModel() {
    // TODO: refaire cet appel pour coller aux autres view model
    var cv = MutableStateFlow<GetCvResponse?>(null)
    val loadingState = MutableStateFlow(LoadingStates.LOADING)

    fun getCV(id: Long) {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                cv.value = CvApi.retrofitService.getCV(id)
                loadingState.value =LoadingStates.LOADED
            } catch (e: Throwable) {
                loadingState.value = LoadingStates.ERROR
            }
        }
    }
}