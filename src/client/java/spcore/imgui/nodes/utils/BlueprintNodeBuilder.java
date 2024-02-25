package spcore.imgui.nodes.utils;

import imgui.*;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.flag.NodeEditorStyleVar;
import imgui.flag.ImDrawFlags;
import spcore.imgui.nodes.enums.PinKind;
import spcore.imgui.nodes.models.NodeId;
import spcore.imgui.nodes.models.PinId;

public class BlueprintNodeBuilder {
    private int HeaderTextureId;
    private int HeaderTextureWidth;
    private int HeaderTextureHeight;
    private NodeId CurrentNodeId;
    private Stage CurrentStage;
//    private int HeaderColor;
    private ImVec2 NodeMin;
    private ImVec2 NodeMax;
    private ImVec2 HeaderMin;
    private ImVec2 HeaderMax;
    private ImVec2 ContentMin;
    private ImVec2 ContentMax;
    private boolean HasHeader;

    public BlueprintNodeBuilder(int headerTextureId, int textureWidth, int textureHeight){
        this.CurrentNodeId = new NodeId(0);
        this.CurrentStage = Stage.Invalid;
        this.HasHeader = false;
        this.HeaderTextureId = headerTextureId;
        this.HeaderTextureWidth = textureWidth;
        this.HeaderTextureHeight = textureHeight;
    }


    public void header(){
        SetStage(Stage.Header);
    }

    public void endHeader(){
        SetStage(Stage.Content);
    }

    public void begin(NodeId id){
        this.HasHeader = false;
        HeaderMin = HeaderMax = new ImVec2();
        NodeEditor.pushStyleVar(NodeEditorStyleVar.NodePadding,
                8, 4, 8, 8);



        NodeEditor.beginNode(id.value);

        ImGui.pushID(id.value);
        CurrentNodeId = id;
        SetStage(Stage.Begin);
    }

    public void End() {
        SetStage(Stage.End);

        NodeEditor.endNode();

        if (ImGui.isItemVisible()) {
            int alpha = (int) (255 * ImGui.getStyle().getAlpha());

            ImDrawList drawList = NodeEditor.getNodeBackgroundDrawList(CurrentNodeId.value);


            float halfBorderWidth = NodeEditor.getStyle().getNodeBorderWidth() * 0.5f;

//            int headerColor = ImColor.floatToColor(0, 0, 0, alpha) | (HeaderColor & ImColor.floatToColor(255, 255, 255, 0));
            if ((HeaderMax.x > HeaderMin.x) && (HeaderMax.y > HeaderMin.y)) {
//                ImVec2 uv = new ImVec2(
//                        (HeaderMax.x - HeaderMin.x) / (float) (4.0f * HeaderTextureWidth),
//                        (HeaderMax.y - HeaderMin.y) / (float) (4.0f * HeaderTextureHeight));
//
//
//                var v1 = ImGuiUtils.sub(HeaderMin, new ImVec2(8 - halfBorderWidth, 4 - halfBorderWidth));
//                var v2 = HeaderMax.plus(new ImVec2(8 - halfBorderWidth, 0));
//                var v3 = new ImVec2(0.0f, 0.0f);
//                drawList.addImageRounded(HeaderTextureId,
//                        v1.x, v1.y,
//                        v2.x, v2.y,
//                        v3.x, v3.y, uv.x, uv.y,
//                        headerColor, NodeEditor.getStyle().getNodeRounding(), ImDrawFlags.RoundCornersTop);

                if (ContentMin.y > HeaderMax.y) {
                    var p1 = new ImVec2(HeaderMin.x - (8 - halfBorderWidth), HeaderMax.y - 0.5f);
                    var p2 = new ImVec2(HeaderMax.x + (8 - halfBorderWidth), HeaderMax.y - 0.5f);

                    drawList.addLine(p1.x, p1.y, p1.x, p2.y, ImColor.floatToColor(255, 255, 255, 96 * alpha / (3 * 255)), 1.0f);
                }
            }
        }

        CurrentNodeId = new NodeId(0);

        ImGui.popID();

        NodeEditor.popStyleVar(1);

        SetStage(Stage.Invalid);
    }

