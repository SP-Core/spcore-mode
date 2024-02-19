package spcore.imgui.nodes.utils;

import imgui.ImDrawList;
import imgui.ImVec2;
import imgui.flag.ImDrawFlags;
import imgui.internal.ImRect;
import spcore.imgui.nodes.enums.IconType;

public class DrawingUtils {
    public static void DrawIcon(ImDrawList drawList, ImVec2 a, ImVec2 b, IconType type, boolean filled, int color, int innerColor){
        ImRect rect = new ImRect(a, b);
        float rect_x = rect.min.x;
        float rect_y = rect.min.y;
        float rect_w = rect.max.x - rect.min.x;
        float rect_h = rect.max.y - rect.min.y;
        float rect_center_x = (rect.min.x + rect.max.x) * 0.5f;
        float rect_center_y = (rect.min.y + rect.max.y) * 0.5f;
        ImVec2 rect_center = new ImVec2(rect_center_x, rect_center_y);
        float outline_scale = rect_w / 24.0f;
        int extra_segments = (int) (2 * outline_scale); // for full circle

        if (type == IconType.Flow) {
            float triangleStart = rect.min.x;
            int rect_offset = -(int) (rect_w * 0.25f * 0.25f);

            rect.min.x += rect_offset;
            rect.max.x += rect_offset;
            rect_x += rect_offset;
            rect_center_x += rect_offset * 0.5f;
            rect_center.x += rect_offset * 0.5f;

            float triangleTip = triangleStart + 20;

            var v1 = new ImVec2((float) Math.ceil(triangleTip), rect_y + rect_h * 0.5f);
            var v2 = new ImVec2(triangleStart, rect_center_y + 0.5f * rect_h);
            var v3 = new ImVec2(triangleStart, rect_center_y - 0.5f * rect_h);
            drawList.addTriangleFilled(
                    v1.x, v1.y,
                    v2.x, v2.y,
                    v3.x, v3.y,
                    color);

//            float origin_scale = rect_w / 24.0f;
//            float offset_x = 1.0f * origin_scale;
//            float offset_y = 0.0f * origin_scale;
//            float margin = (filled ? 2.0f : 2.0f) * origin_scale;
//            float rounding = 0.1f * origin_scale;
//            float tip_round = 0.7f; // percentage of triangle edge (for tip)
//            ImRect canvas = new ImRect(
//                    rect.min.x + margin + offset_x,
//                    rect.min.y + margin + offset_y,
//                    rect.max.x - margin + offset_x,
//                    rect.max.y - margin + offset_y
//            );
//            float canvas_x = canvas.min.x;
//            float canvas_y = canvas.min.y;
//            float canvas_w = canvas.max.x - canvas.min.x;
//            float canvas_h = canvas.max.y - canvas.min.y;
//
//            float left = canvas_x + canvas_w * 0.5f * 0.3f;
//            float right = canvas_x + canvas_w - canvas_w * 0.5f * 0.3f;
//            float top = canvas_y + canvas_h * 0.5f * 0.2f;
//            float bottom = canvas_y + canvas_h - canvas_h * 0.5f * 0.2f;
//            float center_y = (top + bottom) * 0.5f;
//
//            ImVec2 tip_top = new ImVec2(canvas_x + canvas_w * 0.5f, top);
//            ImVec2 tip_right = new ImVec2(right, center_y);
//            ImVec2 tip_bottom = new ImVec2(canvas_x + canvas_w * 0.5f, bottom);
//
//            var v = ImGuiUtils.add(new ImVec2(left, top), new ImVec2(0, rounding));
//            drawList.pathLineTo(v.x, v.y);
//            var v2 = ImGuiUtils.add(new ImVec2(left, top), new ImVec2(rounding, 0));
//            drawList.pathBezierCubicCurveTo(
//                    left, top,
//                    left, top,
//                    v2.x, v2.y
//            );
//            drawList.pathLineTo(tip_top.x, tip_top.y);
//            var v3 = ImGuiUtils.mul(ImGuiUtils.sub(ImGuiUtils.add(tip_top, tip_right), tip_top), tip_round);
//
//            drawList.pathLineTo(v3.x, v3.y);
//
//            var v4 = ImGuiUtils.mul(ImGuiUtils.sub(ImGuiUtils.add(tip_bottom, tip_right), tip_bottom), tip_round);
//            drawList.pathBezierCubicCurveTo(
//                    tip_right.x, tip_right.y,
//                    tip_right.x, tip_right.y,
//                    v4.x, v4.y
//            );
//            drawList.pathLineTo(tip_bottom.x, tip_bottom.y);
//            var v5 = ImGuiUtils.add(new ImVec2(left, bottom), new ImVec2(rounding, 0));
//            drawList.pathLineTo(v5.x, v5.y);
//            var v6 = ImGuiUtils.sub(new ImVec2(left, bottom), new ImVec2(0, rounding));
//            drawList.pathBezierCubicCurveTo(
//                    left, bottom,
//                    left, bottom,
//                    v6.x, v6.y
//            );
//
//            if (!filled) {
////                if ((innerColor & 0xFF000000) != 0) {
////                    drawList.addConvexPolyFilled(drawList.path, drawList.getPath().getSize(), innerColor);
////                }
//
//                //drawList.pathStroke(color, true, 2.0f * outline_scale);
//            } else {
//                drawList.pathFillConvex(color);
//            }
        } else {
            float triangleStart = rect_center_x + 0.32f * rect_w;

            int rect_offset = -(int) (rect_w * 0.25f * 0.25f);

            rect.min.x += rect_offset;
            rect.max.x += rect_offset;
            rect_x += rect_offset;
            rect_center_x += rect_offset * 0.5f;
            rect_center.x += rect_offset * 0.5f;

            if (type == IconType.Circle) {
                ImVec2 c = rect_center;

                if (!filled) {
                    float r = 0.5f * rect_w / 2.0f - 0.5f;

                    if ((innerColor & 0xFF000000) != 0) {
                        drawList.addCircleFilled(c.x, c.y, r, innerColor, 12 + extra_segments);
                    }
                    drawList.addCircle(c.x, c.y, r, color, 12 + extra_segments, 2.0f * outline_scale);
                } else {
                    drawList.addCircleFilled(c.x, c.y, 0.5f * rect_w / 2.0f, color, 12 + extra_segments);
                }
            }

            if (type == IconType.Square) {
                if (filled) {
                    float r = 0.5f * rect_w / 2.0f;
                    ImVec2 p0 = ImGuiUtils.sub(rect_center, new ImVec2(r, r));
                    ImVec2 p1 = ImGuiUtils.add(rect_center, new ImVec2(r, r));

                    drawList.addRectFilled(p0.x, p0.y, p1.x, p1.y, color, 0, ImDrawFlags.RoundCornersAll);
                } else {
                    float r = 0.5f * rect_w / 2.0f - 0.5f;
                    ImVec2 p0 = ImGuiUtils.sub(rect_center, new ImVec2(r, r));
                    ImVec2 p1 = ImGuiUtils.add(rect_center, new ImVec2(r, r));

                    if ((innerColor & 0xFF000000) != 0) {
                        drawList.addRectFilled(p0.x, p0.y, p1.x, p1.y, innerColor, 0, ImDrawFlags.RoundCornersAll);
                    }
                }
            }

            if (type == IconType.Grid) {
                float r = 0.5f * rect_w / 2.0f;
                float w = (float) Math.ceil(r / 3.0f);

                ImVec2 baseTl = new ImVec2((float) Math.floor(rect_center_x - w * 2.5f), (float) Math.floor(rect_center_y - w * 2.5f));
                ImVec2 baseBr = new ImVec2((float) Math.floor(baseTl.x + w), (float) Math.floor(baseTl.y + w));

                ImVec2 tl = baseTl;
                ImVec2 br = baseBr;
                for (int i = 0; i < 3; ++i) {
                    tl.x = baseTl.x;
                    br.x = baseBr.x;
                    drawList.addRectFilled(tl.x, tl.y, br.x, br.y, color);
                    tl.x += w * 2;
                    br.x += w * 2;
                    if (i != 1 || filled)
                        drawList.addRectFilled(tl.x, tl.y, br.x, br.y, color);
                    tl.x += w * 2;
                    br.x += w * 2;
                    drawList.addRectFilled(tl.x, tl.y, br.x, br.y, color);

                    tl.y += w * 2;
                    br.y += w * 2;
                }

                triangleStart = br.x + w + 1.0f / 24.0f * rect_w;
            }

            if(type == IconType.RoundSquare){
                if (filled) {
                    float r = 0.5f * rect_w / 2.0f;
                    float cr = r * 0.5f;
                    ImVec2 p0 = new ImVec2(rect_center.x - r, rect_center.y - r);
                    ImVec2 p1 = new ImVec2(rect_center.x + r, rect_center.y + r);

                    drawList.addRectFilled(p0.x, p0.y, p1.x, p1.y, color, cr, 15);

                } else {
                    float r = 0.5f * rect_w / 2.0f - 0.5f;
                    float cr = r * 0.5f;
                    ImVec2 p0 = new ImVec2(rect_center.x - r, rect_center.y - r);
                    ImVec2 p1 = new ImVec2(rect_center.x + r, rect_center.y + r);

                    if ((innerColor & 0xFF000000) != 0) {
                        drawList.addRectFilled(p0.x, p0.y, p1.x, p1.y, innerColor, cr, 15);

                    }

                    drawList.addRect(p0.x, p0.y, p1.x, p1.y, color, cr, 15, 2.0f * outline_scale);

                }
            }
            else if(type == IconType.Diamond){
                if (filled) {
                    final float r = 0.607f * rect_w / 2.0f;
                    final ImVec2 c = rect_center;

                    var v1 = ImGuiUtils.add(c, new ImVec2(0, -r));
                    var v2 = ImGuiUtils.add(c, new ImVec2(r, 0));
                    var v3 = ImGuiUtils.add(c, new ImVec2(0, r));
                    var v4 = ImGuiUtils.add(c, new ImVec2(-r, 0));

                    drawList.pathLineTo(v1.x, v1.y);
                    drawList.pathLineTo(v2.x, v2.y);
                    drawList.pathLineTo(v3.x, v3.y);
                    drawList.pathLineTo(v4.x, v4.y);
                    drawList.pathFillConvex(color);
                } else {
                    final float r = 0.607f * rect_w / 2.0f - 0.5f;
                    final ImVec2 c = rect_center;

                    var v1 = ImGuiUtils.add(c, new ImVec2(0, -r));
                    var v2 = ImGuiUtils.add(c, new ImVec2(r, 0));
                    var v3 = ImGuiUtils.add(c, new ImVec2(0, r));
                    var v4 = ImGuiUtils.add(c, new ImVec2(-r, 0));

                    drawList.pathLineTo(v1.x, v1.y);
                    drawList.pathLineTo(v2.x, v2.y);
                    drawList.pathLineTo(v3.x, v3.y);
                    drawList.pathLineTo(v4.x, v4.y);

//                    if ((innerColor & 0xFF000000) != 0) {
//                        drawList.addConvexPolyFilled(drawList._Path.Data, drawList._Path.Size, innerColor);
//                    }
//
//                    drawList.pathStroke(color, true, 2.0f * outline_scale);
                }

            }
            else{
                float triangleTip = triangleStart + rect_w * (0.45f - 0.32f);

                var v1 = new ImVec2((float) Math.ceil(triangleTip), rect_y + rect_h * 0.5f);
                var v2 = new ImVec2(triangleStart, rect_center_y + 0.15f * rect_h);
                var v3 = new ImVec2(triangleStart, rect_center_y - 0.15f * rect_h);
                drawList.addTriangleFilled(
                        v1.x, v1.y,
                        v2.x, v2.y,
                        v3.x, v3.y,
                        color);
            }
        }
    }
}
