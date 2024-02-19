package spcore.imgui.nodes.utils;

import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.internal.ImRect;
import spcore.imgui.nodes.enums.IconType;

public class WidgetsUtils {
    public static void Icon(ImVec2 size, IconType type, boolean filled, int color, int innerColor){
        if(ImGui.isRectVisible(size.x, size.y)){
            var cursorPos = ImGui.getCursorScreenPos();
            var drawList = ImGui.getWindowDrawList();
            var fff = ImGuiUtils.add(cursorPos, size);
            DrawingUtils.DrawIcon(drawList, cursorPos, fff, type, filled, color, innerColor);
        }
        ImGui.dummy(size.x, size.y);
    }

    static float calcMaxPopupHeightFromItemCount(int itemsCount) {
        if (itemsCount <= 0)
            return Float.MAX_VALUE;
        return (ImGui.getFontSize() + ImGui.getStyle().getItemSpacing().y) * itemsCount - ImGui.getStyle().getItemSpacing().y + (ImGui.getStyle().getWindowPadding().y * 2);
    }



}
