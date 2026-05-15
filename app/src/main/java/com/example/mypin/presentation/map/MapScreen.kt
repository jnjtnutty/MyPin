// Figma: https://www.figma.com/design/RbxZunWIJGyF1YrWcgE54q/MyPin-Mobile-Login-Design?node-id=15-46 node-id=15:46
package com.example.mypin.presentation.map

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mypin.domain.model.PinEntity
import com.example.mypin.ui.theme.MyPinTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.json.JSONArray
import org.json.JSONObject
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource

private val DarkText = ComposeColor(0xFF212529)
private val GrayText = ComposeColor(0xFF6B7280)
private val PlaceholderText = ComposeColor(0xFF808799)
private val DividerColor = ComposeColor(0xFFE6E8EB)
private val DragHandleColor = ComposeColor(0xFFCCD4E0)
private val SortBgColor = ComposeColor(0xFFF5F6F7)
private val White = ComposeColor.White
private val ChipSelectedBg = ComposeColor(0xFF212529)
private val AvatarBg = ComposeColor(0xFF212529)
private val ChipUnselectedBg = ComposeColor.White

private data class GradientColors(val start: Long, val end: Long)

private val PinGradients = listOf(
    GradientColors(0xFFC775EB, 0xFF5C2980),
    GradientColors(0xFFF2A84D, 0xFFA65C1A),
    GradientColors(0xFFD98C4D, 0xFF734729),
    GradientColors(0xFF7EC8A0, 0xFF2D6B4F),
    GradientColors(0xFF6BB3E0, 0xFF2A5F8A),
    GradientColors(0xFFE07575, 0xFF8A2A2A),
    GradientColors(0xFF8BC7E8, 0xFF3A6E8F),
    GradientColors(0xFFC9B97A, 0xFF7A6B3A)
)

private val ThumbnailGradients = PinGradients.map {
    Brush.horizontalGradient(listOf(ComposeColor(it.start), ComposeColor(it.end)))
}

private val Categories = listOf("All", "Coffee", "Food", "Nature", "Art", "Nightlife", "Shopping", "Stay")

private const val STYLE_URL = "https://demotiles.maplibre.org/style.json"
private const val DEFAULT_LAT = 37.7749
private const val DEFAULT_LNG = -122.4194
private const val DEFAULT_ZOOM = 13.0
private const val PIN_ICON_SIZE = 48

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel,
    onAddPinClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var hasFittedInitialCamera by remember { mutableStateOf(false) }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val pins = (uiState as? MapUiState.Success)?.filteredPins.orEmpty()

    Box(modifier = modifier.fillMaxSize()) {
        MapViewContainer(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { map, mv ->
                mapLibreMap = map
                mapView = mv
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            SearchBarSection(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            CategoryChipsRow(
                selectedCategory = (uiState as? MapUiState.Success)?.selectedCategory,
                onCategorySelected = { viewModel.filterByCategory(it) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            FloatingActionButtonsColumn(
                onMyLocationClick = {
                    if (locationPermissionState.status.isGranted) {
                        animateToUserLocation(mapLibreMap, context)
                    } else {
                        locationPermissionState.launchPermissionRequest()
                    }
                },
                onAddPinClick = onAddPinClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 16.dp)
            )

            PinBottomSheet(
                pins = pins,
                selectedPinId = (uiState as? MapUiState.Success)?.selectedPinId,
                onPinClick = { pin ->
                    viewModel.selectPin(pin.id)
                    animateToPin(mapLibreMap, pin)
                }
            )

            TabBar(
                activeTab = "Map",
                onTabClick = { }
            )
        }
    }

    LaunchedEffect(pins) {
        mapLibreMap?.let { map ->
            updateMapPins(map, context, pins)
            if (!hasFittedInitialCamera) {
                fitCameraToPins(map, pins)
                hasFittedInitialCamera = true
            }
        }
    }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            enableLocationComponent(mapLibreMap, context)
        }
    }

    MapViewLifecycle(mapView = mapView)
}

@Composable
private fun MapViewLifecycle(mapView: MapView?) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView?.onStart()
                Lifecycle.Event.ON_RESUME -> mapView?.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView?.onPause()
                Lifecycle.Event.ON_STOP -> mapView?.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView?.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView?.onDestroy()
        }
    }
}

