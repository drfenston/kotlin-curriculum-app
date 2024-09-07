/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cyrilmaquaire.curriculum.model.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrilmaquaire.curriculum.model.responses.Response
import com.cyrilmaquaire.curriculum.network.CvApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface CvUiState {
    data class Success(val response: Response) : CvUiState
    data object Error : CvUiState
    data object Loading : CvUiState
}

class CvViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var cvUiState: CvUiState by mutableStateOf(CvUiState.Loading)
        private set

    fun getAllCV() {
        viewModelScope.launch {
            cvUiState = CvUiState.Loading
            cvUiState = try {
                val response = CvApi.retrofitService.getAllCV()
                CvUiState.Success(response)
            } catch (e: IOException) {
                CvUiState.Error
            } catch (e: HttpException) {
                CvUiState.Error
            }
        }
    }
}
