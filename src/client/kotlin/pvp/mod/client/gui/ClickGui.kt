package pvp.mod.client.gui

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import pvp.mod.client.config.Config
import pvp.mod.client.modules.Module
import pvp.mod.client.modules.ModuleManager

/**
 * Кастомное меню в стиле Lunar/Feather: блюр фона (в т.ч. свой GLSL gaussian),
 * полупрозрачные панели, свои кнопки с hover-подсветкой. Никаких ванильных виджетов.
 */
class ClickGui(parent: Screen?) : Screen(Component.literal("PvP Mod")) {
    private val parent: Screen? = parent

    private class HitRect(val x: Int, val y: Int, val w: Int, val h: Int) {
        fun hit(mx: Int, my: Int) = mx >= x && mx < x + w && my >= y && my < y + h
    }

    private class Row(val module: Module, val rect: HitRect)

    private var rows: List<Row> = emptyList()
    private var closeRect: HitRect? = null
    private var mouseX = 0
    private var mouseY = 0

    private val accent = 0xFF00B34C.toInt()
    private val panelBg = 0xA0000000.toInt()
    private val rowBg = 0x40000000.toInt()
    private val rowHoverBg = 0x60FFFFFF.toInt()

    override fun extractBackground(extractor: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, tickDelta: Float) {
        if (Config.get().menuBlur && minecraft.level != null) {
            // Блюр мира позади; если fancyBlur - GameRendererMixin подменит chain на наш gaussian
            extractor.blurBeforeThisStratum()
            extractor.fill(0, 0, extractor.guiWidth(), extractor.guiHeight(), 0x40000000.toInt())
        } else {
            super.extractBackground(extractor, mouseX, mouseY, tickDelta)
        }
    }

    override fun init() {
        rows = emptyList()
        closeRect = null
    }

    override fun extractRenderState(extractor: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.extractRenderState(extractor, mouseX, mouseY, tickDelta)
        this.mouseX = mouseX
        this.mouseY = mouseY

        val font = font
        val colWidth = 170
        val rowH = 22
        val pad = 8
        val totalW = colWidth * 2 + 30

        extractor.centeredText(font, "§fPvP Mod §7Click GUI", width / 2, 24, 0xFFFFFF)

        val newRows = mutableListOf<Row>()
        var px = width / 2 - totalW / 2
        var py = 46

        for ((title, col) in listOf("Visual" to Module.Column.VISUAL, "Utility" to Module.Column.UTILITY)) {
            val mods = ModuleManager.column(col)
            val panelH = 30 + mods.size * (rowH + 4) + 6

            extractor.fill(px, py, px + colWidth, py + panelH, panelBg)
            extractor.outline(px, py, px + colWidth, py + panelH, 0x30FFFFFF.toInt())
            extractor.text(font, title, px + pad, py + 8, accent)

            var ry = py + 28
            for (m in mods) {
                val rect = HitRect(px + pad, ry, colWidth - pad * 2, rowH)
                val hovered = rect.hit(mouseX, mouseY)
                extractor.fill(rect.x, rect.y, rect.x + rect.w, rect.y + rect.h, if (hovered) rowHoverBg else rowBg)

                extractor.text(font, "${if (m.enabled) "§a" else "§7"}${m.name}",
                    rect.x + 5, rect.y + (rowH - font.lineHeight) / 2, 0xFFFFFF)
                m.keybind?.let { km ->
                    val key = km.getTranslatedKeyMessage().string
                    extractor.text(font, key, rect.x + rect.w - 5 - font.width(key),
                        rect.y + (rowH - font.lineHeight) / 2, 0x909090)
                }

                newRows.add(Row(m, rect))
                ry += rowH + 4
            }

            px += colWidth + 30
        }
        rows = newRows

        val bw = 100
        val bx = width / 2 - bw / 2
        val by = height - 34
        val close = HitRect(bx, by, bw, 20)
        extractor.fill(close.x, close.y, close.x + close.w, close.y + close.h,
            if (close.hit(mouseX, mouseY)) 0x6000B34C.toInt() else 0x5000B34C.toInt())
        extractor.outline(close.x, close.y, close.x + close.w, close.y + close.h, accent)
        extractor.centeredText(font, "Close (Esc)", width / 2, close.y + (20 - font.lineHeight) / 2, 0xFFFFFF)
        closeRect = close
    }

    override fun mouseClicked(event: MouseButtonEvent, bl: Boolean): Boolean {
        val mx = event.x().toInt()
        val my = event.y().toInt()
        for (row in rows) {
            if (row.rect.hit(mx, my)) {
                row.module.toggle()
                return true
            }
        }
        closeRect?.let { if (it.hit(mx, my)) { onClose(); return true } }
        return super.mouseClicked(event, bl)
    }

    override fun onClose() {
        minecraft.gui.setScreen(parent)
    }
}
