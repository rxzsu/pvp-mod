package pvp.mod.client.gui

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import pvp.mod.client.config.Config
import pvp.mod.client.modules.Module
import pvp.mod.client.modules.ModuleManager

/**
 * Кастомное меню в стиле Lunar/Feather: блюр фона (свой GLSL gaussian),
 * тёмные панели, шрифт Inter, свои кнопки с hover. Без ванильных виджетов.
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

    // Feather-like палитра
    private val accent = 0xFF00B34C.toInt()
    private val panelBg = 0xF0101114.toInt()
    private val panelBorder = 0xFF26292F.toInt()
    private val rowBg = 0x08FFFFFF.toInt()
    private val rowHoverBg = 0x14FFFFFF.toInt()
    private val textPrimary = 0xFFF2F4F7.toInt()
    private val textSecondary = 0xFF6B7280.toInt()

    override fun extractBackground(extractor: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, tickDelta: Float) {
        if (Config.get().menuBlur && minecraft.level != null) {
            extractor.blurBeforeThisStratum()
            extractor.fill(0, 0, extractor.guiWidth(), extractor.guiHeight(), 0x40000000.toInt())
        } else {
            super.extractBackground(extractor, mouseX, mouseY, tickDelta)
        }
    }

    override fun init() {
        rows = emptyList()
        closeRect = null
        MenuFont.ensure()
    }

    override fun extractRenderState(extractor: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.extractRenderState(extractor, mouseX, mouseY, tickDelta)
        this.mouseX = mouseX
        this.mouseY = mouseY

        MenuFont.ensure()
        val f = MenuFont.medium
        val fb = MenuFont.semibold

        val colWidth = 180
        val rowH = 24
        val pad = 10
        val totalW = colWidth * 2 + 30

        // Заголовок
        extractor.text(fb, "PvP Mod", width / 2 - fb.width("PvP Mod") / 2, 22, textPrimary)
        extractor.text(f, "Click GUI", width / 2 - fb.width("PvP Mod") / 2 + fb.width("PvP Mod") + 6, 24, textSecondary)

        val newRows = mutableListOf<Row>()
        var px = width / 2 - totalW / 2
        var py = 48

        for ((title, col) in listOf("Visual" to Module.Column.VISUAL, "Utility" to Module.Column.UTILITY)) {
            val mods = ModuleManager.column(col)
            val panelH = 32 + mods.size * (rowH + 4) + 8

            extractor.fill(px, py, px + colWidth, py + panelH, panelBg)
            extractor.outline(px, py, px + colWidth, py + panelH, panelBorder)
            extractor.text(fb, title, px + pad, py + 10, textSecondary)

            var ry = py + 32
            for (m in mods) {
                val rect = HitRect(px + pad, ry, colWidth - pad * 2, rowH)
                val hovered = rect.hit(mouseX, mouseY)
                extractor.fill(rect.x, rect.y, rect.x + rect.w, rect.y + rect.h, if (hovered) rowHoverBg else rowBg)

                // Индикатор состояния + имя
                val dot = if (m.enabled) accent else textSecondary
                extractor.text(f, "•", rect.x + 6, rect.y + (rowH - f.lineHeight) / 2, dot)
                extractor.text(f, m.name, rect.x + 16, rect.y + (rowH - f.lineHeight) / 2,
                    if (m.enabled) textPrimary else textSecondary)

                m.keybind?.let { km ->
                    val key = km.getTranslatedKeyMessage().string
                    extractor.text(f, key, rect.x + rect.w - 6 - f.width(key),
                        rect.y + (rowH - f.lineHeight) / 2, textSecondary)
                }

                newRows.add(Row(m, rect))
                ry += rowH + 4
            }

            px += colWidth + 30
        }
        rows = newRows

        // Кнопка Close
        val bw = 110
        val bx = width / 2 - bw / 2
        val by = height - 36
        val close = HitRect(bx, by, bw, 22)
        val closeHover = close.hit(mouseX, mouseY)
        extractor.fill(close.x, close.y, close.x + close.w, close.y + close.h,
            if (closeHover) 0x1800B34C.toInt() else 0x0D00B34C.toInt())
        extractor.outline(close.x, close.y, close.x + close.w, close.y + close.h, accent)
        extractor.text(fb, "Close (Esc)", width / 2 - fb.width("Close (Esc)") / 2,
            close.y + (22 - fb.lineHeight) / 2, if (closeHover) textPrimary else textSecondary)
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