@Composable
private fun MapViewContainer(
    modifier: Modifier = Modifier,
    onMapReady: (MapLibreMap, MapView) -> Unit
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                onCreate(null)
                getMapAsync { map ->
                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(DEFAULT_LAT, DEFAULT_LNG))
                        .zoom(DEFAULT_ZOOM)
                        .build()

                    map.uiSettings.isRotateGesturesEnabled = true
                    map.uiSettings.isZoomGesturesEnabled = true
                    map.uiSettings.isScrollGesturesEnabled = true
                    map.uiSettings.isDoubleTapGesturesEnabled = true

                    map.setStyle(STYLE_URL) { style ->
                        onMapReady(map, this@apply)
                    }
                }
            }
        }
    )
}

private fun updateMapPins(
    map: MapLibreMap,
    context: Context,
    pins: List<PinEntity>
) {
    map.getStyle { style ->
        val existingSource = style.getSource("pins-source") as? GeoJsonSource
        val featureArray = JSONArray()
        for (pin in pins) {
            val feature = JSONObject().apply {
                put("type", "Feature")
                put("geometry", JSONObject().apply {
                    put("type", "Point")
                    put("coordinates", JSONArray().put(pin.longitude).put(pin.latitude))
                })
                put("properties", JSONObject().apply {
                    put("id", pin.id)
                    put("name", pin.name)
                    put("category", pin.category)
                    put("icon", "pin-icon-${pin.id}")
                })
            }
            featureArray.put(feature)
        }
        val geoJson = JSONObject().apply {
            put("type", "FeatureCollection")
            put("features", featureArray)
        }.toString()

        val newIconIds = pins.map { "pin-icon-${it.id}" }.toSet()

        if (existingSource != null) {
            existingSource.setGeoJson(geoJson)
            val currentIcons = mutableSetOf<String>()
            for (pin in pins) {
                val iconId = "pin-icon-${pin.id}"
                if (style.getImage(iconId) == null) {
                    val bitmap = createCircularPinBitmap(
                        context = context,
                        gradientIndex = pins.indexOf(pin) % PinGradients.size,
                        initial = pin.name.firstOrNull()?.toString() ?: ""
                    )
                    style.addImage(iconId, bitmap)
                }
                currentIcons.add(iconId)
            }
        } else {
            val source = GeoJsonSource("pins-source", geoJson)
            style.addSource(source)

            for (pin in pins) {
                val bitmap = createCircularPinBitmap(
                    context = context,
                    gradientIndex = pins.indexOf(pin) % PinGradients.size,
                    initial = pin.name.firstOrNull()?.toString() ?: ""
                )
                style.addImage("pin-icon-${pin.id}", bitmap)
            }

            val symbolLayer = SymbolLayer("pins-layer", "pins-source")
            symbolLayer.setProperties(
                PropertyFactory.iconImage(Expression.get("icon")),
                PropertyFactory.iconSize(1f),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true)
            )
            style.addLayer(symbolLayer)
        }
    }
}

private fun createCircularPinBitmap(
    context: Context,
    gradientIndex: Int,
    initial: String
): Bitmap {
    val density = context.resources.displayMetrics.density
    val sizePx = (PIN_ICON_SIZE * density).toInt()
    val borderWidthPx = (2 * density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawOval(
        RectF(0f, 0f, sizePx.toFloat(), sizePx.toFloat()),
        borderPaint
    )

    val gradient = PinGradients[gradientIndex]
    val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        val r = ((gradient.start shr 16) and 0xFF).toInt()
        val g = ((gradient.start shr 8) and 0xFF).toInt()
        val b = (gradient.start and 0xFF).toInt()
        color = Color.rgb(r, g, b)
        style = Paint.Style.FILL
    }
    val inset = borderWidthPx.toFloat()
    canvas.drawOval(
        RectF(inset, inset, sizePx - inset, sizePx - inset),
        innerPaint
    )

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = sizePx * 0.35f
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.create(
            android.graphics.Typeface.DEFAULT,
            android.graphics.Typeface.BOLD
        )
    }
    val textBounds = android.graphics.Rect()
    textPaint.getTextBounds(initial, 0, initial.length, textBounds)
    canvas.drawText(
        initial,
        sizePx / 2f,
        sizePx / 2f + textBounds.height() / 2f,
        textPaint
    )

    return bitmap
}

