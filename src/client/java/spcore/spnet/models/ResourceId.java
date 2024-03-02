package spcore.spnet.models;

import java.util.Objects;

public class ResourceId {
    public ResourceId(int value, ResourceType type)
    {
        Value = value;
        Type = type;
    }

    public final int Value;
    public final ResourceType Type;

    @Override
    public int hashCode() {
        return Objects.hash(Value, Type.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == hashCode();
    }

    public String getFileName(){
        String ex = switch (Type) {
            case Sound -> "wav";
            default -> null;
        };
        if(ex == null){
            ex = "data";
        }

        return Value + "." + ex;
    }

}
