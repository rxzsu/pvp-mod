package pvp.mod.client.config

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW
import pvp.mod.client.modules.Module
import pvp.mod.client.modules.ModuleManager

/**
 * Простой экран настроек: тогглы всех модулей + переназначение клавиш. Открывается через ModMenu.
 */
class ConfigScreen(private val parent: Screen?) : Screen(Component.literal("PvP Mod Config")) {

    private var listeningKeyId: String? = null

    override fun init() {
        val centerX = width / 2
        var y = 40

        for (m in ModuleManager.all()) {
            row(centerX, y, m); y += 26
        }

        y += 4
        addRenderableWidget(
            Button.builder(Component.literal("Done"), {
                Config.save()
                minecraft?.gui?.setScreen(parent)
            }).bounds(centerX - 50, y, 100, 20).build()
        )
    }

    private fun row(x: Int, y: Int, m: Module) {
        val km = m.keybind
        lateinit var toggleBtn: Button
        toggleBtn = Button.builder(toggleText(m), {
            m.toggle()
            toggleBtn.setMessage(toggleText(m))
        }).bounds(x - 100, y, 200, 20).build()
        addRenderableWidget(toggleBtn)

        if (km != null) {
            lateinit var keyBtn: Button
            keyBtn = Button.builder(keyText(km), {
                listeningKeyId = km.getName()
                keyBtn.setMessage(Component.literal(">_"))
            }).bounds(x + 110, y, 60, 20).build()
            addRenderableWidget(keyBtn)
        }
    }

    override fun keyPressed(event: KeyEvent): Boolean {
        val lid = listeningKeyId
        if (lid != null) {
            if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
                listeningKeyId = null
                refreshButtons()
                return true
            }
            val km = KeyMapping.get(lid)
            if (km != null) {
                km.setKey(InputConstants.Type.KEYSYM.getOrCreate(event.key()))
                KeyMapping.resetMapping()
            }
            listeningKeyId = null
            refreshButtons()
            return true
        }
        return super.keyPressed(event)
    }

    override fun mouseClicked(event: MouseButtonEvent, bl: Boolean): Boolean {
        val lid = listeningKeyId
        if (lid != null) {
            val km = KeyMapping.get(lid)
            if (km != null) {
                km.setKey(InputConstants.Type.MOUSE.getOrCreate(event.button()))
                KeyMapping.resetMapping()
            }
            listeningKeyId = null
            refreshButtons()
            return true
        }
        return super.mouseClicked(event, bl)
    }

    private fun refreshButtons() {
        clearWidgets()
        init()
    }

    private fun toggleText(m: Module): Component {
        val mark = if (m.enabled) "§a✔" else "§7✘"
        return Component.literal("$mark ${m.name}")
    }

    private fun keyText(km: KeyMapping): Component {
        return Component.literal("§7[${km.getTranslatedKeyMessage().string}]")
    }

    override fun extractRenderState(extractor: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.extractRenderState(extractor, mouseX, mouseY, tickDelta)
        extractor.centeredText(font, title, width / 2, 15, 0xFFFFFFFF.toInt())
    }
}
