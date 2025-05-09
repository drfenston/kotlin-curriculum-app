package com.cyrilmaquaire.curriculum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.StoreData
import com.cyrilmaquaire.curriculum.model.responses.LoginResponse
import com.cyrilmaquaire.curriculum.ui.CvApp
import com.cyrilmaquaire.curriculum.ui.theme.CurriculumTheme

var cvList: List<CV> = listOf()
var token:String? = null
var user:LoginResponse.User? = null

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Créer une instance de UserPreferences
        val userPreferences = StoreData(applicationContext)
        setContent {
            CurriculumTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CvApp(userPreferences)
                }
            }
        }
    }
}

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)