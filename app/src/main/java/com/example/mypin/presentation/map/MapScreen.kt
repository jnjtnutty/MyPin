// Figma: https://www.figma.com/design/RbxZunWIJGyF1YrWcgE54q/MyPin-Mobile-Login-Design?node-id=15-46
package com.example.mypin.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.BitmapShader
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.mypin.domain.model.PinItem
import com.example.mypin.ui.theme.MyPinTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonOptions
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

private val PrimaryDark = ComposeColor(0xFF212529)
private val White = ComposeColor(0xFFFFFFFF)
private val Gray600 = ComposeColor(0xFF6B7280)
private val LightGrayBg = ComposeColor(0xFFE6E8EB)
private val PlaceholderGray = ComposeColor(0xFF808799)
private val SortButtonBg = ComposeColor(0xFFF5F6F7)
private val DragHandleColor = ComposeColor(0xFFCCD4E0)
private val DividerColor = ComposeColor(0xFFE6E8EB)

private const val MAP_STYLE_URL = "https://basemaps.cartocdn.com/gl/positron-gl-style/style.json"
private const val PINS_SOURCE_ID = "pins-source"
private const val PINS_LAYER_ID = "pins-layer"
private const val CLUSTER_LAYER_ID = "pins-cluster"
private const val CLUSTER_COUNT_LAYER_ID = "pins-cluster-count"
private const val DEFAULT_ZOOM = 13.0
private const val SINGLE_PIN_ZOOM = 15.0

private val CATEGORIES = listOf("All", "Coffee", "Food", "Nature", "Art", "Nightlife", "Shopping", "Stay")

