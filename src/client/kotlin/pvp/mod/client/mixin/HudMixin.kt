package pvp.mod.client.mixin

import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import pvp.mod.client.config.Config
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Hud::class)
abstract class HudMixin {
    @Inject(method = ["extractRenderState"], at = [At("RETURN")])
    private fun onExtractRenderState(extractor: GuiGraphicsExtractor, deltaTracker: DeltaTracker, ci: CallbackInfo) {
        if (!Config.get().hudEnabled) return

        val client = Minecraft.getInstance()
        val player = client.player ?: return

        val pos = player.blockPosition()
        val coords = "X: ${pos.x} Y: ${pos.y} Z: ${pos.z}"
        val ping = player.connection.getPlayerInfo(player.uuid)?.latency ?: 0

        extractor.text(client.font, coords, 4, 4, 0xFFFFFF, true)
        extractor.text(client.font, "FPS: ${client.fps}  Ping: ${ping}ms", 4, 14, 0xFFFFFF, true)
    }
}
