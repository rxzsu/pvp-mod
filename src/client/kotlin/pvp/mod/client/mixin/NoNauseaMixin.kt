package pvp.mod.client.mixin

import net.minecraft.client.renderer.GameRenderer
import pvp.mod.client.modules.Modules
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(GameRenderer::class)
abstract class NoNauseaMixin {
    @Shadow
    private var spinningEffectTime: Float = 0f

    @Shadow
    private var spinningEffectSpeed: Float = 0f

    @Inject(method = ["tick"], at = [At("RETURN")])
    private fun onTick(ci: CallbackInfo) {
        if (Modules.noNausea.enabled) {
            spinningEffectTime = 0.0f
            spinningEffectSpeed = 0.0f
        }
    }
}
