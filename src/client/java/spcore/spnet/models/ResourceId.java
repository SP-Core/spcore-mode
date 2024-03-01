package spcore.spnet.models;

public class ResourceId {
    public ResourceId(int value, ResourceType type)
    {
        Value = value;
        Type = type;
    }

    public final int Value;
    public final ResourceType Type;

}
