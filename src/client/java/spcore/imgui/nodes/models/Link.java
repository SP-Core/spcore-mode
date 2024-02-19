package spcore.imgui.nodes.models;

import imgui.ImColor;
import spcore.imgui.nodes.models.LinkId;
import spcore.imgui.nodes.models.PinId;

import java.awt.*;
import java.util.Date;

public class Link {
    public LinkId id;
    public PinId startPinId;
    public PinId endPinId;

    public int color_r;
    public int color_g;
    public int color_b;
    public int color_a;

    public long create_time;

    public Link(LinkId id, PinId start, PinId end){
        this.id = id;
        this.startPinId = start;
        this.endPinId = end;
        this.color_r = 255;
        this.color_g = 255;
        this.color_b = 255;
        this.color_a = 255;
        this.create_time = new Date().getTime();
    }

    public long getCreate_time(){
        return create_time;
    }
}
