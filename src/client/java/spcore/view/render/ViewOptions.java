package spcore.view.render;

public abstract class ViewOptions {

    private boolean isDefault = true;

    public boolean isDefault(){
        return isDefault;
    }

    public void change(){
        isDefault = false;
    }
}
