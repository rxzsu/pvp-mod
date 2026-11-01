package pvp.mod.client.mixin

import net.minecraft.client.gui.font.FontManager
import net.minecraft.client.gui.font.FontSet
import net.minecraft.resources.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(FontManager::class)
interface FontManagerAccessor {
    @Accessor("fontSets")
    fun fontSets(): Map<Identifier, FontSet>
}
