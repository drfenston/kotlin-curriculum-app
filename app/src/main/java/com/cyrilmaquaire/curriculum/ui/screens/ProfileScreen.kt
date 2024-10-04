package com.cyrilmaquaire.curriculum.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cyrilmaquaire.curriculum.R
import com.cyrilmaquaire.curriculum.data.LoadingStates
import com.cyrilmaquaire.curriculum.model.Autre
import com.cyrilmaquaire.curriculum.model.CV
import com.cyrilmaquaire.curriculum.model.CompetenceTechnique
import com.cyrilmaquaire.curriculum.model.Experience
import com.cyrilmaquaire.curriculum.model.Formation
import com.cyrilmaquaire.curriculum.model.Langue
import com.cyrilmaquaire.curriculum.model.viewmodels.GetCvViewModel
import com.cyrilmaquaire.curriculum.provider
import com.cyrilmaquaire.curriculum.ui.elements.LinkText
import com.cyrilmaquaire.curriculum.ui.elements.LinkTextData
import com.cyrilmaquaire.curriculum.ui.theme.CurriculumTheme
import com.cyrilmaquaire.curriculum.ui.theme.DateText
import com.cyrilmaquaire.curriculum.ui.theme.ExtendedText
import com.cyrilmaquaire.curriculum.ui.theme.FenstonBlue
import java.time.LocalDate
import java.time.Period
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException


val fontOrbitron = GoogleFont("Orbitron")
val fontAntonio = GoogleFont("Antonio")

val fontOrbitronFamily = FontFamily(
    Font(googleFont = fontOrbitron, fontProvider = provider),
)

val fontAntonioFamily = FontFamily(
    Font(googleFont = fontAntonio, fontProvider = provider)
)

val moisAbreges = arrayOf(
    "Jan", "Fév", "Mar", "Avr", "Mai", "Juin",
    "Juil", "Août", "Sept", "Octt", "Nov", "Déc"
)

