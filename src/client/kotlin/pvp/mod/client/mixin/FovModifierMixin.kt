package pvp.mod.client.mixin

import net.minecraft.client.player.AbstractClientPlayer
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(AbstractClientPlayer::class)
abstract class FovModifierMixin {
    @Inject(method = ["getFieldOfViewModifier"], at = [At("HEAD")], cancellable = true)
    private fun onGetFieldOfViewModifier(cir: CallbackInfoReturnable<Float>) {
        if (Config.get().noFovChange) {
            cir.returnValue = 1.0f
        }
    }
}
