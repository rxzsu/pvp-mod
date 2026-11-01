package pvp.mod.client.mixin

import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import pvp.mod.client.modules.KeystrokesRenderer
import pvp.mod.client.modules.Modules
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Hud::class)
abstract class HudMixin {
    @Inject(method = ["extractRenderState"], at = [At("RETURN")])
    private fun onExtractRenderState(extractor: GuiGraphicsExtractor, deltaTracker: DeltaTracker, ci: CallbackInfo) {
        if (!Modules.hud.enabled) return

        val client = Minecraft.getInstance()
        val player = client.player ?: return

        val pos = player.blockPosition()
        val coords = "X: ${pos.x} Y: ${pos.y} Z: ${pos.z}"
        val ping = player.connection.getPlayerInfo(player.uuid)?.latency ?: 0

        extractor.text(client.font, coords, 4, 4, 0xFFFFFFFF.toInt(), true)
        extractor.text(client.font, "FPS: ${client.fps}  Ping: ${ping}ms", 4, 14, 0xFFFFFFFF.toInt(), true)

        if (Modules.keystrokes.enabled) {
            KeystrokesRenderer.render(extractor)
        }

        if (Modules.statusHud.enabled) {
            var sy = 24

            for (e in player.activeEffects) {
                val name = Component.translatable(e.effect.value().descriptionId).string
                val secs = e.duration / 20
                val time = if (secs >= 60) "${secs / 60}:${(secs % 60).toString().padStart(2, '0')}" else "${secs}s"
                extractor.text(client.font, "$name §7$time", 4, sy, 0xFFFFFFFF.toInt(), true)
                sy += 10
            }

            val labels = mapOf(EquipmentSlot.HEAD to "Helm", EquipmentSlot.CHEST to "Chest",
                EquipmentSlot.LEGS to "Legs", EquipmentSlot.FEET to "Boots")
            for ((slot, label) in labels) {
                val st = player.getItemBySlot(slot)
                if (!st.isEmpty && st.maxDamage > 0) {
                    val pct = (st.maxDamage - st.damageValue) * 100 / st.maxDamage
                    val col = if (pct > 50) 0xFFFFFFFF.toInt() else if (pct > 20) 0xFFFFAA00.toInt() else 0xFFFF5555.toInt()
                    extractor.text(client.font, "$label §7$pct%", 4, sy, col, true)
                    sy += 10
                }
            }
        }
    }
}
