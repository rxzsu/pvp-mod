package pvp.mod.client.mixin

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Camera
import net.minecraft.client.renderer.GameRenderer
import pvp.mod.client.handler.ZoomHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(GameRenderer::class)
abstract class ZoomMixin {
    @Inject(method = ["getFov"], at = [At("RETURN")], cancellable = true)
    private fun onGetFov(camera: Camera, tickDelta: Float, changingFov: Boolean, cir: CallbackInfoReturnable<Float>) {
        if (ZoomHandler.isZooming) {
            cir.returnValue = cir.returnValue * ZoomHandler.getZoomMultiplier().toFloat()
        }
    }
}
