package spcore.appapi;

import net.minecraft.client.util.SelectionManager;
import spcore.GlobalContext;
import spcore.api.delegates.Action;
import spcore.api.delegates.Action1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Terminal {

    private final List<String> history = new ArrayList<>();
    private final CommandEngine commandEngine;
    private String currentCommandName;
    private final SelectionManager manager;
    public final Action exitAction;
    public final Action1<String> signAction;
    public Terminal(SelectionManager manager, Action exitAction, Action1<String> signAction) {
        this.commandEngine = new CommandEngine(this);
        this.manager = manager;
        this.exitAction = exitAction;
        this.signAction = signAction;
    }


    public void Exit(){
        exitAction.invoke();
    }

    public void Sign(String title) throws Exception {
        signAction.invoke(title);
    }


    public SelectionManager getManager(){
        return manager;
    }

    public void init(){
        reset();
    }

    public void reset(){
        currentCommandName = null;
        manager.selectAll();
        manager.delete(-1);
        this.manager.insert("$/");
    }
    public void onLineEnd(String page){
        try{
            if(currentCommandName != null){
                reset();
                return;
            }
            var endLine = page.lastIndexOf("\n");
            var startCommand = page.lastIndexOf("$/");
            var allCommand = page.substring(startCommand + 2, endLine);
            currentCommandName = commandEngine.Execute(allCommand);
            if(currentCommandName == null){
                currentCommandName = "unknown";
                manager.insert("Команда не найдена");
            }
        }
        catch (Exception e){
            GlobalContext.LOGGER.error(e.getMessage());
        }

    }

    public void onKeyDown(String page){
        if(!page.startsWith("$/")){
            manager.selectAll();
            manager.delete(-1);
            this.manager.insert("$/");
        }
    }
}
