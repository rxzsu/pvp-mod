package pvp.mod.client.mixin

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
import net.minecraft.client.Minecraft
import net.minecraft.client.OptionInstance
import net.minecraft.client.renderer.LightmapRenderStateExtractor
import org.spongepowered.asm.mixin.injection.At
import pvp.mod.client.modules.Modules
import org.spongepowered.asm.mixin.Mixin

@Mixin(LightmapRenderStateExtractor::class)
abstract class FullbrightMixin {
    // extract() вызывает OptionInstance.get() дважды (hideLightningFlash и gamma) - трогаем только гамму
    @WrapOperation(
        method = ["extract"],
        at = [At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;")]
    )
    private fun redirectGamma(instance: OptionInstance<*>, original: Operation<*>): Any {
        if (Modules.fullbright.enabled && instance === Minecraft.getInstance().options.gamma()) return 16.0
        return original.call(instance)
    }
}
