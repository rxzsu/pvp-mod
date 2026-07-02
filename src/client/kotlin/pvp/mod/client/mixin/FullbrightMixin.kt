package pvp.mod.client.mixin

import net.minecraft.client.OptionInstance
import net.minecraft.client.renderer.LightTexture
import pvp.mod.client.handler.FullbrightHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Mixin(LightTexture::class)
abstract class FullbrightMixin {
    @Redirect(
        method = ["updateLightTexture"],
        at = At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;")
    )
    private fun redirectGamma(instance: OptionInstance<*>): Any {
        if (FullbrightHandler.enabled) return 16.0
        return instance.get()
    }
}