private fun fitCameraToPins(map: MapLibreMap?, pins: List<PinEntity>) {
    if (map == null || pins.isEmpty()) return

    if (pins.size == 1) {
        val pin = pins[0]
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(pin.latitude, pin.longitude), 15.0
            ),
            800
        )
        return
    }

    val boundsBuilder = LatLngBounds.Builder()
    for (pin in pins) {
        boundsBuilder.include(LatLng(pin.latitude, pin.longitude))
    }
    val bounds = boundsBuilder.build()
    map.animateCamera(
        CameraUpdateFactory.newLatLngBounds(bounds, 64),
        800
    )
}

private fun animateToPin(map: MapLibreMap?, pin: PinEntity) {
    map?.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            LatLng(pin.latitude, pin.longitude), 15.0
        ),
        800
    )
}

@Suppress("MissingPermission")
private fun animateToUserLocation(map: MapLibreMap?, context: Context) {
    if (map == null) return
    if (PermissionChecker.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PermissionChecker.PERMISSION_GRANTED
    ) return

    val lastKnown = map.locationComponent.lastKnownLocation
    val target = if (lastKnown != null) {
        LatLng(lastKnown.latitude, lastKnown.longitude)
    } else {
        LatLng(DEFAULT_LAT, DEFAULT_LNG)
    }
    map.animateCamera(
        CameraUpdateFactory.newLatLngZoom(target, DEFAULT_ZOOM),
        800
    )
}

@Suppress("MissingPermission")
private fun enableLocationComponent(map: MapLibreMap?, context: Context) {
    if (map == null) return
    if (PermissionChecker.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PermissionChecker.PERMISSION_GRANTED
    ) return

    map.getStyle { style ->
        val activationOptions = LocationComponentActivationOptions.builder(context, style)
            .build()
        map.locationComponent.apply {
            activateLocationComponent(activationOptions)
            isLocationComponentEnabled = true
            renderMode = RenderMode.COMPASS
        }
    }
}

@Composable
private fun SearchBarSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(999.dp),
            color = White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = PlaceholderText
                )
                Text(
                    text = "Search this area\u2026",
                    color = PlaceholderText,
                    fontSize = 15.sp
                )
            }
        }

        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = AvatarBg
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "A",
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CategoryChipsRow(
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(Categories) { category ->
            val isSelected = (category == "All" && selectedCategory == null) ||
                    category == selectedCategory
            CategoryChip(
                label = category,
                isSelected = isSelected,
                onClick = {
                    onCategorySelected(if (category == "All") null else category)
                }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (isSelected) ChipSelectedBg else ChipUnselectedBg,
        shadowElevation = if (isSelected) 0.dp else 2.dp
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = if (isSelected) White else DarkText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FloatingActionButtonsColumn(
    onMyLocationClick: () -> Unit,
    onAddPinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            modifier = Modifier
                .size(56.dp)
                .clickable(onClick = onMyLocationClick),
            shape = CircleShape,
            color = White,
            shadowElevation = 6.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Current Location",
                    modifier = Modifier.size(22.dp),
                    tint = ComposeColor(0xFF1E1E1E)
                )
            }
        }

        Surface(
            modifier = Modifier
                .size(56.dp)
                .clickable(onClick = onAddPinClick),
            shape = RoundedCornerShape(28.dp),
            color = DarkText,
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Pin",
                    modifier = Modifier.size(28.dp),
                    tint = White
                )
            }
        }
    }
}

@Composable
private fun PinBottomSheet(
    pins: List<PinEntity>,
    selectedPinId: String?,
    onPinClick: (PinEntity) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = White,
        shadowElevation = 24.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp),
                    shape = RoundedCornerShape(2.dp),
                    color = DragHandleColor
                ) {}
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nearby Pins",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${pins.size} places",
                    fontSize = 13.sp,
                    color = GrayText
                )
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = SortBgColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Nearest",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkText
                        )
                        Text(
                            text = "\u22EF",
                            fontSize = 14.sp,
                            color = DarkText
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                pins.forEachIndexed { index, pin ->
                    PinRow(
                        pin = pin,
                        isSelected = pin.id == selectedPinId,
                        gradientIndex = index % ThumbnailGradients.size,
                        onClick = { onPinClick(pin) }
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
}

@Composable
private fun PinRow(
    pin: PinEntity,
    isSelected: Boolean,
    gradientIndex: Int,
    onClick: () -> Unit
) {
    val selectedScale by animateDpAsState(
        targetValue = if (isSelected) 60.dp else 56.dp,
        label = "thumbnail_scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(selectedScale)
                .clip(RoundedCornerShape(12.dp))
                .background(ThumbnailGradients[gradientIndex]),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = pin.name.firstOrNull()?.toString() ?: "",
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = pin.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(GrayText)
                )
                Text(
                    text = pin.category,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = GrayText
                )
                Text(text = "\u00B7", fontSize = 12.sp, color = GrayText)
                Text(
                    text = "${pin.distanceKm} km",
                    fontSize = 12.sp,
                    color = GrayText
                )
                Text(text = "\u00B7", fontSize = 12.sp, color = GrayText)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = ComposeColor(0xFFFFC107)
                    )
                    Text(
                        text = String.format("%.1f", pin.rating),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = GrayText
        )
    }
}

