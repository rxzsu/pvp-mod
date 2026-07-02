package pvp.mod.client.config

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.minecraft.client.Minecraft
import pvp.mod.PvpMod
import java.io.File

data class Config(
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
                val type = object : TypeToken<Config>() {}.type
                instance = gson.fromJson(json, type)
            } catch (e: Exception) {
                PvpMod.LOGGER.error("Failed to load config", e)
            }
            return instance
        }

        fun save(config: Config = instance) {
            instance = config
            try {
                getConfigFile().writeText(gson.toJson(config))
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
