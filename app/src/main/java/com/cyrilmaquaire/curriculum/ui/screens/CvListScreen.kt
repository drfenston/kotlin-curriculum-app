package com.cyrilmaquaire.curriculum.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cyrilmaquaire.curriculum.cvList
import com.cyrilmaquaire.curriculum.ui.NavigationItem
import com.cyrilmaquaire.curriculum.ui.theme.FenstonBlue


@Composable
fun CvListScreen(
    navController: NavController
) {
    LazyColumn {
        items(cvList) { cv ->
            Card(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(FenstonBlue)
                    ) {
                        AsyncImage(
                            model = "https://www.cyrilmaquaire.com/curriculum/uploads/" + cv.profileImage,
                            contentDescription = "Translated description of what the image contains",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 12.dp)
                    ) {
                        val nom = cv.nom?:""
                        val prenom = cv.prenom?:""
                        if(nom.isNotEmpty() || prenom.isNotEmpty()) {
                        Text(
                            text = "$nom $prenom",
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = fontOrbitronFamily
                        )
                            }
                        Text(text = cv.poste, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = getExperienceText(cv.debut)?:"",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    TextButton(
                        onClick = {
                            navController.navigate(NavigationItem.Profile.route + "/" + cv.id)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface // Utiliser la couleur de texte par défaut
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Visibility, // Icône d'œil
                            contentDescription = "Consulter",
                            modifier = Modifier.padding(end = 8.dp) // Espace entre l'icône et le texte
                        )
                        Text("Consulter")
                    }

                    TextButton(
                        onClick = {
                            navController.navigate(NavigationItem.Edit.route + "/" + cv.id)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface // Utiliser la couleur de texte par défaut
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit, // Icône de crayon
                            contentDescription = "Modifier",
                            modifier = Modifier.padding(end = 8.dp) // Espace entre l'icône et le texte
                        )
                        Text("Modifier")
                    }
                }
            }
        }
    }
}