@Composable
fun ProfileScreen(
    userId: Long?, viewModel: GetCvViewModel, modifier: Modifier = Modifier
) {
    val cv = viewModel.cv.collectAsState()
    // Variables qui changeront d'état lors du chargement des données
    val loadingState = viewModel.loadingState.collectAsState()

    LaunchedEffect(Unit) {
        userId?.let { viewModel.getCV(it) }
    }

    when (loadingState.value) {
        LoadingStates.LOADING -> {
            // Affichage d'un loader
            LoadingScreen()
        }

        LoadingStates.LOADED -> {
            // Affichage de la liste
            cv.value?.let {
                ResultScreen(it.data, modifier)
            }
        }

        LoadingStates.ERROR -> {
            // Affichage d'un message d'erreur
            ErrorScreen()
        }
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(cv: CV?, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
    ) {
        cv.let {
            Column {
                it?.let { data ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .clip(CircleShape)
                                .background(FenstonBlue)
                        ) {
                            AsyncImage(
                                model = "https://www.cyrilmaquaire.com/curriculum/uploads/" + data.profileImage,
                                contentDescription = "Translated description of what the image contains",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(300.dp)
                                    .width(300.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    it?.let { data ->
                        val nom = data.nom ?: ""
                        val prenom = data.prenom ?: ""
                        Text(
                            text = "$nom $prenom",
                            style = MaterialTheme.typography.headlineMedium,
                            fontFamily = fontOrbitronFamily
                        )
                        Text(text = data.poste, style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = getExperienceText(cv?.debut) ?: "",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                it?.let { data ->
                    ContactCard(cv = data)
                    CompetenceTechniqueCard(competencesTechniques = data.competenceTechniques)
                    LangueCard(data.langues)
                    AutresCard(autres = data.autres)
                    DescriptionCard(data)
                    ExperienceCard(experiences = data.experiences)
                    FormationCard(formations = data.formations)
                }
            }
        }

    }
}

@Composable
fun DescriptionCard(cv: CV) {
    if (!cv.description.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer, // Couleur de fond
                    shape = MaterialTheme.shapes.small // Arrondir les bords
                )
                .padding(16.dp).fillMaxWidth() // Ajout d'un espacement intérieur
        ) {
            Text(
                text = cv.description ?: "",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = fontAntonioFamily
            )
        }
    }
}

@Composable
fun LangueCard(langues: List<Langue>) {
    if (langues.isNotEmpty()) {
        Column {
            val textColor = MaterialTheme.colorScheme.onSurface
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon_langues),
                    contentDescription = "Icône des langues", // Description pour l'accessibilité
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    colorFilter = ColorFilter.tint(textColor) // Teinte l'image avec la couleur du texte

                )

                ExtendedText(
                    text = stringResource(R.string.langues)
                )
            }
            for (langue in langues) {
                Row(Modifier.padding(all = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = langue.origine ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    LinearProgressIndicator(
                        progress = { langue.percent.toFloat() / 100 },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompetenceTechniqueCard(competencesTechniques: List<CompetenceTechnique>) {
    if (competencesTechniques.isNotEmpty()) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            val textColor = MaterialTheme.colorScheme.onSurface
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon_comptech),
                    contentDescription = "Icône competence technique", // Description pour l'accessibilité
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    colorFilter = ColorFilter.tint(textColor) // Teinte l'image avec la couleur du texte

                )
                ExtendedText(text = stringResource(R.string.competences_techniques))
            }
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
            ) {
                for (competenceTechnique in competencesTechniques) {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomAsyncImage(
                                imageUrl = "https://www.cyrilmaquaire.com/curriculum/uploads/${competenceTechnique.libelle}.png",
                                fallbackImageResId = R.drawable.comptech_default, // Remplace par l'ID de ta ressource drawable
                                contentDescription = competenceTechnique.libelle,
                                size = 60.dp
                            )

                        }
                        Text(
                            text = competenceTechnique.libelle ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                        competenceTechnique.competence?.let {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomAsyncImage(
    imageUrl: String,
    fallbackImageResId: Int,
    contentDescription: String?,
    size: Dp
) {
    val finalImageUrl by remember { mutableStateOf(imageUrl) }
    var hasError by remember { mutableStateOf(false) }

    AsyncImage(
        model = finalImageUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .height(size)
            .width(size),
        onSuccess = {
            hasError = false // Réinitialiser l'erreur si le chargement réussit
        },
        onError = {
            hasError = true // En cas d'erreur
        }
    )

    // Affiche l'image locale si une erreur s'est produite
    if (hasError) {
        Image(
            painter = painterResource(id = fallbackImageResId),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(size)
                .width(size)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AutresCard(autres: List<Autre>) {
    if (autres.isNotEmpty()) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            val textColor = MaterialTheme.colorScheme.onSurface
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon_activites),
                    contentDescription = "Icône autre", // Description pour l'accessibilité
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    colorFilter = ColorFilter.tint(textColor) // Teinte l'image avec la couleur du texte

                )
                ExtendedText(text = stringResource(R.string.activites))
            }
            FlowRow(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                maxItemsInEachRow = 2
            ) {
                for (autre in autres) {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(color = Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomAsyncImage(
                                imageUrl = "https://www.cyrilmaquaire.com/curriculum/uploads/${autre.libelle}.png",
                                fallbackImageResId = R.drawable.activity_default, // Remplace par l'ID de ta ressource drawable
                                contentDescription = autre.libelle,
                                size = 80.dp
                            )
                        }
                        Text(
                            text = autre.libelle ?: "",
                            modifier = Modifier.padding(bottom = 12.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactCard(cv: CV) {
    val context = LocalContext.current
    val adresse1 = cv.adresse1 ?: ""
    val adresse2 = cv.adresse2 ?: ""
    val zipCode = cv.zipCode ?: ""
    val city = cv.city ?: ""
    val telephone = cv.telephone
    val mail = cv.mail
    val website = cv.website

    Row(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {

        if (adresse1.isEmpty() && adresse2.isEmpty() && zipCode.isEmpty() && city.isEmpty() && telephone.isNullOrEmpty() && mail.isNullOrEmpty() && website.isNullOrEmpty()) {
            Text(text = "Aucune information renseignée", modifier = Modifier.padding(top = 16.dp))
        } else {
            Column {
                val textColor = MaterialTheme.colorScheme.onSurface
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_contact),
                        contentDescription = "Icône contact", // Description pour l'accessibilité
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(textColor) // Teinte l'image avec la couleur du texte

                    )
                    ExtendedText(text = stringResource(R.string.contact))
                }
                telephone?.let {
                    Row(
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telephone"))
                                context.startActivity(intent)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = stringResource(R.string.telephone),
                            Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = telephone,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }

                mail?.let {
                    Row(
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$mail")
                                }
                                context.startActivity(intent)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = stringResource(R.string.email),
                            Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = mail,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }

                website?.let {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = stringResource(R.string.website),
                            Modifier.padding(horizontal = 16.dp)
                        )
                        LinkText(
                            linkTextData = listOf(
                                LinkTextData(
                                    text = website,
                                    tag = "icon_1_author",
                                    annotation = "http://$website",
                                    onClick = {
                                        val i = Intent(Intent.ACTION_VIEW)
                                        i.setData(Uri.parse(it.item))
                                        context.startActivity(i)
                                    },
                                )
                            )
                        )
                    }
                }

                if (adresse1.isNotEmpty() || adresse2.isNotEmpty() || zipCode.isNotEmpty() || city.isNotEmpty()) {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = "Mail",
                            Modifier.padding(horizontal = 16.dp)
                        )
                        Box {
                            Column {
                                if (adresse1.isNotEmpty()) Text(text = adresse1)
                                if (adresse2.isNotEmpty()) Text(text = adresse2)
                                if (zipCode.isNotEmpty() || city.isNotEmpty()) Text(text = cv.zipCode + " " + cv.city)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExperienceCard(experiences: List<Experience>) {
    if (experiences.isNotEmpty()) {
        Column {
            val textColor = MaterialTheme.colorScheme.onSurface
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon_experience),
                    contentDescription = "Icône experiences", // Description pour l'accessibilité
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    colorFilter = ColorFilter.tint(textColor) // Teinte l'image avec la couleur du texte

                )
                ExtendedText(text = stringResource(R.string.experience))
            }
            for (experience in experiences.sortedByDescending {
                parseDate(
                    it.dateDebut ?: ""
                )?.year
            }) {
                val dateDebut = parseDate(experience.dateDebut ?: "")
                val dateFin = parseDate(experience.dateFin ?: "")
                DateText(
                    text = moisAbreges[(dateDebut?.monthValue
                        ?: 1) - 1] + " " + dateDebut?.year + " - " + moisAbreges[(dateFin?.monthValue
                        ?: 1) - 1] + " " + dateFin?.year
                )
                Text(
                    text = experience.entreprise ?: "",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = experience.poste ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Box(modifier = Modifier.padding(all = 8.dp)) {
                    Column {
                        for (projet in experience.projets) {
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Email",
                                    Modifier.padding(end = 16.dp)
                                )
                                Text(
                                    text = "Projet " + projet.nom,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }

                            Text(
                                text = projet.description ?: "",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun FormationCard(formations: List<Formation>) {
    if (formations.isNotEmpty()) {
        Column {
            val textColor = MaterialTheme.colorScheme.onSurface
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon_education),
                    contentDescription = "Icône education", // Description pour l'accessibilité
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    colorFilter = ColorFilter.tint(textColor) // Teinte l'image avec la couleur du texte

                )
                ExtendedText(text = stringResource(R.string.education))
            }
            for (formation in formations.sortedByDescending {
                parseDate(
                    it.dateDebut ?: ""
                )?.year
            }) {
                val dateDebut = parseDate(formation.dateDebut ?: "")
                val dateFin = parseDate(formation.dateFin ?: "")
                DateText(
                    text = moisAbreges[(dateDebut?.monthValue
                        ?: 1) - 1] + " " + dateDebut?.year + " - " + moisAbreges[(dateFin?.monthValue
                        ?: 1) - 1] + " " + dateFin?.year
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = formation.etablissement ?: "",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = formation.description ?: "",
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    CurriculumTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    CurriculumTheme {
        ErrorScreen()
    }
}

fun parseDate(dateString: String): ZonedDateTime? {
    return try {
        ZonedDateTime.parse(dateString)
    } catch (e: DateTimeParseException) {
        println("Erreur de parsing: ${e.message}")
        null // Retourne null en cas d'erreur
    }
}

fun getExperienceText(debut: String?): String? {
    if (debut.isNullOrEmpty()) {
        return null // Si la date est vide ou nulle, on ne renvoie rien
    }

    // Convertir la chaîne en LocalDate
    val startDate = try {
        ZonedDateTime.parse(debut)
    } catch (e: DateTimeParseException) {
        return null // Si le format de la date n'est pas valide, renvoyer null
    }

    val currentDate = LocalDate.now()

    // Calculer la différence en années et en mois
    val period = Period.between(startDate.toLocalDate(), currentDate)
    val years = period.years
    val months = period.months
    val totalMonths = years * 12 + months

    return when {
        totalMonths < 0 -> null // Date future non valide
        totalMonths < 12 -> "$totalMonths mois d'expérience" // Moins d'un an
        else -> {
            val yearLabel = if (years == 1) "an" else "ans"
            "$years $yearLabel d'expérience" // 1 an ou plus
        }
    }
}
