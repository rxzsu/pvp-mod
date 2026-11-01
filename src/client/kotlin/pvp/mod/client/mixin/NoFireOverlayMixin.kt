package pvp.mod.client.mixin

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.ScreenEffectRenderer
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At

@Mixin(ScreenEffectRenderer::class)
abstract class NoFireOverlayMixin {
    // В 26.2 огонь рисуется внутри submit(); глушим только вызов isOnFire(),
    // чтобы не трогать оверлеи воды и блока
    @WrapOperation(
        method = ["submit"],
        at = [At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z")]
    )
    private fun redirectIsOnFire(instance: LocalPlayer, original: Operation<Boolean>): Boolean {
        if (Config.get().noFireOverlay) return false
        return original.call(instance)
    }
}
