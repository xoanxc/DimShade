package com.xoanxc.dimshade

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout

// Añadimos OnSharedPreferenceChangeListener para "escuchar" cambios
class DimmingService : AccessibilityService(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        sharedPreferences = getSharedPreferences("DimShadePrefs", Context.MODE_PRIVATE)

        // Leemos el valor inicial guardado por la app
        val initialAlpha = sharedPreferences.getInt("dim_alpha", 128)

        overlayView = FrameLayout(this).apply {
            setBackgroundColor(Color.argb(initialAlpha, 0, 0, 0))
        }

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )

        try {
            windowManager?.addView(overlayView, layoutParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Le decimos al servicio que empiece a escuchar los cambios del Slider
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    // Esta magia se ejecuta sola cada vez que el usuario mueve el Slider
    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences?, key: String?) {
        if (key == "dim_alpha") {
            val newAlpha = sharedPrefs?.getInt("dim_alpha", 128) ?: 128
            // Actualizamos el color del filtro en tiempo real
            overlayView?.setBackgroundColor(Color.argb(newAlpha, 0, 0, 0))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dejamos de escuchar cambios si el servicio se detiene
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        overlayView?.let {
            windowManager?.removeView(it)
            overlayView = null
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}