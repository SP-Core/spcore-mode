package spcore.imgui;

import imgui.*;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.glfw.GLFW;
import spcore.GlobalContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImGuiImpl {
    private final static ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private final static ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();

    private static List<ImFont> juraFonts;
    public static List<ImFont> getJuraFonts(){
        return juraFonts;
    }

    public static ImFont GetBigJuraFont(){
        return juraFonts.get(30);
    }

    public static void create(final long handle) {
        ImGui.createContext();
        ImPlot.createContext();
        ImNodes.createContext();

        final ImGuiIO data = ImGui.getIO();
        data.setIniFilename("modid.ini");
        data.setFontGlobalScale(1F);


        {
            final ImFontAtlas fonts = data.getFonts();
            final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();

            rangesBuilder.addRanges(data.getFonts().getGlyphRangesDefault());
            rangesBuilder.addRanges(data.getFonts().getGlyphRangesCyrillic());
            rangesBuilder.addRanges(data.getFonts().getGlyphRangesJapanese());

            final short[] glyphRanges = rangesBuilder.buildRanges();

            final ImFontConfig basicConfig = new ImFontConfig();
            basicConfig.setGlyphRanges(data.getFonts().getGlyphRangesCyrillic());
            juraFonts = new ArrayList<>();
            for (int i = 5 /* MINIMUM_FONT_SIZE */; i < 50 /* MAXIMUM_FONT_SIZE */; i++) {
                basicConfig.setName("Jura" + i + "px");
                try {
                    var r = ImGuiImpl.class
                            .getResourceAsStream("/gui/fonts/Jura-VariableFont_wght.ttf");
                    juraFonts.add(fonts.addFontFromMemoryTTF(IOUtils.toByteArray(r), i, basicConfig, glyphRanges));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            fonts.build();
            basicConfig.destroy();
        }
        data.setFontDefault(GetBigJuraFont());


        data.setConfigFlags(ImGuiConfigFlags.DockingEnable);



        imGuiImplGlfw.init(handle, true);
        imGuiImplGl3.init();
    }

    public static void draw(final RenderInterface runnable) {
//        imGuiImplGlfw.newFrame(); // Handle keyboard and mouse interactions
//        ImGui.newFrame();
        try{

            imGuiImplGlfw.newFrame(); // Handle keyboard and mouse interactions
            ImGui.newFrame();
            runnable.render(ImGui.getIO());
        }
        catch (Exception | Error e){
            GlobalContext.LOGGER.error(e.getMessage());
        }
        finally {
            try{
                ImGui.render();
                imGuiImplGl3.renderDrawData(ImGui.getDrawData());
                if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                    final long pointer = GLFW.glfwGetCurrentContext();
                    ImGui.updatePlatformWindows();
                    ImGui.renderPlatformWindowsDefault();

                    GLFW.glfwMakeContextCurrent(pointer);
                }
            }
            catch (Exception | Error e){
                GlobalContext.LOGGER.error(e.getMessage());

            }

        }

    }
}
