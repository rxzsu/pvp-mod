package pvp.mod.client.mixin

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.PostChain
import net.minecraft.client.renderer.ShaderManager
import net.minecraft.resources.Identifier
import pvp.mod.client.config.Config
import pvp.mod.client.gui.ClickGui
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At

@Mixin(GameRenderer::class)
abstract class GameRendererMixin {
    // Подменяем ванильный blur chain на наш gaussian, когда открыт ClickGui и включён fancyBlur
    @WrapOperation(
        method = ["processBlurEffect"],
        at = [At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShaderManager;getPostChain(Lnet/minecraft/resources/Identifier;Ljava/util/Set;)Lnet/minecraft/client/renderer/PostChain;")]
    )
    private fun wrapBlurChain(
        instance: ShaderManager,
        id: Identifier,
        targets: Set<Identifier>,
        original: Operation<PostChain>
    ): PostChain {
        val screen: Screen? = Minecraft.getInstance().gui.screen()
        if (screen is ClickGui && Config.get().fancyBlur) {
            try {
                return original.call(instance, Identifier.fromNamespaceAndPath("pvp-mod", "menu_blur"), targets)
            } catch (e: Exception) {
                pvp.mod.PvpMod.LOGGER.error("Failed to load custom blur shader, falling back to vanilla", e)
                Config.get().fancyBlur = false
            }
        }
        return original.call(instance, id, targets)
    }
}
