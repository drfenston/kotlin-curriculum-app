package com.cyrilmaquaire.curriculum.model.viewmodels

import androidx.lifecycle.ViewModel
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.responses.GetCvListResponse
import com.cyrilmaquaire.curriculum.network.CvApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetCvListViewModel : ViewModel() {
    var response = MutableStateFlow<GetCvListResponse?>(null)
    val loadingState = MutableStateFlow(LoadingStates.LOADING)

    fun getCvList() {
        loadingState.value = LoadingStates.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                response.value = CvApi.retrofitService.getAllCV()
                loadingState.value = LoadingStates.LOADED
            } catch (e: Throwable) {
                loadingState.value = LoadingStates.ERROR
            }
        }
    }
}
