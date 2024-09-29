package com.cyrilmaquaire.curriculum.ui.screens


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.cyrilmaquaire.curriculum.model.responses.UploadPhotoResponse
import com.cyrilmaquaire.curriculum.network.CvApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CapturePhotoScreen(apiService: CvApiService, onPictureUpload: (data: UploadPhotoResponse.Data) -> Unit) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Lancer l'appareil photo avec un launcher d'activité
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            // Récupérer le fichier à partir de l'URI
            val resizedFile = resizeImage(context, photoUri!!, maxWidth = 1024, maxHeight = 1024)
            if (resizedFile != null) {
                uploadImage(apiService, resizedFile) {data ->
                    onPictureUpload(data)
                } // Appel à uploadImage avec l'image réduite
            } else {
                Log.e("CapturePhotoScreen", "Erreur : Impossible de redimensionner l'image")
            }
        }
    }

    // Gestionnaire de permission pour la caméra
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si l'utilisateur a accepté, on peut lancer la caméra
            val photoFile = createImageFile(context)
            photoFile?.also { file ->
                photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                cameraLauncher.launch(photoUri) // Lancer l'appareil photo
            }
        } else {
            // Permission refusée, ne rien faire ou afficher un message
            Toast.makeText(context, "Permission refusée", Toast.LENGTH_SHORT).show()
        }
    }

    Button(onClick = {
        // Vérifier si la permission a déjà été accordée
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission déjà accordée, on peut lancer la caméra
            val photoFile = createImageFile(context)
            photoFile?.also { file ->
                photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                cameraLauncher.launch(photoUri) // Lancer la capture de l'image
            }
        } else {
            // Demander la permission pour l'appareil photo
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Prendre une photo")
    }
}

fun createImageFile(context: Context): File? {
    return try {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    } catch (ex: IOException) {
        Log.e("CameraCapture", "Erreur lors de la création du fichier image", ex)
        null
    }
}

fun uploadImage(apiService: CvApiService, file: File, onPictureUpload: (data: UploadPhotoResponse.Data) -> Unit) {
    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("upload", file.name, requestFile)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response: UploadPhotoResponse = apiService.uploadPhoto(body)
            if (response.data != null) {
                Log.d("Upload", "Succès de l'envoi : ${response.message}")
                response.data?.let{onPictureUpload(it)}
            } else {
                Log.e("Upload", "Échec de l'envoi : ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("Upload", "Erreur lors de l'envoi", e)
        }
    }
}

fun resizeImage(context: Context, imageUri: Uri, maxWidth: Int, maxHeight: Int): File? {
    // Corriger l'orientation de l'image avant de la redimensionner
    val correctedBitmap = correctImageOrientation(context, imageUri) ?: return null

    // Calcul des nouvelles dimensions
    val ratioBitmap = correctedBitmap.width.toFloat() / correctedBitmap.height.toFloat()
    val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

    var finalWidth = maxWidth
    var finalHeight = maxHeight
    if (ratioMax > 1) {
        finalWidth = (maxHeight * ratioBitmap).toInt()
    } else {
        finalHeight = (maxWidth / ratioBitmap).toInt()
    }

    // Redimensionner l'image
    val resizedBitmap = Bitmap.createScaledBitmap(correctedBitmap, finalWidth, finalHeight, true)

    // Enregistrer l'image redimensionnée dans un fichier temporaire
    val file = File(context.cacheDir, "resized_image.jpg")
    val outputStream = FileOutputStream(file)
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
    outputStream.flush()
    outputStream.close()

    return file
}

fun correctImageOrientation(context: Context, imageUri: Uri): Bitmap? {
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    val exifInterface = ExifInterface(context.contentResolver.openInputStream(imageUri) ?: return bitmap)
    val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
        else -> bitmap // Pas besoin de rotation
    }
}

fun rotateImage(bitmap: Bitmap, degree: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}



