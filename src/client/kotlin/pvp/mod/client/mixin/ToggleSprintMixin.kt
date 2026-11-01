package pvp.mod.client.mixin

import net.minecraft.client.player.ClientInput
import net.minecraft.client.player.LocalPlayer
import pvp.mod.client.handler.ToggleSprintHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LocalPlayer::class)
abstract class ToggleSprintMixin {
    @Shadow
    lateinit var input: ClientInput

    @Shadow
    abstract fun isSprinting(): Boolean

    @Shadow
    abstract fun setSprinting(sprinting: Boolean)

    @Inject(method = ["aiStep"], at = [At("RETURN")])
    private fun onAiStep(ci: CallbackInfo) {
        if (!ToggleSprintHandler.enabled) return

        if (input.hasForwardImpulse() && !isSprinting() && !input.keyPresses.shift()) {
            setSprinting(true)
        }
    }
}
