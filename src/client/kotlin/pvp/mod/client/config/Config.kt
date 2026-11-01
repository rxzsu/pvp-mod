package pvp.mod.client.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import net.minecraft.client.Minecraft
import pvp.mod.PvpMod
import java.io.File

data class Config(
    var modules: MutableMap<String, Boolean> = mutableMapOf(
        "noFovChange" to true,
        "noNausea" to true,
        "noHurtTilt" to true,
        "noFireOverlay" to true,
        "noWeather" to true,
        "noViewBobbing" to true,
        "fullbright" to false,
        "toggleSprint" to false,
        "hud" to true,
        "keystrokes" to false
    ),
    var zoomSensitivity: Double = 0.2,
    // Позиция Keystrokes: отступ от правого/верхнего края экрана
    var keystrokesOffsetX: Int = 6,
    var keystrokesOffsetY: Int = 6
) {
    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()
        private var instance: Config = Config()

        fun load(): Config {
            val file = getConfigFile()
            if (!file.exists()) {
                save(instance)
                return instance
            }
            try {
                val json = file.readText()
                val root = gson.fromJson(json, JsonObject::class.java)
                instance = if (root.has("modules")) {
                    // Новая схема (2.0+)
                    gson.fromJson(json, Config::class.java)
                } else {
                    // Миграция со схемы 1.x (плоские булевы поля)
                    migrateLegacy(gson.fromJson(json, Legacy::class.java))
                }
            } catch (e: Exception) {
                PvpMod.LOGGER.error("Failed to load config", e)
            }
            return instance
        }

        private fun migrateLegacy(old: Legacy): Config {
            PvpMod.LOGGER.info("Migrating legacy 1.x config to 2.0 format")
            return Config(
                modules = mutableMapOf(
                    "noFovChange" to old.noFovChange,
                    "noNausea" to old.noNausea,
                    "noHurtTilt" to old.noHurtTilt,
                    "noFireOverlay" to old.noFireOverlay,
                    "noWeather" to old.noWeather,
                    "noViewBobbing" to old.noViewBobbing,
                    "fullbright" to old.fullbright,
                    "toggleSprint" to old.toggleSprint,
                    "hud" to old.hudEnabled,
                    "keystrokes" to false
                ),
                zoomSensitivity = old.zoomSensitivity
            )
        }

        private class Legacy(
            var fullbright: Boolean = false,
            var toggleSprint: Boolean = false,
            var noViewBobbing: Boolean = true,
            var noHurtTilt: Boolean = true,
            var noNausea: Boolean = true,
            var noFovChange: Boolean = true,
            var noFireOverlay: Boolean = true,
            var noWeather: Boolean = true,
            var hudEnabled: Boolean = true,
            var zoomSensitivity: Double = 0.2
        )

        fun save(config: Config = instance) {
            instance = config
            try {
                val f = getConfigFile()
                f.parentFile.mkdirs()
                f.writeText(gson.toJson(config))
            } catch (e: Exception) {
                PvpMod.LOGGER.error("Failed to save config", e)
            }
        }

        fun get(): Config = instance

        private fun getConfigFile(): File {
            val gameDir = Minecraft.getInstance().gameDirectory
            return File(gameDir, "config/pvp-mod.json")
        }
    }
}
