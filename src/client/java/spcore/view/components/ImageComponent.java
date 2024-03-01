package spcore.view.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import spcore.view.ViewComponent;
import spcore.view.render.RenderContext;
import spcore.view.render.Renderable;

public class ImageComponent extends Renderable implements Drawable {
    public ImageComponent() {
        super(new Vector2i(0, 0));
        this.style = new ViewStyles();
    }

    @Override
    protected void internalConfigure(RenderContext render) {

    }

    @Override
    protected void internalRender(DrawContext context, int mouseX, int mouseY, float delta) {
        var src = style.get(ViewStyles.SRC, String.class);
        if(src == null || src.equals("")){
            return;
        }
        float x = getX(); float y = getY(); float width = getWidth(); float height = getHeight();
        if(Identifier.isValid("spnet:" + src)){
            var textureId = new Identifier("spnet:" + src);
            drawTexture(context, textureId, x, y, 0.0F, 0.0F, width, height, width, height);
        }
    }

    public void drawTexture(DrawContext context, Identifier texture, float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        this.drawTexture(context, texture, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    public void drawTexture(DrawContext context,
            Identifier texture, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight
    ) {
        this.drawTexture(context, texture, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    void drawTexture(DrawContext context,
            Identifier texture, float x1, float x2, float y1, float y2, float z,
                     float regionWidth, float regionHeight, float u, float v,
                     float textureWidth, float textureHeight
    ) {
        drawTexturedQuad(
                context,
                texture,
                x1,
                x2,
                y1,
                y2,
                z,
                (u + 0.0F) / textureWidth,
                (u + regionWidth) / textureWidth,
                (v + 0.0F) / textureHeight,
                (v + regionHeight) / textureHeight
        );
    }


    private void drawTexturedQuad(DrawContext context, Identifier texture,
                                  float x1, float x2, float y1, float y2, float z,
                                  float u1, float u2, float v1, float v2){
        var matrices = context.getMatrices();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix4f, x1, y2, z).texture(u1, v2).next();
        bufferBuilder.vertex(matrix4f, x2, y2, z).texture(u2, v2).next();
        bufferBuilder.vertex(matrix4f, x2, y1, z).texture(u2, v1).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static class ViewStyles extends RenderableStyles{
        public static final String SRC = "src";


        public ViewStyles(){
            addHandler(SRC, "global/omega.png", String.class, p -> p);
        }



    }

}
