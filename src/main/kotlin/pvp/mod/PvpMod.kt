package pvp.mod

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PvpMod : ModInitializer {
    companion object {
        const val MOD_ID = "pvp-mod"
        val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

        fun id(path: String) = Identifier.fromNamespaceAndPath(MOD_ID, path)
    }

    override fun onInitialize() {
        LOGGER.info("PvP-Mod initialized!")
    }
}
