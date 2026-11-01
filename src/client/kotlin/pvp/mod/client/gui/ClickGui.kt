package pvp.mod.client.gui

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import pvp.mod.client.modules.Module
import pvp.mod.client.modules.ModuleManager

/**
 * Click GUI: тогглы всех модулей мышкой, подпись показывает состояние и клавишу.
 */
class ClickGui(parent: Screen?) : Screen(Component.literal("PvP Mod")) {
    private val parent: Screen? = parent

    override fun init() {
        val columns = listOf(Module.Column.VISUAL, Module.Column.UTILITY)
        val columnTitles = mapOf(Module.Column.VISUAL to "Visual", Module.Column.UTILITY to "Utility")
        val colWidth = 130
        val totalW = colWidth * 2 + 40
        var startX = width / 2 - totalW / 2
        var y = 40

        for (col in columns) {
            var cy = y
            addRenderableWidget(
                Button.builder(Component.literal("§n${columnTitles[col]}"), { }).bounds(startX, cy, colWidth, 18).build()
            ).active = false
            cy += 24

            for (m in ModuleManager.column(col)) {
                lateinit var btn: Button
                btn = Button.builder(label(m), {
                    m.toggle()
                    btn.setMessage(label(m))
                }).bounds(startX, cy, colWidth, 20).build()
                addRenderableWidget(btn)
                cy += 24
            }
            startX += colWidth + 40
        }

        addRenderableWidget(
            Button.builder(Component.literal("Done"), { onClose() })
                .bounds(width / 2 - 50, height - 30, 100, 20).build()
        )
    }

    private fun label(m: Module): Component {
        val mark = if (m.enabled) "§a✔" else "§7✘"
        val key = m.keybind?.let { km -> " §7[${km.getTranslatedKeyMessage().string}]" } ?: ""
        return Component.literal("$mark ${m.name}$key")
    }

    override fun extractRenderState(extractor: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.extractRenderState(extractor, mouseX, mouseY, tickDelta)
        extractor.centeredText(font, title, width / 2, 15, 0xFFFFFF)
    }

    override fun onClose() {
        parent?.let { minecraft.setScreenAndShow(it) }
    }
}
