package spcore.imgui.nodes.utils;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.internal.ImGuiWindow;
import imgui.internal.ImRect;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Random;

public class ImGuiUtils {

    public static final Random rd = new Random();

    public static ImRect GetItemRect(){
        return new ImRect(ImGui.getItemRectMin(), ImGui.getItemRectMax());
    }

    public static ImRect Expanded(final ImRect rect, float x, float y){
        var result = rect;//clone?
        result.min.x -= x;
        result.min.y -= y;
        result.max.x -= x;
        result.max.y -= y;
        return result;
    }


    public static ImVec2 sub(ImVec2 v1, ImVec2 v2){
        var v1f = new Vector2f(v1.x, v1.y);
        var v2f = new Vector2f(v2.x, v2.y);
        var r = v1f.sub(v2f);
        return new ImVec2(r.x, r.y);
    }

    public static ImVec2 add(ImVec2 v1, ImVec2 v2){
        var v1f = new Vector2f(v1.x, v1.y);
        var v2f = new Vector2f(v2.x, v2.y);
        var r = v1f.add(v2f);
        return new ImVec2(r.x, r.y);
    }

    public static ImVec2 min(ImVec2 v1, ImVec2 v2){
        var v1f = new Vector2f(v1.x, v1.y);
        var v2f = new Vector2f(-v2.x, -v2.y);
        var r = v1f.add(v2f);
        return new ImVec2(r.x, r.y);
    }

    public static ImVec2 mul(ImVec2 v1, ImVec2 v2){
        var v1f = new Vector2f(v1.x, v1.y);
        var v2f = new Vector2f(v2.x, v2.y);
        var r = v1f.mul(v2f);
        return new ImVec2(r.x, r.y);
    }

    public static ImVec2 mul(ImVec2 v1, float v2){
        var v1f = new Vector2f(v1.x, v1.y);
        var r = v1f.mul(v2);
        return new ImVec2(r.x, r.y);
    }

}
