package pvp.mod.client.mixin

import net.minecraft.client.Camera
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Camera::class)
abstract class FovModifierMixin {
    @Shadow
    private var fovModifier: Float = 0f

    @Shadow
    private var oldFovModifier: Float = 0f

    @Inject(method = ["tickFov"], at = [At("HEAD")], cancellable = true)
    private fun onTickFov(ci: CallbackInfo) {
        if (Config.get().noFovChange) {
            oldFovModifier = 1.0f
            fovModifier = 1.0f
            ci.cancel()
        }
    }
}
