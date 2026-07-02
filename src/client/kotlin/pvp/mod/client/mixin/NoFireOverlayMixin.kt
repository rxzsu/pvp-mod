package pvp.mod.client.mixin

import net.minecraft.client.renderer.ScreenEffectRenderer
import net.minecraft.client.renderer.SubmitNodeCollector
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ScreenEffectRenderer::class)
abstract class NoFireOverlayMixin {
    @Inject(method = ["renderScreenEffect"], at = [At("HEAD")], cancellable = true)
    private fun onRenderScreenEffect(renderFire: Boolean, alpha: Float, collector: SubmitNodeCollector, ci: CallbackInfo) {
        if (Config.get().noFireOverlay) {
            ci.cancel()
        }
    }
}
