package spcore.imgui.nodes.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import spcore.imgui.ImGuiImpl;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageLoader {

    public static int loadImageFromImageBuffer(ByteBuffer imageBuffer, int width, int height) {

        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);

        STBImage.stbi_image_free(imageBuffer);

        return textureId;
    }
}