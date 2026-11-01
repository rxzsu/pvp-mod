package pvp.mod.client.handler

object ZoomHandler {
    var isZooming = false
    private var currentMultiplier = 1.0
    private var targetMultiplier = 1.0
    var multiplier = 0.2
    private const val SMOOTH_SPEED = 0.3

    fun tick() {
        targetMultiplier = if (isZooming) multiplier else 1.0
        currentMultiplier += (targetMultiplier - currentMultiplier) * SMOOTH_SPEED
        if (kotlin.math.abs(currentMultiplier - targetMultiplier) < 0.001) {
            currentMultiplier = targetMultiplier
        }
    }

    fun getZoomMultiplier(): Double = currentMultiplier
}
