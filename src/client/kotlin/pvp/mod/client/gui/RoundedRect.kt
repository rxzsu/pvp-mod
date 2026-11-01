package pvp.mod.client.gui

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import net.minecraft.client.renderer.state.gui.GuiRenderState
import pvp.mod.client.mixin.GuiGraphicsExtractorAccessor

/**
 * Скруглённый прямоугольник: центр и рёбра - обычные fill, углы - SDF-шейдер (GLSL).
 */
object RoundedRect {
    const val RADIUS = 6

    fun fill(extractor: GuiGraphicsExtractor, x1: Int, y1: Int, x2: Int, y2: Int, argb: Int) {
        val r = RADIUS
        val state: GuiRenderState = (extractor as GuiGraphicsExtractorAccessor).guiRenderState()

        // центральная вертикальная полоса + боковые
        extractor.fill(x1 + r, y1, x2 - r, y2, argb)
        extractor.fill(x1, y1 + r, x1 + r, y2 - r, argb)
        extractor.fill(x2 - r, y1 + r, x2, y2 - r, argb)

        // 4 уголка (SDF)
        state.addGuiElement(CornerElement(x1, y1, r, Corner.TL, argb))
        state.addGuiElement(CornerElement(x2 - r, y1, r, Corner.TR, argb))
        state.addGuiElement(CornerElement(x1, y2 - r, r, Corner.BL, argb))
        state.addGuiElement(CornerElement(x2 - r, y2 - r, r, Corner.BR, argb))
    }
}

private enum class Corner { TL, TR, BL, BR }

private class CornerElement(
    private val x: Int,
    private val y: Int,
    private val r: Int,
    private val corner: Corner,
    private val argb: Int
) : GuiElementRenderState {
    override fun buildVertices(vc: com.mojang.blaze3d.vertex.VertexConsumer) {
        // uv = локальные координаты внутри квадрата уголка, так чтобы центр дуги был в (r, r)
        var u1 = 0f; var v1 = 0f; var u2 = r.toFloat(); var v2 = r.toFloat()
        if (corner == Corner.TR || corner == Corner.BR) { u1 = r.toFloat(); u2 = 0f }
        if (corner == Corner.BL || corner == Corner.BR) { v1 = r.toFloat(); v2 = 0f }

        vc.addVertex(x.toFloat(), y.toFloat(), 0f).setUv(u1, v1).setColor(argb)
        vc.addVertex(x.toFloat(), (y + r).toFloat(), 0f).setUv(u1, v2).setColor(argb)
        vc.addVertex((x + r).toFloat(), (y + r).toFloat(), 0f).setUv(u2, v2).setColor(argb)
        vc.addVertex((x + r).toFloat(), y.toFloat(), 0f).setUv(u2, v1).setColor(argb)
    }

    override fun pipeline() = PvpPipelines.ROUNDED_CORNER

    override fun textureSetup() = net.minecraft.client.gui.render.TextureSetup.noTexture()

    override fun scissorArea(): ScreenRectangle = ScreenRectangle(x, y, r, r)

    override fun bounds(): ScreenRectangle = ScreenRectangle(x, y, r, r)
}
