package pvp.mod.client.mixin

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import pvp.mod.client.modules.Modules
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(GameRenderer::class)
abstract class NoHurtTiltMixin {
    @Inject(method = ["bobHurt"], at = [At("HEAD")], cancellable = true)
    private fun onBobHurt(cameraRenderState: CameraRenderState, poseStack: PoseStack, ci: CallbackInfo) {
        if (Modules.noHurtTilt.enabled) {
            ci.cancel()
        }
    }
}
