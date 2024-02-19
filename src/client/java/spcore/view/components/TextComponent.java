package spcore.view.components;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;
import spcore.GlobalContext;
import spcore.view.ViewComponent;

import java.awt.*;

public class TextComponent extends ViewComponent {
    private final TextRenderer textRenderer;
    private Text message;

    private float scale = 1.0f;
    public static final Identifier SPCORE_FONT_ID = new Identifier("spcore:studio/gui/jura.ttf");

    public TextComponent(String message, TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
        this.style = new TextStyles();
        this.message = Text.of(message);
//        this.message.getStyle().withFont(GlobalContext.FONT_ID);
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text text) {
        this.message = text;
    }

    @Override
    protected void internalRender(DrawContext context, int mouseX, int mouseY, float delta) {
        super.internalRender(context, mouseX, mouseY, delta);
        Text text = this.getMessage();
        TextRenderer textRenderer = this.getTextRenderer();
        var m = context.getMatrices();
        var s = (1 / scale);
        m.scale(scale, scale, scale);
        int i = this.getX() + Math.round(this.style.get(TextStyles.ALIGN, float.class) * (float)(this.getWidth() - (textRenderer.getWidth(text) * scale)));
        float j = this.getY() + (this.getHeight() - (textRenderer.fontHeight * scale)) / 2;
        context.drawTextWithShadow(textRenderer, text,  (int)(i * s), (int)(j * s), style.get(TextStyles.COLOR, int.class));
        m.scale((1 / scale), (1 / scale), (1 / scale));
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }


    public static class TextStyles extends ViewStyles {
        public static final String COLOR = "color";
        public static final String ALIGN = "align";

        public TextStyles(){
            addHandler(COLOR, "#ffD9D9D9", int.class, ViewStyles::hexToRGBA);
            addHandler(ALIGN, "0.5", float.class, Float::parseFloat);
        }

        public TextStyles alignLeft() {
            styles.put(ALIGN, "0.0");
            return this;
        }

        public TextStyles alignCenter() {
            styles.put(ALIGN, "0.5");
            return this;
        }

        public TextStyles alignRight() {
            styles.put(ALIGN, "1.0");
            return this;
        }

    }

}
