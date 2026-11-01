package pvp.mod.client.mixin

import net.minecraft.client.Camera
import pvp.mod.client.handler.ZoomHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(Camera::class)
abstract class ZoomMixin {
    @Inject(method = ["getFov"], at = [At("RETURN")], cancellable = true)
    private fun onGetFov(cir: CallbackInfoReturnable<Float>) {
        if (ZoomHandler.isZooming) {
            cir.returnValue = cir.returnValue * ZoomHandler.getZoomMultiplier().toFloat()
        }
    }
}
