package pvp.mod.client.mixin

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(GameRenderer::class)
abstract class NoViewBobbingMixin {
    @Inject(method = ["bobView"], at = [At("HEAD")], cancellable = true)
    private fun onBobView(cameraRenderState: CameraRenderState, poseStack: PoseStack, ci: CallbackInfo) {
        if (Config.get().noViewBobbing) {
            ci.cancel()
        }
    }
}
