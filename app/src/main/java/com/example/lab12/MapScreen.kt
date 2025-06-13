package com.example.lab12

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.example.lab12.utils.getCurrentLocation


@Composable
fun MapScreen() {

    val context = LocalContext.current
    // ubicación de Arequipa (marcador fijo)
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    var customMarkerIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }


    var mapLoaded by remember { mutableStateOf(false) }

    val rutaPoints = listOf(
        LatLng(-16.4040102, -71.559611),
        LatLng(-16.406000, -71.555000),
        LatLng(-16.408500, -71.550000),
        LatLng(-16.410000, -71.546000)
    )
    val mallAventuraPolygon = listOf(
        LatLng(-16.432292, -71.509145),
        LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310),
        LatLng(-16.432566, -71.508853)
    )


    val parqueLambramaniPolygon = listOf(
        LatLng(-16.422704, -71.530830),
        LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110),
        LatLng(-16.423050, -71.530600)
    )

    val plazaDeArmasPolygon = listOf(
        LatLng(-16.398866, -71.536961),
        LatLng(-16.398744, -71.536529),
        LatLng(-16.399178, -71.536289),
        LatLng(-16.399299, -71.536721)
    )

    // cntrol de camara del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            userLocation ?: arequipaLocation, // aqui usa la mi ubiacion actual si esya disponible
            12f
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context) { latLng ->
                userLocation = latLng

                cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(latLng, 15f)
            }
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                mapLoaded = true
            }
        ) {
            if (mapLoaded && customMarkerIcon == null) {
                val originalBitmap = BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.icon
                )

                val scaledBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    64,
                    64,
                    false
                )

                customMarkerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
            }



            customMarkerIcon?.let { icon ->
                Marker(
                    state = rememberMarkerState(position = arequipaLocation),
                    icon = icon,
                    title = "Arequipa, Perú"
                )
            }

            // marcador para la ubicación del usuario
            userLocation?.let { loc ->
                Marker(
                    state = rememberMarkerState(position = loc),
                    title = "Mi Ubicación"

                )
            }

            Polyline(
                points = rutaPoints,
                color = Color.Blue,
                width = 5f
            )
            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )

        }
    }
}