package pvp.mod.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW
import pvp.mod.PvpMod
import pvp.mod.client.config.Config
import pvp.mod.client.config.ConfigScreen
import pvp.mod.client.handler.ZoomHandler
import pvp.mod.client.modules.Module
import pvp.mod.client.modules.ModuleManager
import pvp.mod.client.modules.Modules

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
        Modules.init()
        ModuleManager.applyConfig()
        ZoomHandler.multiplier = Config.get().zoomSensitivity

        fun reg(id: String, default: Int): KeyMapping {
            val km = KeyMapping(id, InputConstants.Type.KEYSYM, default, KeyMapping.Category.MISC)
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

        Modules.fullbright.keybind = fullbrightKey
        Modules.toggleSprint.keybind = toggleSprintKey
        Modules.hud.keybind = hudKey
        Modules.noViewBobbing.keybind = noViewBobbingKey
        Modules.noHurtTilt.keybind = noHurtTiltKey
        Modules.noNausea.keybind = noNauseaKey
        Modules.noFovChange.keybind = noFovChangeKey
        Modules.noFireOverlay.keybind = noFireOverlayKey
        Modules.noWeather.keybind = noWeatherKey

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            handleTick(client)
        }

        PvpMod.LOGGER.info("PvP-Mod client initialized!")
    }

    private fun handleTick(client: Minecraft) {
        ZoomHandler.isZooming = zoomKey.isDown

        toggle(client, toggleSprintKey, Modules.toggleSprint)
        toggle(client, fullbrightKey, Modules.fullbright)
        toggle(client, noViewBobbingKey, Modules.noViewBobbing)
        toggle(client, noHurtTiltKey, Modules.noHurtTilt)
        toggle(client, noNauseaKey, Modules.noNausea)
        toggle(client, noFovChangeKey, Modules.noFovChange)
        toggle(client, noFireOverlayKey, Modules.noFireOverlay)
        toggle(client, noWeatherKey, Modules.noWeather)
        toggle(client, hudKey, Modules.hud)

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

    private fun toggle(client: Minecraft, key: KeyMapping, module: Module) {
        if (key.consumeClick()) {
            module.toggle()
            val msg = if (module.enabled) "§a${module.name} ON" else "§c${module.name} OFF"
            client.player?.sendOverlayMessage(Component.literal(msg))
        }
    }
}