@Composable
private fun TabBar(
    activeTab: String,
    onTabClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 24.dp, vertical = 10.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabItem("Map", Icons.Default.LocationOn, activeTab == "Map", onTabClick)
            TabItem("My Pins", Icons.Default.BookmarkBorder, activeTab == "My Pins", onTabClick)
            TabItem("Add", Icons.Default.Add, activeTab == "Add", onTabClick)
            TabItem("Profile", Icons.Default.PersonOutline, activeTab == "Profile", onTabClick)
        }
    }
}

@Composable
private fun TabItem(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    onTabClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onTabClick(label) }
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(22.dp),
            tint = if (isActive) DarkText else GrayText
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isActive) DarkText else GrayText
        )
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun MapScreenPreview() {
    MyPinTheme {
        MapScreenPreviewContent()
    }
}

@Composable
private fun MapScreenPreviewContent() {
    val demoPins = listOf(
        PinEntity("1", "SFMOMA", "Art", 37.7857, -122.4011, "", 4.7, 0.4),
        PinEntity("2", "Ferry Building", "Shopping", 37.7955, -122.3937, "", 4.6, 0.7),
        PinEntity("3", "Blue Bottle Coffee", "Coffee", 37.7849, -122.4094, "", 4.8, 1.2)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            SearchBarSection(modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            CategoryChipsRow(
                selectedCategory = null,
                onCategorySelected = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButtonsColumn(
                onMyLocationClick = {},
                onAddPinClick = {},
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 16.dp)
            )
            PinBottomSheet(
                pins = demoPins,
                selectedPinId = null,
                onPinClick = {}
            )
            TabBar(activeTab = "Map", onTabClick = {})
        }
    }
}

@Preview
@Composable
private fun SearchBarSectionPreview() {
    MyPinTheme { SearchBarSection(modifier = Modifier.padding(16.dp)) }
}

@Preview
@Composable
private fun CategoryChipsRowPreview() {
    MyPinTheme {
        CategoryChipsRow(
            selectedCategory = null,
            onCategorySelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun PinRowPreview() {
    MyPinTheme {
        PinRow(
            pin = PinEntity("1", "SFMOMA", "Art", 37.7857, -122.4011, "", 4.7, 0.4),
            isSelected = false,
            gradientIndex = 0,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PinRowSelectedPreview() {
    MyPinTheme {
        PinRow(
            pin = PinEntity("1", "SFMOMA", "Art", 37.7857, -122.4011, "", 4.7, 0.4),
            isSelected = true,
            gradientIndex = 0,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun BottomSheetPreview() {
    MyPinTheme {
        val pins = listOf(
            PinEntity("1", "SFMOMA", "Art", 37.7857, -122.4011, "", 4.7, 0.4),
            PinEntity("2", "Ferry Building", "Shopping", 37.7955, -122.3937, "", 4.6, 0.7),
            PinEntity("3", "Blue Bottle Coffee", "Coffee", 37.7849, -122.4094, "", 4.8, 1.2)
        )
        PinBottomSheet(pins = pins, selectedPinId = null, onPinClick = {})
    }
}

@Preview
@Composable
private fun TabBarPreview() {
    MyPinTheme { TabBar(activeTab = "Map", onTabClick = {}) }
}

@Preview
@Composable
private fun FloatingActionButtonsPreview() {
    MyPinTheme {
        FloatingActionButtonsColumn(
            onMyLocationClick = {},
            onAddPinClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun CategoryChipSelectedPreview() {
    MyPinTheme { CategoryChip(label = "All", isSelected = true, onClick = {}) }
}

@Preview
@Composable
private fun CategoryChipUnselectedPreview() {
    MyPinTheme { CategoryChip(label = "Coffee", isSelected = false, onClick = {}) }
}
