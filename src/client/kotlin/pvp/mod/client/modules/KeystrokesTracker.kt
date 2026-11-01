package pvp.mod.client.modules

import org.lwjgl.glfw.GLFW
import java.util.ArrayDeque

/**
 * Счётчик кликов мыши за скользящее окно 1 секунду.
 */
object KeystrokesTracker {
    private const val WINDOW_MS = 1000L
    private val leftClicks = ArrayDeque<Long>()
    private val rightClicks = ArrayDeque<Long>()

    fun onMouseButton(button: Int, action: Int) {
        if (action != GLFW.GLFW_PRESS) return
        val now = System.currentTimeMillis()
        when (button) {
            GLFW.GLFW_MOUSE_BUTTON_LEFT -> leftClicks.addLast(now)
            GLFW.GLFW_MOUSE_BUTTON_RIGHT -> rightClicks.addLast(now)
        }
    }

    fun leftCps(): Int = prune(leftClicks)

    fun rightCps(): Int = prune(rightClicks)

    private fun prune(clicks: ArrayDeque<Long>): Int {
        val cutoff = System.currentTimeMillis() - WINDOW_MS
        while (clicks.isNotEmpty() && clicks.first() < cutoff) clicks.removeFirst()
        return clicks.size
    }
}
