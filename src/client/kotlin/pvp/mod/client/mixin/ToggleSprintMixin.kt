package pvp.mod.client.mixin

import net.minecraft.client.player.LocalPlayer
import pvp.mod.client.handler.ToggleSprintHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LocalPlayer::class)
abstract class ToggleSprintMixin {
    // isSprinting/setSprinting объявлены в Entity (суперклассе), @Shadow их не найдёт -
    // поэтому кастим this (в рантайме это и есть LocalPlayer)
    @Suppress("USELESS_CAST")
    @Inject(method = ["aiStep"], at = [At("RETURN")])
    private fun onAiStep(ci: CallbackInfo) {
        if (!ToggleSprintHandler.enabled) return

        val self = this as LocalPlayer
        if (self.input.hasForwardImpulse() && !self.isSprinting && !self.input.keyPresses.shift()) {
            self.setSprinting(true)
        }
    }
}
