package com.example.lab12

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker

@Composable
fun MapScreen() {
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            arequipaLocation,
            12f
        )
    }

    val context = LocalContext.current
    var customMarkerIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }

    var mapLoaded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                mapLoaded = true
            }
        ) {
            // Solo creamos el icono cuando el mapa ya está cargado
            if (mapLoaded && customMarkerIcon == null) {
                val originalBitmap = BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.icon
                )

                val scaledBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    100, // ancho en píxeles (ajusta según necesidad)
                    100, // alto en píxeles
                    false
                )

                customMarkerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
            }


            // Dibujamos el marcador solo cuando el icono está listo
            customMarkerIcon?.let { icon ->
                Marker(
                    state = rememberMarkerState(position = arequipaLocation),
                    icon = icon,
                    title = "Arequipa, Perú"
                )
            }
        }
    }
}