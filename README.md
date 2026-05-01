# DimShade
App para Android que permite bajar el brillo de la pantalla por debajo del límite mínimo del sistema operativo. Ideal para usar el móvil a oscuras sin dejarte los ojos.

## Qué hace exactamente
Cubre el 100% de la pantalla: Usa la API de Accesibilidad para poner el filtro por encima de todo, incluyendo la barra de estado, el notch y el panel de notificaciones al desplegarlo.

Control de intensidad: Un slider básico para ajustar el nivel de oscuridad en tiempo real.

Límite anti-bloqueo: Topado al 95% de opacidad. Si llegara al 100%, la pantalla quedaría negra y el móvil inservible, así que este límite te salva de tener que reiniciar a ciegas.

## Stack técnico
Lenguaje: Kotlin

UI: Jetpack Compose

Core: AccessibilityService (para el overlay total) y SharedPreferences (para guardar la configuración).

Mínimo: Android 11+ (API 30).
