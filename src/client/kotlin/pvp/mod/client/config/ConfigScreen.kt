package pvp.mod.client.config

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW
import pvp.mod.client.handler.FullbrightHandler
import pvp.mod.client.handler.ToggleSprintHandler

class ConfigScreen(private val parent: Screen?) : Screen(Component.literal("PvP Mod Config")) {

    private var listeningKeyId: String? = null

    override fun init() {
        val centerX = width / 2
        var y = 40

        row(centerX, y, "Fullbright", "key.pvpmod.fullbright",
            { Config.get().fullbright }, { Config.get().fullbright = it },
            { FullbrightHandler.loadFromConfig(it) }); y += 26
        row(centerX, y, "Sprint", "key.pvpmod.toggle_sprint",
            { Config.get().toggleSprint }, { Config.get().toggleSprint = it },
            { ToggleSprintHandler.loadFromConfig(it) }); y += 26
        row(centerX, y, "No Bobbing", "key.pvpmod.no_view_bobbing",
            { Config.get().noViewBobbing }, { Config.get().noViewBobbing = it }); y += 26
        row(centerX, y, "No Hurt", "key.pvpmod.no_hurt_tilt",
            { Config.get().noHurtTilt }, { Config.get().noHurtTilt = it }); y += 26
        row(centerX, y, "No Nausea", "key.pvpmod.no_nausea",
            { Config.get().noNausea }, { Config.get().noNausea = it }); y += 26
        row(centerX, y, "No FOV", "key.pvpmod.no_fov_change",
            { Config.get().noFovChange }, { Config.get().noFovChange = it }); y += 26
        row(centerX, y, "No Fire", "key.pvpmod.no_fire_overlay",
            { Config.get().noFireOverlay }, { Config.get().noFireOverlay = it }); y += 26
        row(centerX, y, "No Weather", "key.pvpmod.no_weather",
            { Config.get().noWeather }, { Config.get().noWeather = it }); y += 26
        row(centerX, y, "HUD", "key.pvpmod.hud",
            { Config.get().hudEnabled }, { Config.get().hudEnabled = it }); y += 26

        y += 4
        keyRow(centerX, y, "Zoom", "key.pvpmod.zoom"); y += 20
        keyRow(centerX, y, "Brightness+", "key.pvpmod.brightness_up"); y += 20
        keyRow(centerX, y, "Brightness−", "key.pvpmod.brightness_down"); y += 20

        y += 6
        addRenderableWidget(
            Button.builder(Component.literal("Done"), {
                saveOptions()
                Config.save()
                minecraft?.screen = parent
            }).bounds(centerX - 50, y, 100, 20).build()
        )
    }

    private fun row(x: Int, y: Int, name: String, keyId: String,
                    get: () -> Boolean, set: (Boolean) -> Unit,
                    onSync: ((Boolean) -> Unit)? = null) {
        val km = KeyMapping.get(keyId)!!
        lateinit var toggleBtn: Button
        lateinit var keyBtn: Button
        toggleBtn = Button.builder(toggleText(name, get(), km), {
            val v = !get()
            set(v)
            onSync?.invoke(v)
            Config.save()
            toggleBtn.setMessage(toggleText(name, get(), km))
            keyBtn.setMessage(keyText(km))
        }).bounds(x - 75, y, 115, 20).build()
        addRenderableWidget(toggleBtn)
        keyBtn = Button.builder(keyText(km), {
            listeningKeyId = keyId
            keyBtn.setMessage(Component.literal(">_"))
        }).bounds(x + 42, y, 60, 20).build()
        addRenderableWidget(keyBtn)
    }

    private fun keyRow(x: Int, y: Int, name: String, keyId: String) {
        val km = KeyMapping.get(keyId)!!
        lateinit var keyBtn: Button
        val labelBtn = Button.builder(Component.literal("§7$name"), { }).bounds(x - 75, y, 115, 20).build()
        labelBtn.active = false
        addRenderableWidget(labelBtn)
        keyBtn = Button.builder(keyText(km), {
            listeningKeyId = keyId
            keyBtn.setMessage(Component.literal(">_"))
        }).bounds(x + 42, y, 60, 20).build()
        addRenderableWidget(keyBtn)
    }

    override fun keyPressed(event: KeyEvent): Boolean {
        val lid = listeningKeyId
        if (lid != null) {
            if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
                listeningKeyId = null
                refreshButtons()
                return true
            }
            val km = KeyMapping.get(lid) ?: return true
            km.setKey(InputConstants.Type.KEYSYM.getOrCreate(event.key()))
            KeyMapping.resetMapping()
            listeningKeyId = null
            refreshButtons()
            saveOptions()
            return true
        }
        return super.keyPressed(event)
    }

    override fun mouseClicked(event: MouseButtonEvent, bl: Boolean): Boolean {
        val lid = listeningKeyId
        if (lid != null) {
            val km = KeyMapping.get(lid) ?: return true
            km.setKey(InputConstants.Type.MOUSE.getOrCreate(event.button()))
            KeyMapping.resetMapping()
            listeningKeyId = null
            refreshButtons()
            saveOptions()
            return true
        }
        return super.mouseClicked(event, bl)
    }

    private fun refreshButtons() {
        init()
    }

    private fun toggleText(name: String, enabled: Boolean, km: KeyMapping): Component =
        Component.literal("${if (enabled) "§a✔" else "§7✘"} $name")

    private fun keyText(km: KeyMapping): Component {
        val name = km.getTranslatedKeyMessage().string
        return Component.literal("§7[$name]")
    }

    private fun syncAll() {
        val c = Config.get()
        FullbrightHandler.loadFromConfig(c.fullbright)
        ToggleSprintHandler.loadFromConfig(c.toggleSprint)
    }

    private fun saveOptions() {
        syncAll()
        Minecraft.getInstance().options.save()
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.render(guiGraphics, mouseX, mouseY, tickDelta)
        guiGraphics.drawCenteredString(font, title, width / 2, 15, 0xFFFFFF)
        if (listeningKeyId != null) {
            guiGraphics.drawCenteredString(font, "Press a key or ESC to cancel", width / 2, height - 20, 0xFFFF55)
        }
    }

    override fun onClose() {
        saveOptions()
        Config.save()
        super.onClose()
    }
}
