package pvp.mod.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.font.FontSet
import pvp.mod.client.mixin.FontManagerAccessor
import pvp.mod.client.mixin.MinecraftAccessor
import net.minecraft.network.chat.FontDescription
import net.minecraft.resources.Identifier
import pvp.mod.PvpMod

/**
 * Современный шрифт (Inter) для кастомного UI, в обход пиксельного ванильного.
 * Typeface'ы грузятся стандартным resource loader из assets/pvp-mod/font/.
 */
object MenuFont {
    lateinit var medium: Font
        private set
    lateinit var semibold: Font
        private set

    val available: Boolean get() = ::medium.isInitialized

    fun ensure() {
        if (available) return
        try {
            val fm = (Minecraft.getInstance() as MinecraftAccessor).fontManager()
            val sets = (fm as FontManagerAccessor).fontSets()
            medium = buildFont(sets, "inter")
            semibold = buildFont(sets, "inter_semibold")
            PvpMod.LOGGER.info("Menu fonts loaded")
        } catch (e: Exception) {
            PvpMod.LOGGER.error("Failed to load menu fonts, falling back to vanilla", e)
            medium = Minecraft.getInstance().font
            semibold = Minecraft.getInstance().font
        }
    }

    private fun buildFont(sets: Map<Identifier, FontSet>, name: String): Font {
        val id = Identifier.fromNamespaceAndPath(PvpMod.MOD_ID, name)
        val set = sets[id] ?: throw IllegalStateException("Typeface $id is not loaded")
        val provider = object : Font.Provider {
            override fun glyphs(desc: FontDescription) = set.source(false)
            override fun effect() = set.whiteGlyph()
        }
        return Font(provider)
    }
}
