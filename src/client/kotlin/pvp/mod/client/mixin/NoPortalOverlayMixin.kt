package pvp.mod.client.mixin

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
import net.minecraft.client.gui.Hud
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.Holder
import pvp.mod.client.modules.Modules
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At

@Mixin(Hud::class)
abstract class NoPortalOverlayMixin {
    // Портальная засветка в 26.2 рисуется через getEffectBlendFactor(NAUSEA) - гасим при No Nausea
    @WrapOperation(
        method = ["extractCameraOverlays"],
        at = [At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F")]
    )
    private fun wrapPortalOverlay(instance: LocalPlayer, effect: Holder<*>, tickDelta: Float,
                                  original: Operation<Float>): Float {
        if (Modules.noNausea.enabled) return 0.0f
        return original.call(instance, effect, tickDelta)
    }
}
