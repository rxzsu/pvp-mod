package pvp.mod.client.mixin

import net.minecraft.client.MouseHandler
import net.minecraft.client.input.MouseButtonInfo
import pvp.mod.client.modules.KeystrokesTracker
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MouseHandler::class)
abstract class MouseHandlerMixin {
    @Inject(method = ["onButton"], at = [At("HEAD")])
    private fun onOnButton(window: Long, info: MouseButtonInfo, action: Int, ci: CallbackInfo) {
        KeystrokesTracker.onMouseButton(info.button(), action)
    }
}
