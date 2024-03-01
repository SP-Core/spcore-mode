package spcore.imgui.nodes;

import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.nodeditor.NodeEditor;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTabBarFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImLong;
import imgui.type.ImString;
import spcore.GlobalContext;
import spcore.imgui.ImGuiImpl;
import spcore.imgui.nodes.enums.*;
import spcore.imgui.nodes.models.*;
import spcore.imgui.nodes.types.*;
import spcore.imgui.nodes.types.guides.Guide;
import spcore.imgui.nodes.types.guides.GuideLink;
import spcore.imgui.nodes.types.guides.GuideSize;
import spcore.imgui.nodes.types.literal.ColorNodeType;
import spcore.imgui.nodes.types.literal.IntNodeType;
import spcore.imgui.nodes.types.literal.TextNodeType;
import spcore.imgui.nodes.types.literal.VectorNodeType;
import spcore.imgui.nodes.types.math.Float.AddFloatMathNode;
import spcore.imgui.nodes.types.math.Float.DivFloatMathNode;
import spcore.imgui.nodes.types.math.Float.MulFloatMathNode;
import spcore.imgui.nodes.types.math.Int.AddIntMathNode;
import spcore.imgui.nodes.types.math.Int.DivIntMathNode;
import spcore.imgui.nodes.types.math.Int.MulIntMathNode;
import spcore.imgui.nodes.types.math.RandomNodeType;
import spcore.imgui.nodes.types.math.Vector2.AddVector2MathNode;
import spcore.imgui.nodes.types.math.Vector2.DivVector2MathNode;
import spcore.imgui.nodes.types.math.Vector3.AddVector3MathNode;
import spcore.imgui.nodes.types.math.Vector3.DivVector3MathNode;
import spcore.imgui.nodes.types.render.DevRenderNodeType;
import spcore.imgui.nodes.types.render.RenderNodeType;
import spcore.imgui.nodes.types.renders.AbstractNodeRender;
import spcore.imgui.nodes.types.renders.BlueprintNodeRender;
import spcore.imgui.nodes.types.storage.StorageResetNodeType;
import spcore.imgui.nodes.types.storage.StorageValueNodeType;
import spcore.imgui.nodes.types.variables.ScreenNodeType;
import spcore.imgui.nodes.types.view.*;
import spcore.imgui.nodes.utils.ImGuiUtils;
import spcore.imgui.nodes.utils.WidgetsUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeRender {

    public final NodeContext context;
    private final HashMap<NodeId, Float> m_NodeTouchTime = new HashMap<>();
    private final Float m_TouchTime = 1.0f;

    private final ImLong contextNodeId = new ImLong(0);
    private final ImLong contextLinkId = new ImLong(0);
    private final ImLong contextPinId = new ImLong(0);

    private boolean createNewNode = false;
    private Pin newNodeLinkPin;
    public Pin newLinkPin;
    private final float leftPaneWidth = 400;
    private final float rightPaneWidth = 800;

    private final BlueprintNodeRender nodeRender = new BlueprintNodeRender();

    private final List<NodeTab> tabs = new ArrayList<>();
    public NodeRender(NodeContext context) {
        this.context = context;
        this.tabs.add(new NodeTab("main", "fefeef"));
        this.tabs.add(new NodeTab("main2", "fefeef2"));
    }

    public Node spawn(Node node){
        if(context.nodes.stream().anyMatch(p -> p.id.value == node.id.value)){
            return node;
        }
        context.nodes.add(node);
        BuildNode(node);
        return node;
    }

    public void remove(int nodeId){
        var dNode = context.nodes
                .stream().filter(p -> p.id.value == nodeId)
                .findAny();

        dNode.ifPresent(context.nodes::remove);

        var idInputs = dNode.get()
                .inputs.stream().map(p -> p.id).toList();
        var idOutputs = dNode.get()
                .outputs.stream().map(p -> p.id).toList();


        try{
            for(var i = 0; i < context.links.size(); i++){
                var link = context.links.get(i);
                var v1 = idInputs.stream().anyMatch(z -> z.value == link.endPinId.value || z.value == link.startPinId.value);
                var v2 = idOutputs.stream().anyMatch(z -> z.value == link.endPinId.value || z.value == link.startPinId.value);

                if(v1 || v2){
                    context.links.remove(i);
                }

            }
        }
        catch (Exception e){
            GlobalContext.LOGGER.error(e.getMessage());
        }


    }

    private void BuildNode(Node node){
        node.init();
        for (var input: node.inputs
        ) {
            input.nodeId = node.id;
        }

        for (var output: node.outputs
        ) {
            output.nodeId = node.id;
        }
    }



    public void render(){
        UpdateTouch();
        NodeEditor.setCurrentEditor(context.context);
        NodeEditor.begin("Node editor");
        {
            nodeRender.init(this);
            var cursorTopLeft = ImGui.getCursorScreenPos();


            for (var node: context.nodes
            ) {
                nodeRender.render(node);
            }

            for (var link: context.links
            ) {
                NodeEditor.link(link.id.value, link.startPinId.value, link.endPinId.value, link.color_r, link.color_g, link.color_b, link.color_a, 2.0f);
            }

            if(!createNewNode){

                if(NodeEditor.beginCreate(255, 255, 255, 255, 2)){
                    ImLong startPinId = new ImLong(0);
                    ImLong endPinId = new ImLong(0);
                    if(NodeEditor.queryNewLink(startPinId, endPinId)){
                        Pin startPin = FindPin((int)startPinId.get());
                        Pin endPin = FindPin((int)endPinId.get());

                        newLinkPin = startPin != null ? startPin : endPin;
                        if(endPin.innerPin.kind == PinKind.Input){
                            var s = startPin;
                            startPin = endPin;
                            endPin = s;
                            var s2 = startPinId;
                            startPinId = endPinId;
                            endPinId = s2;
                        }
                        if(startPin != null && endPin != null){
                            if(startPin == endPin){
                                NodeEditor.rejectNewItem(255, 0, 0, 255, 2.0f);
                            }
                            else if(endPin.innerPin.kind == startPin.innerPin.kind){
                                showLabel("x Incompatible Pin Kind", ImColor.floatToColor(45, 32, 32, 180));
                                NodeEditor.rejectNewItem(255, 0, 0, 255, 2.0f);
                            }
                            else if(endPin.type != startPin.type){
                                showLabel("x Incompatible Pin Type", ImColor.floatToColor(45, 32, 32, 180));
                                NodeEditor.rejectNewItem(255, 0, 0, 255, 1.0f);
                            }
                            else{
                                showLabel("+ Create Link", ImColor.floatToColor(32, 45, 32, 180));
                                if(NodeEditor.acceptNewItem(128, 255, 128, 255, 4.0f)){
                                    var l = new Link(new LinkId(context.nextId()), startPin.id, endPin.id);
                                    var lcolor = GetIconColor(startPin.type);

                                    l.color_a = lcolor.getAlpha();
                                    l.color_r = lcolor.getRed();
                                    l.color_b = lcolor.getBlue();
                                    l.color_g = lcolor.getGreen();

                                    context.links.add(l);
                                }
                            }
                        }
                    }
                }
                else{
                    newLinkPin = null;
                }

                NodeEditor.endCreate();



                if(NodeEditor.beginDelete()){
                    ImLong nodeId = new ImLong(0);
                    while (NodeEditor.queryDeletedNode(nodeId)){
                        remove((int) nodeId.get());
                    }
                    ImLong linkId = new ImLong(0);
                    while (NodeEditor.queryDeletedLink(linkId, new ImLong(0), new ImLong(0))){
                        var dLink = context.links
                                .stream().filter(p -> p.id.value == linkId.get())
                                .findAny();

                        dLink.ifPresent(context.links::remove);
                    }
                }
                NodeEditor.endDelete();
            }
            ImGui.setCursorScreenPos(cursorTopLeft.x, cursorTopLeft.y);
        }


        var openPopupPosition = ImGui.getMousePos();
        NodeEditor.suspend();
        if(NodeEditor.showNodeContextMenu(contextNodeId)){
            ImGui.openPopup("Node Context Menu");
        }
        else if(NodeEditor.showPinContextMenu(contextPinId)){
            ImGui.openPopup("Pin Context Menu");
        }
        else if(NodeEditor.showLinkContextMenu(contextLinkId)){
            ImGui.openPopup("Link Context Menu");
        }
        else if(NodeEditor.showBackgroundContextMenu()){
            ImGui.openPopup("Create New Node");
            newNodeLinkPin = null;
        }
        NodeEditor.resume();
        NodeEditor.suspend();

        //ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 8, 8);
        if(ImGui.beginPopup("Node Context Menu")){
            Node node = FindNode(contextNodeId.get());
            ImGui.textUnformatted("Node Context Menu");
            ImGui.separator();
            if(node != null){
                ImGui.text("ID: " + node.id.value);
                ImGui.text("Type: " + "Blueprint");
                ImGui.text("Inputs: " + node.inputs.size());
                ImGui.text("Outputs: " + node.outputs.size());
            }
            else{
                ImGui.text("Unknown node: " + contextNodeId.get());
            }

            ImGui.separator();
            if(ImGui.menuItem("Delete")){
                NodeEditor.deleteNode(contextNodeId.get());
            }
            ImGui.endPopup();
        }

        if(ImGui.beginPopup("Create New Node")){
            var newNodePostion = openPopupPosition;
            Node node = null;

            for (var category: NodeCategory.values()
            ) {

                if(ImGui.beginMenu(category.value)){
                    for (var nodeType: category.getTypes()
                    ) {
                        if(ImGui.menuItem(nodeType.name)){
                            node = spawn(nodeType.nodeType.create(NodeType.getNodeInfo(nodeType), context));
                        }
                    }
                    ImGui.endMenu();
                }


            }

            if(node != null){
                BuildNodes();

                createNewNode = false;
                NodeEditor.setNodePosition(node.id.value, newNodePostion.x, newNodePostion.y);

                var startPin = newNodeLinkPin;
                if(startPin != null){
                    var pins = startPin.innerPin.kind == PinKind.Input ? node.inputs : node.outputs;

                    for (var pin: pins
                    ) {

                        if(CanCreateLink(startPin, pin)){
                            var endPin = pin;
                            if(startPin.innerPin.kind == PinKind.Input){
                                var v = startPin;
                                startPin = endPin;
                                endPin = v;
                            }

                            var link = new Link(new LinkId(context.nextId()), startPin.id, endPin.id);
                            var lcolor = GetIconColor(startPin.type);
                            link.color_a = lcolor.getAlpha();
                            link.color_r = lcolor.getRed();
                            link.color_b = lcolor.getBlue();
                            link.color_g = lcolor.getGreen();
                            context.links.add(link);
                            break;

                        }

                    }
                }
            }

            ImGui.endPopup();
        }
        else{
            createNewNode = false;
        }

        NodeEditor.resume();

        NodeEditor.end();

    }

    public ImString createView = new ImString("", 20);
    public boolean showCreateView = false;


    public void showLabel(String label, int color){
        ImGui.setCursorPosY(ImGui.getCursorPosY() - ImGui.getTextLineHeight());
        var size = ImGui.calcTextSize(label);
        var padding = ImGui.getStyle().getFramePadding();
        var spacing = ImGui.getStyle().getItemSpacing();

        var cp = ImGuiUtils.add(ImGui.getCursorPos(), new ImVec2(spacing.x, -spacing.y));

        ImGui.setCursorPos(cp.x, cp.y);

        var rectMin = ImGuiUtils.min(ImGui.getCursorScreenPos(), padding);
        var rectMax = ImGuiUtils.add(ImGui.getCursorScreenPos(), ImGuiUtils.add(size, padding));
        var drawList = ImGui.getWindowDrawList();
        drawList.addRectFilled(rectMin.x, rectMin.y, rectMax.x, rectMax.y, color, size.y * 0.15f);
        ImGui.textUnformatted(label);
    }

    void UpdateTouch()
    {
        var deltaTime = ImGui.getIO().getDeltaTime();
        for (var entry : m_NodeTouchTime.entrySet())
        {
            if (entry.getValue() > 0.0f)
                entry.setValue(entry.getValue() - deltaTime);
        }
    }

    public boolean CanCreateLink(Pin a, Pin b)
    {
        return a != null && b != null && a != b && a.innerPin.kind != b.innerPin.kind && a.type == b.type && a.nodeId != b.nodeId;
    }

    Color GetIconColor(PinType type)
    {
        switch (type)
        {
            case Component:     return new Color(245, 222, 179);
            case Components:     return new Color(236, 176, 64);
            case Styles:     return new Color(48, 28, 171);
            case Bool:     return new Color(220,  48,  48);
            case Int:      return new Color( 68, 201, 156);
            case Float:    return new Color(147, 226,  74);
            case String:   return new Color(41, 56, 196);
            case Object:   return new Color( 51, 150, 215);
            case Function: return new Color(218,   0, 183);
            case Delegate: return new Color(255,  48,  48);
            case TypeScriptSetter: return new Color(138, 14, 14);
            case Vector2: return new Color(140, 63, 168);
            case Vector3: return new Color(140, 63, 168);
            case Vector4: return new Color(140, 63, 168);
            case Parameter: return new Color(56, 69, 215);
            default:
        }

        return null;
    }


    public void DrawPinIcon(Pin pin, boolean connected, int alpha)
    {
        IconType iconType;
        Color color = GetIconColor(pin.type);
//        color.Value.w = alpha / 255.0f;
        switch (pin.type)
        {
            case Component:     iconType = IconType.Flow;   break;
            case Components:     iconType = IconType.Flow;   break;
            case Styles:     iconType = IconType.Circle;   break;
            case Bool:     iconType = IconType.Circle; break;
            case Int:      iconType = IconType.Circle; break;
            case Float:    iconType = IconType.Circle; break;
            case String:   iconType = IconType.Circle; break;
            case Object:   iconType = IconType.Circle; break;
            case Function: iconType = IconType.Circle; break;
            case Delegate: iconType = IconType.Square; break;
            case TypeScriptSetter: iconType = IconType.Square; break;
            case Vector2: iconType = IconType.Circle; break;
            case Vector3: iconType = IconType.Circle; break;
            case Vector4: iconType = IconType.Circle; break;
            case Parameter: iconType = IconType.Circle; break;
            default:
                return;
        }


        WidgetsUtils.Icon(new ImVec2(24.0f, 24.0f), iconType, connected, color.getRGB(), ImColor.floatToColor(32, 32, 32, alpha));
    }

    public boolean IsPinLinked(PinId id)
    {
        if (id == null)
            return false;

        for (var link : context.links)
        if (link.startPinId == id || link.endPinId == id)
            return true;

        return false;
    }

    Node FindNode(long id)
    {
        for (var node : context.nodes)
            if (node.id.value == id)
                return node;

        return null;
    }

    Pin FindPin(int id)
    {
        for (var node : context.nodes)
        {
            for (var pin : node.inputs)
                if (pin.id.value == id)
                    return pin;

            for (var pin : node.outputs)
                if (pin.id.value == id)
                    return pin;
        }

        return null;
    }


    void BuildNodes()
    {
        for (var node : context.nodes){
            BuildNode(node);
        }
    }
//    static boolean Splitter(boolean split_vertically, float thickness, Float size1, Float size2, float min_size1, float min_size2, float splitter_long_axis_size)
//    {
//        var g = ImGui.getCurrentContext();
//        var id = ImGui.getID("##Splitter");
//        ImRect bb = new ImRect();
//
//        var pos = ImGui.getCursorPos();
//        ImVec2 min;
//        ImVec2 max;
//        if(split_vertically){
//            min = new ImVec2(pos.x + size1, pos.y);
//        }
//        else{
//            min = new ImVec2(pos.x, pos.y + size2);
//        }
//
//        if(split_vertically){
//            max = new ImVec2(min.x + thickness, min.y + splitter_long_axis_size);
//        }
//        else{
//            max = new ImVec2(min.x + splitter_long_axis_size, min.y + thickness);
//
//        }
//        pos = new ImVec2();
//        if()
//
//        bb.min =  pos;
//        bb.max = bb.Min + CalcItemSize(split_vertically ? ImVec2(thickness, splitter_long_axis_size) : ImVec2(splitter_long_axis_size, thickness), 0.0f, 0.0f);
//        return SplitterBehavior(bb, id, split_vertically ? ImGuiAxis_X : ImGuiAxis_Y, size1, size2, min_size1, min_size2, 0.0f);
//    }
}
