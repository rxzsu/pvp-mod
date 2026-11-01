package pvp.mod.client.gui

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.minecraft.client.renderer.RenderPipelines
import pvp.mod.client.mixin.RenderPipelinesAccessor

object PvpPipelines {
    /** Четверть-круглый уголок: SDF в фрагментном шейдере, RADIUS - радиус в пикселях GUI. */
    val ROUNDED_CORNER: RenderPipeline = register(
        RenderPipeline.builder(RenderPipelinesAccessor.guiSnippet())
            .withLocation("pvp-mod:rounded_corner")
            .withVertexShader("pvp-mod:rounded_corner")
            .withFragmentShader("pvp-mod:rounded_corner")
            .withShaderDefine("RADIUS", 6.0f)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .build()
    )

    private fun register(builder: RenderPipeline): RenderPipeline =
        RenderPipelinesAccessor.invokeRegister(builder)
}
