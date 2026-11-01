package pvp.mod.client.mixin

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
import net.minecraft.client.gui.Hud
import net.minecraft.world.item.equipment.Equippable
import pvp.mod.client.modules.Modules
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import java.util.Optional

@Mixin(Hud::class)
abstract class NoPumpkinMixin {
    // В 26.2 тыквенный оверлей data-driven: рисуется через Equippable.cameraOverlay()
    @WrapOperation(
        method = ["extractCameraOverlays"],
        at = [At(value = "INVOKE", target = "Lnet/minecraft/world/item/equipment/Equippable;cameraOverlay()Ljava/util/Optional;")]
    )
    private fun wrapCameraOverlay(instance: Equippable, original: Operation<Optional<String>>): Optional<*> {
        if (Modules.noPumpkin.enabled) return Optional.empty<Any>()
        return original.call(instance)
    }
}
