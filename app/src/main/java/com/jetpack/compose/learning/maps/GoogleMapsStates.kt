package com.jetpack.compose.learning.maps

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.Cap
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.maps.model.SquareCap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.jetpack.compose.learning.R

enum class MapStyle {
    NORMAL, DARK, NIGHT
}

data class BasicMapUIState(
    val mapsProperties: MapProperties = MapProperties(),
    val mapUiSettings: MapUiSettings = MapUiSettings(),
    val mapStyle: MapStyle = MapStyle.NORMAL
)

enum class MarkerIconStyle { DEFAULT, IMAGE, VECTOR }

enum class MarkerStyle {
    DEFAULT, CUSTOM_INFO_WINDOW_CONTENT, CUSTOM_INFO_WINDOW;

    override fun toString(): String {
        return when (this) {
            DEFAULT -> "Default"
            CUSTOM_INFO_WINDOW_CONTENT -> "Custom Info Window Content"
            CUSTOM_INFO_WINDOW -> "Custom Info Window"
        }
    }
}

data class MarkerMapUIState(
    val markerStyle: MarkerStyle = MarkerStyle.DEFAULT,
    val markerIconStyle: MarkerIconStyle = MarkerIconStyle.DEFAULT,
    val markerHue: Float = 0f,
    val alpha: Float = 1f,
    val rotation: Float = 0f,
    val draggable: Boolean = false,
    val flat: Boolean = false,
    val visible: Boolean = true,
) {
    fun getMarkerIcon(context: Context): BitmapDescriptor {
        return when (markerIconStyle) {
            MarkerIconStyle.DEFAULT -> BitmapDescriptorFactory.defaultMarker(markerHue)
            MarkerIconStyle.IMAGE -> BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_128px)
            MarkerIconStyle.VECTOR -> convertVectorToBitmap(
                context,
                R.drawable.ic_vector_pin,
                Color.hsv(markerHue, 1f, 1f)
            )
        }
    }
}

enum class PolylineCap { BUTT, ROUND, SQUARE, CUSTOM }

enum class StrokeJointType(val type: Int) {
    BEVEL(JointType.BEVEL),
    DEFAULT(JointType.DEFAULT),
    ROUND(JointType.ROUND),
}

enum class StrokePatternType { SOLID, DASH, DOT, GAP, MIX }

abstract class CommonShapeUIState {
    abstract val strokeColor: Color
    abstract val strokeColorAlpha: Float
    abstract val clickable: Boolean
    abstract val jointType: StrokeJointType
    abstract val patternType: StrokePatternType
    abstract val visible: Boolean
    abstract val strokeWidth: Float

    fun getPatternType(): List<PatternItem>? {
        return when (patternType) {
            StrokePatternType.SOLID -> null
            StrokePatternType.DASH -> listOf(Dash(20f), Gap(20f))
            StrokePatternType.DOT -> listOf(Dot())
            StrokePatternType.GAP -> listOf(Dot(), Gap(50f), Dot())
            StrokePatternType.MIX -> listOf(Dot(), Gap(20f), Dash(20f), Gap(50f))
        }
    }
}

data class PolylineMapUIState(
    val isMarkerDraggable: Boolean = false,
    val isMarkerVisible: Boolean = true,
    val geodesic: Boolean = false,
    val polylineStartCap: PolylineCap = PolylineCap.BUTT,
    val polylineEndCap: PolylineCap = PolylineCap.BUTT,
    override val strokeColor: Color = Color.Black,
    override val clickable: Boolean = false,
    override val jointType: StrokeJointType = StrokeJointType.DEFAULT,
    override val patternType: StrokePatternType = StrokePatternType.SOLID,
    override val visible: Boolean = true,
    override val strokeWidth: Float = 10f,
    override val strokeColorAlpha: Float = 1f,
) : CommonShapeUIState() {

    fun getPolylineStartCap(): Cap {
        return when (polylineStartCap) {
            PolylineCap.BUTT -> ButtCap()
            PolylineCap.ROUND -> RoundCap()
            PolylineCap.SQUARE -> SquareCap()
            PolylineCap.CUSTOM -> CustomCap(BitmapDescriptorFactory.defaultMarker())
        }
    }

    fun getPolylineEndCap(): Cap {
        return when (polylineEndCap) {
            PolylineCap.BUTT -> ButtCap()
            PolylineCap.ROUND -> RoundCap()
            PolylineCap.SQUARE -> SquareCap()
            PolylineCap.CUSTOM -> CustomCap(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                strokeWidth
            )
        }
    }
}

data class PolygonMapUIState(
    val geodesic: Boolean = false,
    val fillColor: Color = Color.Red,
    val fillColorAlpha: Float = 0.2f,
    override val strokeColor: Color = Color.Black,
    override val clickable: Boolean = false,
    override val jointType: StrokeJointType = StrokeJointType.DEFAULT,
    override val patternType: StrokePatternType = StrokePatternType.SOLID,
    override val visible: Boolean = true,
    override val strokeWidth: Float = 10f,
    override val strokeColorAlpha: Float = 1f,
) : CommonShapeUIState()

data class CircleMapUIState(
    val isMarkerDraggable: Boolean = false,
    val isMarkerVisible: Boolean = true,
    val radius: Float = 50f,
    val fillColor: Color = Color.Red,
    val fillColorAlpha: Float = 0.2f,
    override val strokeColor: Color = Color.Black,
    override val clickable: Boolean = false,
    override val jointType: StrokeJointType = StrokeJointType.DEFAULT,
    override val patternType: StrokePatternType = StrokePatternType.SOLID,
    override val visible: Boolean = true,
    override val strokeWidth: Float = 10f,
    override val strokeColorAlpha: Float = 1f,
) : CommonShapeUIState()