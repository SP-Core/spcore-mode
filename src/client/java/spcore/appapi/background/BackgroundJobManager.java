package spcore.appapi.background;

import kotlinx.coroutines.Job;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.util.ActionResult;
import spcore.GlobalContext;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.models.JobAppInfo;
import spcore.appapi.models.SpCoreInfo;
import spcore.engine.AppEngine;
import spcore.js.JsRuntime;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BackgroundJobManager {

    private final List<JobAppInfo> Jobs = new ArrayList<>();
    private static final Object _lock = new Object();
    private final static BackgroundJobManager _currentManager;
    public static BackgroundJobManager getInstance(){
        return _currentManager;
    }
    private BackgroundJobManager(){

        UseBlockCallback.EVENT.register((player, world, hand, blockHitResult) -> {
            for(var i = 0; i < Jobs.size(); i++){
                var job = Jobs.get(i);
                synchronized (_lock){
                    try {
                        job.Runtime.putMember("back_currentPlayer", player);
                        job.Runtime.putMember("back_world", world);
                        job.Runtime.putMember("back_hand", hand);
                        job.Runtime.putMember("back_blockHitResult", blockHitResult);

                        var result = job.Runtime.eval("""
                            for(var i = 0; i < module.exports.core.events.use_block_callbacks.length; i++){
                                var currentCallback = module.exports.core.events.use_block_callbacks[i];
                                var result = currentCallback(back_currentPlayer, back_world, back_hand, back_blockHitResult);
                                if(result != ActionResult.PASS){
                                    break;
                                }
                            }
                            """);
                        var actionResult = result.as(ActionResult.class);
                        if(actionResult != null && actionResult != ActionResult.PASS){
                            return actionResult;
                        }
                    } catch (Exception | Error e) {
                        GlobalContext.LOGGER.error(e.getMessage());
                        //Jobs.remove(i);
                    }
                }

            }

            return ActionResult.PASS;
        });
    }

    public boolean InvokeCommand(SelectionManager manager, String commandName, List<String> args){
        for(var i = 0; i < Jobs.size(); i++){
            var job = Jobs.get(i);
            try {
                var commandIndex = job.Runtime
                        .eval("getCommand(module.exports.core.commands, \"" + commandName +"\")");

                if(commandIndex.toString().equals("null")){
                    continue;
                }

                job.Runtime.putMember("back_manager", manager);
                job.Runtime.putMember("back_args", args);

                job.Runtime.eval("module.exports.core.commands["+ commandIndex +"].handler(back_manager, back_args)");
                return true;
            } catch (ScriptException | Error e) {
                GlobalContext.LOGGER.error(e.getMessage());
            }


        }

        return false;
    }

    public JsRuntime register(SpCoreInfo appInfos, boolean disableBackground) throws ScriptException {
        if(Jobs.stream().anyMatch(p -> p.AppInfo.hashCode() == appInfos.hashCode())){
            return Jobs.stream().filter(p -> p.AppInfo.hashCode() == appInfos.hashCode())
                    .findFirst().get().Runtime;
        }

        var job = new JobAppInfo();
        job.Runtime = AppEngine
                .getInstance()
                .createRuntime(AppEngine
                        .getInstance()
                        .getSource(appInfos), appInfos.manifest.host_access);
        job.AppInfo = appInfos;
        if(!disableBackground){
            Jobs.add(job);
        }
        return job.Runtime;
    }

    public void remove(SpCoreInfo appInfos){
        if(Jobs.stream().anyMatch(p -> p.AppInfo.hashCode() == appInfos.hashCode())){
            Jobs.remove(
                    Jobs.stream()
                            .filter(p -> p.AppInfo.hashCode() == appInfos.hashCode())
                            .findFirst().get()
            );
        }
    }

    static{
        _currentManager = new BackgroundJobManager();

        var apps = KnowApplicationManager.getApplications()
                .stream().filter(p -> !p.DisableBackground).toList();
        for (var app: apps.stream().filter(p -> p.Manifest.manifest.lifetime.equals("background")).toList()
             ) {
            try {
                _currentManager.register(app.Manifest, app.DisableBackground);
            } catch (ScriptException e) {
                GlobalContext.LOGGER.error(e.getMessage());
            }
        }
    }

    public List<JobAppInfo> getJobs(){
        return Jobs;
    }
}
