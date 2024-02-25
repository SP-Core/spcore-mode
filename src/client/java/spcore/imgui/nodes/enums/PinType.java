package spcore.imgui.nodes.enums;

public enum PinType {
    Component(0),
    Components(1),
    Bool(2),
    Int(3),
    Float(4),
    String(5),
    Object(6),
    Function(7),
    Delegate(8),
    Vector2(9),
    Vector3(10),
    Vector4(11),
    Styles(12),
    TypeScriptSetter(13),
    Parameter(14);
    public final int value;
    private PinType(int value){
        this.value = value;
    }
}
