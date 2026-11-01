package pvp.mod.client.mixin

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.font.FontManager
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(Minecraft::class)
interface MinecraftAccessor {
    @Accessor("fontManager")
    fun fontManager(): FontManager
}
