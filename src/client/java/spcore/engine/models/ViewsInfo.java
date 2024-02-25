package spcore.engine.models;

import java.util.List;

public class ViewsInfo {
    public List<View> views;

    public static class View{
        public String name;
        public String path;
        public String blueprint;
    }
}
