package pvp.mod.client.mixin

import net.minecraft.client.player.LocalPlayer
import pvp.mod.client.handler.ToggleSprintHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LocalPlayer::class)
abstract class ToggleSprintMixin {
    @Inject(method = ["aiStep"], at = [At("RETURN")])
    private fun onAiStep(ci: CallbackInfo) {
        if (!ToggleSprintHandler.enabled) return

        val self = this as LocalPlayer
        if (self.input.hasForwardImpulse() && !self.isSprinting && !self.input.keyPresses.shift()) {
            self.setSprinting(true)
        }
    }
}