    public void Input(PinId id) {
        if (CurrentStage == Stage.Begin)
            SetStage(Stage.Content);

        SetStage(Stage.Input);


        Pin(id, PinKind.Input);
    }


    private void Pin(PinId id, PinKind kind){

        NodeEditor.beginPin(id.value, kind.value);
    }

    public void EndInput() {
        NodeEditor.endPin();
    }

    public void Middle() {
        if (CurrentStage == Stage.Begin)
            SetStage(Stage.Content);

        SetStage(Stage.Middle);
    }

    public void Output(PinId id) {
        if (CurrentStage == Stage.Begin)
            SetStage(Stage.Content);

        SetStage(Stage.Output);


        Pin(id, PinKind.Output);
    }

    public void EndOutput() {
        NodeEditor.endPin();
    }

    public boolean SetStage(Stage stage) {
        if (stage == CurrentStage)
            return false;

        Stage oldStage = CurrentStage;
        CurrentStage = stage;

        ImVec2 cursor;
        switch (oldStage) {
            case Begin:
                break;

            case Content:
                break;

            case Input:
                NodeEditor.popStyleVar(2);


                // #debug
                // ImGui.GetWindowDrawList().AddRect(
                //     ImGui.GetItemRectMin(), ImGui.GetItemRectMax(), IM_COL32(255, 0, 0, 255));

                break;

            case Middle:
//                ImGui.EndVertical();

                // #debug
                // ImGui.GetWindowDrawList().AddRect(
                //     ImGui.GetItemRectMin(), ImGui.GetItemRectMax(), IM_COL32(255, 0, 0, 255));

                break;

            case Output:
                NodeEditor.popStyleVar(2);

//                ImGui.Spring(1, 0);
//                ImGui.EndVertical();

                // #debug
                // ImGui.GetWindowDrawList().AddRect(
                //     ImGui.GetItemRectMin(), ImGui.GetItemRectMax(), IM_COL32(255, 0, 0, 255));

                break;

            case End:
                break;

            case Invalid:
                break;
        }
        switch (stage) {
            case Begin:
//                ImGui.BeginVertical("node");
                break;


            case Content:
//                if (oldStage == Begin)
//                    ImGui.Spring(0);
//
//                ImGui.BeginHorizontal("content");
//                ImGui.Spring(0, 0);
                break;

            case Input:
//                ImGui.BeginVertical("inputs", new ImVec2(0, 0), 0.0f);


                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotAlignment, 0, 0.5f);
                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotSize, 0, 0);

//                if (!HasHeader)
//                    ImGui.Spring(1, 0);
                break;

            case Middle:
//                ImGui.Spring(1);
//                ImGui.BeginVertical("middle", new ImVec2(0, 0), 1.0f);
                break;

            case Output:
//                if (oldStage == Middle || oldStage == Input)
//                    ImGui.Spring(1);
//                else
//                    ImGui.Spring(1, 0);
//                ImGui.BeginVertical("outputs", new ImVec2(0, 0), 1.0f);

                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotAlignment, 1.0f, 0.5f);
                NodeEditor.pushStyleVar(NodeEditorStyleVar.PivotSize, 0, 0);

//                if (!HasHeader)
//                    ImGui.Spring(1, 0);
                break;

            case End:
//                if (oldStage == Input)
//                    ImGui.Spring(1, 0);
//                if (oldStage != Begin)
//                    ImGui.EndHorizontal();
                ContentMin = ImGui.getItemRectMin();
                ContentMax = ImGui.getItemRectMax();

                //ImGui.Spring(0);
//                ImGui.EndVertical();
                NodeMin = ImGui.getItemRectMin();
                NodeMax = ImGui.getItemRectMax();
                break;

            case Invalid:
                break;
        }

        return true;
    }
    enum Stage
    {
        Invalid,
        Header,
        Begin,
        Content,
        Input,
        Output,
        Middle,
        End
    };
}
