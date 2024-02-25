package spcore.imgui.nodes.enums;

import java.util.Arrays;
import java.util.List;

public enum NodeCategory {
    Math("math"),
    Render("render"),
    Literal("literal"),
    View("view"),
    Guide("guide"),
    Storage("storage"),
    Variables("variables"),
    TypeScript("type-script"),;
    public final String value;
    private NodeCategory(String value){
        this.value = value;
    }

    public List<NodeType> getTypes(){
        return Arrays.stream(NodeType.values())
                .filter(p -> p.category == this)
                .toList();
    }
}
