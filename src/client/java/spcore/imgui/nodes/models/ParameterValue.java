package spcore.imgui.nodes.models;

import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;

import java.util.HashMap;

public class ParameterValue<T> {
    public String id;
    public T value;
}
