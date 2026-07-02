package pvp.mod.client.mixin

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.GameRenderer
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(GameRenderer::class)
abstract class NoHurtTiltMixin {
    @Inject(method = ["bobHurt"], at = [At("HEAD")], cancellable = true)
    private fun onBobHurt(poseStack: PoseStack, tickDelta: Float, ci: CallbackInfo) {
        if (Config.get().noHurtTilt) {
            ci.cancel()
        }
    }
}
