package spcore.imgui.nodes.enums;

public enum PinKind {
    Input(0),
    Output(1);
    public final int value;
    private PinKind(int value){
        this.value = value;
    }
}
