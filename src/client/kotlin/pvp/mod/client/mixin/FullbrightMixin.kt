package pvp.mod.client.mixin

import net.minecraft.client.Minecraft
import net.minecraft.client.OptionInstance
import net.minecraft.client.renderer.LightmapRenderStateExtractor
import pvp.mod.client.handler.FullbrightHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Mixin(LightmapRenderStateExtractor::class)
abstract class FullbrightMixin {
    @Redirect(
        method = ["extract"],
        at = At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;")
    )
    private fun redirectGamma(instance: OptionInstance<*>): Any {
        // extract() вызывает OptionInstance.get() дважды (hideLightningFlash и gamma) - трогаем только гамму
        if (FullbrightHandler.enabled && instance === Minecraft.getInstance().options.gamma()) return 16.0
        return instance.get()
    }
}
