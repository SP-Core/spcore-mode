package spcore.imgui.nodes.enums;

import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.imgui.nodes.types.guides.Guide;
import spcore.imgui.nodes.types.guides.GuideLink;
import spcore.imgui.nodes.types.guides.GuideSize;
import spcore.imgui.nodes.types.literal.ColorNodeType;
import spcore.imgui.nodes.types.literal.IntNodeType;
import spcore.imgui.nodes.types.literal.TextNodeType;
import spcore.imgui.nodes.types.literal.VectorNodeType;
import spcore.imgui.nodes.types.math.RandomNodeType;
import spcore.imgui.nodes.types.render.*;
import spcore.imgui.nodes.types.math.Float.*;
import spcore.imgui.nodes.types.math.Int.*;
import spcore.imgui.nodes.types.math.Vector2.*;
import spcore.imgui.nodes.types.math.Vector3.*;
import spcore.imgui.nodes.types.storage.StorageResetNodeType;
import spcore.imgui.nodes.types.storage.StorageValueNodeType;
import spcore.imgui.nodes.types.typeScript.OutFloatGetterNodeType;
import spcore.imgui.nodes.types.typeScript.OutSetterNodeType;
import spcore.imgui.nodes.types.typeScript.OutWrapper;
import spcore.imgui.nodes.types.variables.ScreenNodeType;
import spcore.imgui.nodes.types.view.*;

import java.util.HashMap;

public enum NodeType {
    AddFloat("Add float", NodeCategory.Math, new AddFloatMathNode()),
    DivFloat("Div float", NodeCategory.Math, new DivFloatMathNode()),
    MulFloat("Mul float", NodeCategory.Math, new MulFloatMathNode()),

    AddInt("Add int", NodeCategory.Math, new AddIntMathNode()),
    DivInt("Div int", NodeCategory.Math, new DivIntMathNode()),
    MulInt("Mul int", NodeCategory.Math, new MulIntMathNode()),

    AddVector2("Add vector2", NodeCategory.Math, new AddVector2MathNode()),
    DivVector2("Div vector2", NodeCategory.Math, new DivVector2MathNode()),

    AddVector3("Add vector3", NodeCategory.Math, new AddVector3MathNode()),
    DivVector3("Div vector3", NodeCategory.Math, new DivVector3MathNode()),

    RandomNodeType("Random", NodeCategory.Math, new RandomNodeType()),

    Render("Render", NodeCategory.Render, new RenderNodeType()),
    ChildRender("Child Render", NodeCategory.Render, new ChildRender()),
    DevRender("Dev Render", NodeCategory.Render, new DevRenderNodeType()),
    ParameterComponent("Parameter Component", NodeCategory.Render, new ParameterComponent()),
    ParameterFloat("Parameter Float", NodeCategory.Render, new ParameterFloat()),
    ParameterVector2("Parameter Vector2", NodeCategory.Render, new ParameterVector2()),
    ParameterVector4("Parameter Vector4", NodeCategory.Render, new ParameterVector4()),

    Color("Color", NodeCategory.Literal, new ColorNodeType()),
    Int("Int", NodeCategory.Literal, new IntNodeType()),
    Vector("Vector", NodeCategory.Literal, new VectorNodeType()),
    Text("Text", NodeCategory.Literal, new TextNodeType()),

    BorderView("BorderView", NodeCategory.View, new BorderViewNodeType()),
    Mix("Mix", NodeCategory.View, new MixNodeType()),
    Repeat("Repeat", NodeCategory.View, new RepeatNodeType()),
    TextView("TextView", NodeCategory.View, new TextViewNodeType()),
    View("View", NodeCategory.View, new ViewNodeType()),
    Image("Image view", NodeCategory.View, new ImageNodeType()),

    Guide("Guide", NodeCategory.Guide, new Guide()),
    GuideLink("Guide Link", NodeCategory.Guide, new GuideLink()),
    GuideSize("Guide Size", NodeCategory.Guide, new GuideSize()),

    StorageReset("Storage Reset", NodeCategory.Storage, new StorageResetNodeType()),
    StorageValue("Storage Value", NodeCategory.Storage, new StorageValueNodeType()),
    ScreenInfo("Screen Info", NodeCategory.Variables, new ScreenNodeType()),

    OutSetter("Out setter", NodeCategory.TypeScript, new OutSetterNodeType()),
    OutFloatGetter("Out float getter", NodeCategory.TypeScript, new OutFloatGetterNodeType()),
    OutWrapper("Out wrapper", NodeCategory.TypeScript, new OutWrapper())
    ;
    public final String name;
    public final NodeCategory category;
    public final AbstractNodeType nodeType;
    private final static HashMap<NodeType, NodeInfo> nodeInfos = new HashMap<>();
    NodeType(String name, NodeCategory category, AbstractNodeType nodeType){
        this.name = name;
        this.category = category;
        this.nodeType = nodeType;
    }


    public static NodeInfo getNodeInfo(NodeType nodeType){
        if(nodeInfos.containsKey(nodeType)){
            return nodeInfos.get(nodeType);
        }
        var nodeInfo = nodeType.nodeType.createInfo(nodeType);
        nodeInfos.put(nodeType, nodeInfo);
        return nodeInfo;
    }


}
