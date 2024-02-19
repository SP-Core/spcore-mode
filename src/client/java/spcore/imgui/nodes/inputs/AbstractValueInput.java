package spcore.imgui.nodes.inputs;

import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;

import java.util.HashMap;

public abstract class AbstractValueInput {

    public static final HashMap<PinType, AbstractValueInput> Inputs;

    public abstract void render(Node node, Pin pin);
    static {
        Inputs = new HashMap<>();
        Inputs.put(PinType.Int, new IntValueInput());
        Inputs.put(PinType.Vector4, new Vector4ValueInput());
        Inputs.put(PinType.Float, new FloatValueInput());
        Inputs.put(PinType.Bool, new BoolValueInput());
        Inputs.put(PinType.String, new StringValueInput());
    }
}