private val CATEGORY_COLORS: Map<String, List<ComposeColor>> = mapOf(
    "Coffee" to listOf(ComposeColor(0xFFD98C4D), ComposeColor(0xFF734729)),
    "Food" to listOf(ComposeColor(0xFFF2A84D), ComposeColor(0xFFA65C1A)),
    "Art" to listOf(ComposeColor(0xFFC775EB), ComposeColor(0xFF5C2980)),
    "Shopping" to listOf(ComposeColor(0xFFF2A84D), ComposeColor(0xFFA65C1A)),
    "Nature" to listOf(ComposeColor(0xFF66BB6A), ComposeColor(0xFF2E7D32)),
    "Nightlife" to listOf(ComposeColor(0xFF7E57C2), ComposeColor(0xFF4527A0)),
    "Stay" to listOf(ComposeColor(0xFF42A5F5), ComposeColor(0xFF1565C0))
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel(),
    onNavigateToAddPin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        viewModel.setLocationPermissionGranted(locationPermissionState.allPermissionsGranted)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is MapUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading map...", color = PrimaryDark)
                }
            }
            is MapUiState.Success -> {
                MapContent(
                    pins = state.filteredPins,
                    allPins = state.pins,
                    selectedPinId = state.selectedPinId,
                    selectedCategory = state.selectedCategory,
                    isLocationPermissionGranted = state.isLocationPermissionGranted,
                    onPinSelected = viewModel::selectPin,
                    onCategorySelected = viewModel::selectCategory,
                    onClearPinSelection = viewModel::clearPinSelection,
                    onNavigateToAddPin = onNavigateToAddPin,
                    onRequestLocationPermission = {
                        locationPermissionState.launchMultiplePermissionRequest()
                    }
                )
            }
            is MapUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = PrimaryDark)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Retry",
                            color = ComposeColor(0xFF4A90D9),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { viewModel.loadPins() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapContent(
    pins: List<PinItem>,
    allPins: List<PinItem>,
    selectedPinId: String?,
    selectedCategory: String,
    isLocationPermissionGranted: Boolean,
    onPinSelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onClearPinSelection: () -> Unit,
    onNavigateToAddPin: () -> Unit,
    onRequestLocationPermission: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var mapStyle by remember { mutableStateOf<Style?>(null) }

    val mapView = rememberMapViewWithLifecycle()

    LaunchedEffect(Unit) {
        MapLibre.getInstance(context)
    }

    AndroidView(
        factory = { _ ->
            mapView.apply {
                getMapAsync { map ->
                    mapLibreMap = map
                    map.uiSettings.isRotateGesturesEnabled = true
                    map.uiSettings.isZoomGesturesEnabled = true
                    map.uiSettings.isScrollGesturesEnabled = true
                    map.uiSettings.isAttributionEnabled = false
                    map.uiSettings.isLogoEnabled = false

                    val sfCenter = LatLng(37.7749, -122.4194)
                    map.cameraPosition = CameraPosition.Builder()
                        .target(sfCenter)
                        .zoom(DEFAULT_ZOOM)
                        .build()

                    map.addOnMapClickListener {
                        onClearPinSelection()
                        false
                    }

                    map.setStyle(MAP_STYLE_URL) { style ->
                        mapStyle = style
                        addPinSource(style, pins, selectedPinId)
                        addPinLayers(style)
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    LaunchedEffect(pins, selectedPinId) {
        val style = mapStyle ?: return@LaunchedEffect
        val map = mapLibreMap ?: return@LaunchedEffect
        updatePinSource(style, pins, selectedPinId)
        fitCameraToPins(map, pins)
        loadPinImages(context, style, pins)
        selectedPinId?.let { pinId ->
            val pin = pins.find { it.id == pinId }
            if (pin != null) {
                val target = LatLng(pin.latitude, pin.longitude)
                map.easeCamera(
                    CameraUpdateFactory.newLatLngZoom(target, SINGLE_PIN_ZOOM),
                    800
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 12.dp)
        ) {
            SearchBarSection()

            Spacer(Modifier.height(16.dp))

            CategoryChips(
                categories = CATEGORIES,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp)
                .padding(bottom = 380.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    if (isLocationPermissionGranted) {
                        coroutineScope.launch {
                            recenterToUserLocation(context, mapLibreMap)
                        }
                    } else {
                        onRequestLocationPermission()
                    }
                },
                icon = Icons.Default.MyLocation,
                contentDescription = "My Location"
            )

            Spacer(Modifier.height(20.dp))

            FloatingActionButton(
                onClick = onNavigateToAddPin,
                icon = Icons.Default.Add,
                contentDescription = "Add Pin"
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomSheetSection(
                pins = pins,
                selectedPinId = selectedPinId,
                onPinClick = onPinSelected
            )

            TabBar()
        }
    }
}

@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }

    return mapView
}

private fun addPinSource(style: Style, pins: List<PinItem>, selectedPinId: String? = null) {
    val existingSource = style.getSource(PINS_SOURCE_ID) as? GeoJsonSource
    if (existingSource != null) {
        existingSource.setGeoJson(buildFeatureCollection(pins, selectedPinId))
    } else {
        val source = GeoJsonSource(
            PINS_SOURCE_ID,
            buildFeatureCollection(pins, selectedPinId),
            GeoJsonOptions()
                .withCluster(true)
                .withClusterRadius(50)
                .withClusterMaxZoom(14)
        )
        style.addSource(source)
    }
}

private fun updatePinSource(style: Style, pins: List<PinItem>, selectedPinId: String?) {
    val source = style.getSource(PINS_SOURCE_ID) as? GeoJsonSource ?: return
    source.setGeoJson(buildFeatureCollection(pins, selectedPinId))
}

private fun addPinLayers(style: Style) {
    if (style.getLayer(CLUSTER_LAYER_ID) == null) {
        val clusterLayer = CircleLayer(CLUSTER_LAYER_ID, PINS_SOURCE_ID)
        clusterLayer.setFilter(Expression.has("point_count"))
        clusterLayer.setProperties(
            PropertyFactory.circleRadius(20f),
            PropertyFactory.circleColor("#212529"),
            PropertyFactory.circleStrokeWidth(3f),
            PropertyFactory.circleStrokeColor("#FFFFFF")
        )
        style.addLayer(clusterLayer)
    }

    if (style.getLayer(CLUSTER_COUNT_LAYER_ID) == null) {
        val countLayer = SymbolLayer(CLUSTER_COUNT_LAYER_ID, PINS_SOURCE_ID)
        countLayer.setFilter(Expression.has("point_count"))
        countLayer.setProperties(
            PropertyFactory.textField("{point_count}"),
            PropertyFactory.textSize(13f),
            PropertyFactory.textColor("#FFFFFF"),
            PropertyFactory.textIgnorePlacement(true),
            PropertyFactory.textAllowOverlap(true)
        )
        style.addLayer(countLayer)
    }

    if (style.getLayer(PINS_LAYER_ID) == null) {
        val pinLayer = SymbolLayer(PINS_LAYER_ID, PINS_SOURCE_ID)
        pinLayer.setFilter(Expression.not(Expression.has("point_count")))
        pinLayer.setProperties(
            PropertyFactory.iconImage(Expression.concat(Expression.literal("pin-"), Expression.get("id"))),
            PropertyFactory.iconSize(
                Expression.switchCase(
                    Expression.eq(Expression.get("selected"), Expression.literal(true)),
                    Expression.literal(1.15f),
                    Expression.literal(1.0f)
                )
            ),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconIgnorePlacement(true)
        )
        style.addLayer(pinLayer)
    }
}

private fun buildFeatureCollection(pins: List<PinItem>, selectedPinId: String? = null): FeatureCollection {
    val features = pins.map { pin ->
        Feature.fromGeometry(
            Point.fromLngLat(pin.longitude, pin.latitude)
        ).apply {
            addStringProperty("id", pin.id)
            addStringProperty("name", pin.name)
            addStringProperty("category", pin.category)
            addBooleanProperty("selected", pin.id == selectedPinId)
        }
    }
    return FeatureCollection.fromFeatures(features)
}

private suspend fun loadPinImages(
    context: Context,
    style: Style,
    pins: List<PinItem>
) {
    val imageLoader = ImageLoader(context)

    for (pin in pins) {
        val imageName = "pin-${pin.id}"
        if (style.getImage(imageName) != null) continue

        try {
            val bitmap = loadCircularBitmap(imageLoader, context, pin.thumbnailUrl, 96, 4)
            if (bitmap != null) {
                withContext(Dispatchers.Main) {
                    if (style.getImage(imageName) == null) {
                        style.addImage(imageName, bitmap)
                    }
                }
            }
        } catch (_: Exception) {
            val fallback = createDefaultMarkerBitmap(pin.name, 96, 4)
            withContext(Dispatchers.Main) {
                if (style.getImage(imageName) == null) {
                    style.addImage(imageName, fallback)
                }
            }
        }
    }
}

private suspend fun loadCircularBitmap(
    imageLoader: ImageLoader,
    context: Context,
    url: String,
    size: Int,
    borderWidth: Int
): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val request = ImageRequest.Builder(context)
            .data(url)
            .size(size)
            .allowHardware(false)
            .build()
        val result = imageLoader.execute(request)
        val sourceBitmap = (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
            ?: return@withContext null
        val circular = createCircularBitmap(sourceBitmap, size, borderWidth)
        if (sourceBitmap != circular) sourceBitmap.recycle()
        circular
    } catch (_: Exception) {
        null
    }
}

private fun createCircularBitmap(source: Bitmap, size: Int, borderWidth: Int): Bitmap {
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    paint.color = Color.WHITE
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

    val innerSize = size - 2 * borderWidth
    val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    val matrix = Matrix()
    val scale = innerSize.toFloat() / minOf(source.width, source.height)
    matrix.setScale(scale, scale)
    matrix.postTranslate(borderWidth.toFloat(), borderWidth.toFloat())
    shader.setLocalMatrix(matrix)
    paint.shader = shader
    canvas.drawCircle(size / 2f, size / 2f, innerSize / 2f, paint)

    return output
}

private fun createDefaultMarkerBitmap(name: String, size: Int, borderWidth: Int): Bitmap {
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    paint.color = Color.parseColor("#212529")
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

    paint.color = Color.WHITE
    paint.textSize = size * 0.4f
    paint.textAlign = Paint.Align.CENTER
    val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val textBounds = android.graphics.Rect()
    paint.getTextBounds(initial, 0, initial.length, textBounds)
    val textY = size / 2f + textBounds.height() / 2f
    canvas.drawText(initial, size / 2f, textY, paint)

    return output
}

private fun fitCameraToPins(map: MapLibreMap, pins: List<PinItem>) {
    if (pins.isEmpty()) return
    if (pins.size == 1) {
        val pin = pins[0]
        map.easeCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(pin.latitude, pin.longitude),
                SINGLE_PIN_ZOOM
            ),
            800
        )
        return
    }
    val boundsBuilder = LatLngBounds.Builder()
    pins.forEach { pin ->
        boundsBuilder.include(LatLng(pin.latitude, pin.longitude))
    }
    val bounds = boundsBuilder.build()
    val padding = 64
    map.getCameraForLatLngBounds(bounds, intArrayOf(padding, padding, padding, padding))?.let {
        map.easeCamera(CameraUpdateFactory.newCameraPosition(it), 800)
    }
}

@SuppressLint("MissingPermission")
private suspend fun recenterToUserLocation(context: Context, mapLibreMap: MapLibreMap?) {
    val map = mapLibreMap ?: return
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    val location = withContext(Dispatchers.IO) {
        try {
            Tasks.await(fusedClient.lastLocation)
        } catch (_: Exception) {
            null
        }
    }
    if (location != null) {
        val target = LatLng(location.latitude, location.longitude)
        withContext(Dispatchers.Main) {
            map.easeCamera(
                CameraUpdateFactory.newLatLngZoom(target, DEFAULT_ZOOM),
                800
            )
        }
    }
}

@Composable
private fun SearchBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(White, RoundedCornerShape(999.dp))
                .shadow(4.dp, RoundedCornerShape(999.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = PlaceholderGray,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Search this area\u2026",
                color = PlaceholderGray,
                fontSize = 15.sp
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(PrimaryDark, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "A",
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            val bgColor = if (isSelected) PrimaryDark else White
            val textColor = if (isSelected) White else PrimaryDark

            Box(
                modifier = Modifier
                    .background(bgColor, RoundedCornerShape(999.dp))
                    .then(
                        if (!isSelected) Modifier.shadow(2.dp, RoundedCornerShape(999.dp))
                        else Modifier
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category,
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun FloatingActionButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .shadow(6.dp, CircleShape)
            .background(White, CircleShape),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = White
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = PrimaryDark,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun BottomSheetSection(
    pins: List<PinItem>,
    selectedPinId: String?,
    onPinClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                White,
                RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
            .shadow(6.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(DragHandleColor, RoundedCornerShape(4.dp))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Nearby Pins",
                    color = PrimaryDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${pins.size} places",
                color = Gray600,
                fontSize = 13.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Box(
                modifier = Modifier
                    .background(SortButtonBg, RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Nearest",
                        color = PrimaryDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = PrimaryDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val sortedPins = remember(pins) { pins.sortedBy { it.distance } }
            sortedPins.forEachIndexed { index, pin ->
                PinRow(
                    pin = pin,
                    isSelected = pin.id == selectedPinId,
                    onClick = { onPinClick(pin.id) }
                )
                if (index < pins.size - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(1.dp)
                            .background(DividerColor)
                    )
                }
            }
        }
    }
}

@Composable
private fun PinRow(
    pin: PinItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val gradientColors = CATEGORY_COLORS[pin.category]
        ?: listOf(ComposeColor(0xFF9E9E9E), ComposeColor(0xFF616161))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.background(ComposeColor(0xFFF0F4FF))
                else Modifier
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(gradientColors)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = pin.name.take(2),
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = pin.name,
                color = PrimaryDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Gray600, CircleShape)
                )
                Text(
                    text = pin.category,
                    color = Gray600,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "\u00B7",
                    color = Gray600,
                    fontSize = 12.sp
                )
                Text(
                    text = "${pin.distance} km",
                    color = Gray600,
                    fontSize = 12.sp
                )
                Text(
                    text = "\u00B7",
                    color = Gray600,
                    fontSize = 12.sp
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = ComposeColor(0xFFFFC107),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = pin.rating.toString(),
                    color = PrimaryDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Gray600,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun TabBar() {
    val bottomInset = WindowInsets.navigationBars

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerColor)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 24.dp, vertical = 10.dp)
                .padding(bottom = with(LocalDensity.current) { bottomInset.getBottom(this).toDp() }),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabItem(Icons.Default.LocationOn, "Map", true)
            TabItem(Icons.Default.Bookmark, "My Pins", false)
            TabItem(Icons.Default.AddCircle, "Add", false)
            TabItem(Icons.Default.Person, "Profile", false)
        }
    }
}

@Composable
private fun TabItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean
) {
    val tint = if (isActive) PrimaryDark else Gray600
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            color = tint,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun MapScreenPreview() {
    MyPinTheme {
        MapScreen()
    }
}

@Preview
@Composable
private fun SearchBarSectionPreview() {
    MyPinTheme { SearchBarSection() }
}

@Preview
@Composable
private fun CategoryChipsPreview() {
    MyPinTheme {
        CategoryChips(
            categories = CATEGORIES,
            selectedCategory = "All",
            onCategorySelected = {}
        )
    }
}

@Preview
@Composable
private fun PinRowPreview() {
    MyPinTheme {
        PinRow(
            pin = PinItem(
                id = "3",
                name = "SFMOMA",
                category = "Art",
                latitude = 37.7857,
                longitude = -122.4011,
                thumbnailUrl = "",
                rating = 4.7,
                distance = 0.4
            ),
            isSelected = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PinRowSelectedPreview() {
    MyPinTheme {
        PinRow(
            pin = PinItem(
                id = "3",
                name = "SFMOMA",
                category = "Art",
                latitude = 37.7857,
                longitude = -122.4011,
                thumbnailUrl = "",
                rating = 4.7,
                distance = 0.4
            ),
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun BottomSheetPreview() {
    MyPinTheme {
        BottomSheetSection(
            pins = listOf(
                PinItem("3", "SFMOMA", "Art", 37.7857, -122.4011, "", 4.7, 0.4),
                PinItem("4", "Ferry Building", "Shopping", 37.7955, -122.3937, "", 4.6, 0.7),
                PinItem("1", "Blue Bottle Coffee", "Coffee", 37.7767, -122.3947, "", 4.8, 1.2)
            ),
            selectedPinId = null,
            onPinClick = {}
        )
    }
}

@Preview
@Composable
private fun TabBarPreview() {
    MyPinTheme { TabBar() }
}
