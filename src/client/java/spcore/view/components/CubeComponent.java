package spcore.view.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector2i;
import spcore.view.render.RenderContext;
import spcore.view.render.Renderable;

public class CubeComponent extends Renderable {

    protected CubeComponent() {
        super(new Vector2i(0, 0));
    }

    @Override
    protected void internalConfigure(RenderContext render) {
    }

    @Override
    protected void internalRender(DrawContext context, int mouseX, int mouseY, float delta) {

    }
}
