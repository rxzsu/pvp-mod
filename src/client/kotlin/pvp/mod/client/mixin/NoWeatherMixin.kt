package pvp.mod.client.mixin

import net.minecraft.client.multiplayer.ClientLevel
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ClientLevel::class)
abstract class NoWeatherMixin {
    @Inject(method = ["getRainLevel"], at = [At("HEAD")], cancellable = true)
    private fun onGetRainLevel(tickDelta: Float, cir: CallbackInfoReturnable<Float>) {
        if (Config.get().noWeather) cir.returnValue = 0.0f
    }

    @Inject(method = ["getThunderLevel"], at = [At("HEAD")], cancellable = true)
    private fun onGetThunderLevel(tickDelta: Float, cir: CallbackInfoReturnable<Float>) {
        if (Config.get().noWeather) cir.returnValue = 0.0f
    }
}
