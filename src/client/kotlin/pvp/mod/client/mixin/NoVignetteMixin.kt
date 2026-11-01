package pvp.mod.client.mixin

import net.minecraft.client.gui.Hud
import pvp.mod.client.modules.Modules
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Hud::class)
abstract class NoVignetteMixin {
    @Inject(method = ["extractVignette"], at = [At("HEAD")], cancellable = true)
    private fun onExtractVignette(ci: CallbackInfo) {
        if (Modules.noVignette.enabled) ci.cancel()
    }
}
