package pvp.mod.client.modules

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * Панель Keystrokes (WASD / пробел / CPS) в правом верхнем углу.
 */
object KeystrokesRenderer {
    private const val CELL = 22
    private const val GAP = 2
    private val BG = 0x90000000.toInt()
    private val BG_PRESSED = 0x9000B34C.toInt()
    private val FG = 0xFFFFFF
    private val FG_PRESSED = 0x7CFC00

    fun render(extractor: GuiGraphicsExtractor) {
        val mc = Minecraft.getInstance()
        val font = mc.font

        val panelW = CELL * 3 + GAP * 2
        val cfg = pvp.mod.client.config.Config.get()
        val x0 = extractor.guiWidth() - cfg.keystrokesOffsetX - panelW
        val y0 = cfg.keystrokesOffsetY

        val up = mc.options.keyUp.isDown
        val down = mc.options.keyDown.isDown
        val left = mc.options.keyLeft.isDown
        val right = mc.options.keyRight.isDown
        val jump = mc.options.keyJump.isDown

        // W
        cell(extractor, font, x0 + CELL + GAP, y0, CELL, "W", up)
        // A S D
        cell(extractor, font, x0, y0 + CELL + GAP, CELL, "A", left)
        cell(extractor, font, x0 + CELL + GAP, y0 + CELL + GAP, CELL, "S", down)
        cell(extractor, font, x0 + (CELL + GAP) * 2, y0 + CELL + GAP, CELL, "D", right)
        // Пробел
        spacebar(extractor, font, x0, y0 + (CELL + GAP) * 2, panelW, jump)
        // CPS
        val cy = y0 + (CELL + GAP) * 3
        cell(extractor, font, x0, cy, CELL + 10, "L ${KeystrokesTracker.leftCps()}", false)
        cell(extractor, font, x0 + CELL + 10 + GAP, cy, CELL + 12, "R ${KeystrokesTracker.rightCps()}", false)
    }

    private fun cell(extractor: GuiGraphicsExtractor, font: net.minecraft.client.gui.Font,
                     x: Int, y: Int, w: Int, label: String, pressed: Boolean) {
        extractor.fill(x, y, x + w, y + CELL, if (pressed) BG_PRESSED else BG)
        val tx = x + (w - font.width(label)) / 2
        val ty = y + (CELL - font.lineHeight) / 2
        extractor.text(font, label, tx, ty, if (pressed) FG_PRESSED else FG, true)
    }

    private fun spacebar(extractor: GuiGraphicsExtractor, font: net.minecraft.client.gui.Font,
                         x: Int, y: Int, w: Int, pressed: Boolean) {
        extractor.fill(x, y, x + w, y + CELL, if (pressed) BG_PRESSED else BG)
        val label = "___"
        val tx = x + (w - font.width(label)) / 2
        val ty = y + (CELL - font.lineHeight) / 2
        extractor.text(font, label, tx, ty, if (pressed) FG_PRESSED else FG, true)
    }
}
