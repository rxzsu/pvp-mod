package pvp.mod.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW
import pvp.mod.PvpMod
import pvp.mod.client.config.Config
import pvp.mod.client.handler.FullbrightHandler
import pvp.mod.client.handler.ToggleSprintHandler
import pvp.mod.client.handler.ZoomHandler

class PvpModClient : ClientModInitializer {
    lateinit var zoomKey: KeyMapping
    lateinit var toggleSprintKey: KeyMapping
    lateinit var fullbrightKey: KeyMapping
    lateinit var brightnessUpKey: KeyMapping
    lateinit var brightnessDownKey: KeyMapping
    lateinit var noViewBobbingKey: KeyMapping
    lateinit var noHurtTiltKey: KeyMapping
    lateinit var noNauseaKey: KeyMapping
    lateinit var noFovChangeKey: KeyMapping
    lateinit var noFireOverlayKey: KeyMapping
    lateinit var noWeatherKey: KeyMapping
    lateinit var hudKey: KeyMapping

    override fun onInitializeClient() {
        Config.load()

        FullbrightHandler.loadFromConfig(Config.get().fullbright)
        ToggleSprintHandler.loadFromConfig(Config.get().toggleSprint)
        ZoomHandler.multiplier = Config.get().zoomSensitivity

        val category = KeyMapping.Category.register(PvpMod.id("main"))

        fun reg(id: String, default: Int): KeyMapping {
            val km = KeyMapping(id, InputConstants.Type.KEYSYM, default, category)
            KeyMappingHelper.registerKeyMapping(km)
            return km
        }

        zoomKey = reg("key.pvpmod.zoom", GLFW.GLFW_KEY_V)
        toggleSprintKey = reg("key.pvpmod.toggle_sprint", GLFW.GLFW_KEY_R)
        fullbrightKey = reg("key.pvpmod.fullbright", GLFW.GLFW_KEY_F)
        brightnessUpKey = reg("key.pvpmod.brightness_up", GLFW.GLFW_KEY_PAGE_UP)
        brightnessDownKey = reg("key.pvpmod.brightness_down", GLFW.GLFW_KEY_PAGE_DOWN)
        noViewBobbingKey = reg("key.pvpmod.no_view_bobbing", GLFW.GLFW_KEY_B)
        noHurtTiltKey = reg("key.pvpmod.no_hurt_tilt", GLFW.GLFW_KEY_H)
        noNauseaKey = reg("key.pvpmod.no_nausea", GLFW.GLFW_KEY_N)
        noFovChangeKey = reg("key.pvpmod.no_fov_change", GLFW.GLFW_KEY_C)
        noFireOverlayKey = reg("key.pvpmod.no_fire_overlay", GLFW.GLFW_KEY_O)
        noWeatherKey = reg("key.pvpmod.no_weather", GLFW.GLFW_KEY_K)
        hudKey = reg("key.pvpmod.hud", GLFW.GLFW_KEY_J)

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            handleTick(client)
        }

        PvpMod.LOGGER.info("PvP-Mod client initialized!")
    }

    private fun handleTick(client: Minecraft) {
        ZoomHandler.isZooming = zoomKey.isDown

        if (toggleSprintKey.consumeClick()) {
            ToggleSprintHandler.toggle()
            Config.get().toggleSprint = ToggleSprintHandler.enabled
            Config.save()
            val msg = if (ToggleSprintHandler.enabled) "§aToggle Sprint ON" else "§cToggle Sprint OFF"
            client.player?.sendOverlayMessage(Component.literal(msg))
        }

        if (fullbrightKey.consumeClick()) {
            FullbrightHandler.toggle()
            Config.get().fullbright = FullbrightHandler.enabled
            Config.save()
            val msg = if (FullbrightHandler.enabled) "§aFullbright ON" else "§cFullbright OFF"
            client.player?.sendOverlayMessage(Component.literal(msg))
        }

        toggleConfig(client, noViewBobbingKey, "No View Bobbing", { Config.get().noViewBobbing }, { Config.get().noViewBobbing = it })
        toggleConfig(client, noHurtTiltKey, "No Hurt Tilt", { Config.get().noHurtTilt }, { Config.get().noHurtTilt = it })
        toggleConfig(client, noNauseaKey, "No Nausea", { Config.get().noNausea }, { Config.get().noNausea = it })
        toggleConfig(client, noFovChangeKey, "No FOV Change", { Config.get().noFovChange }, { Config.get().noFovChange = it })
        toggleConfig(client, noFireOverlayKey, "No Fire Overlay", { Config.get().noFireOverlay }, { Config.get().noFireOverlay = it })
        toggleConfig(client, noWeatherKey, "No Weather", { Config.get().noWeather }, { Config.get().noWeather = it })
        toggleConfig(client, hudKey, "HUD", { Config.get().hudEnabled }, { Config.get().hudEnabled = it })

        if (brightnessUpKey.consumeClick()) {
            val gamma = client.options.gamma().get()
            client.options.gamma().set((gamma + 0.25).coerceAtMost(1.0))
            client.player?.sendOverlayMessage(
                Component.literal("§7Brightness: §e${(client.options.gamma().get() * 100).toInt()}%")
            )
        }

        if (brightnessDownKey.consumeClick()) {
            val gamma = client.options.gamma().get()
            client.options.gamma().set((gamma - 0.25).coerceAtLeast(0.0))
            client.player?.sendOverlayMessage(
                Component.literal("§7Brightness: §e${(client.options.gamma().get() * 100).toInt()}%")
            )
        }

        ZoomHandler.tick()
    }

    private fun toggleConfig(client: Minecraft, key: KeyMapping, name: String, get: () -> Boolean, set: (Boolean) -> Unit) {
        if (key.consumeClick()) {
            val newVal = !get()
            set(newVal)
            Config.save()
            val msg = if (newVal) "§a$name ON" else "§c$name OFF"
            client.player?.sendOverlayMessage(Component.literal(msg))
        }
    }
}
