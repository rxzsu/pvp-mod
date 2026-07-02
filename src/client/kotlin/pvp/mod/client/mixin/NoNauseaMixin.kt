package pvp.mod.client.mixin

import net.minecraft.client.renderer.GameRenderer
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Mixin(GameRenderer::class)
abstract class NoNauseaMixin {
    @Redirect(
        method = ["renderLevel"],
        at = At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z")
    )
    private fun redirectHasEffect(instance: LivingEntity, effect: Holder<MobEffect>): Boolean {
        if (Config.get().noNausea && effect == MobEffects.NAUSEA) return false
        return instance.hasEffect(effect)
    }
}
