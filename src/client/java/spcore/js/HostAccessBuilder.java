package spcore.js;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.client.util.SelectionManager;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import spcore.engine.models.CommandMapItem;
import spcore.engine.models.ViewController;

import java.util.Map;
import java.util.Objects;

public class HostAccessBuilder {


    private static HostAccess.Builder create(){
        return HostAccess.newBuilder()
                .allowMutableTargetMappings(HostAccess.MutableTargetMapping.values())
                .targetTypeMapping(Object.class, CommandMapItem.class, o -> {
                    return o instanceof Map;
                }, o -> {
                    var gson = new Gson();
                    var json = gson.toJson(o);
                    return gson.fromJson(json, CommandMapItem.class);
                })
                .targetTypeMapping(Object.class, ViewController.class, o -> {
                    return o instanceof Map;
                }, o -> {
                    var gson = new Gson();
                    var json = gson.toJson(o);
                    return gson.fromJson(json, ViewController.class);
                })
                .targetTypeMapping(Double.class, Float.class, Objects::nonNull, o -> {
                    return (float)(o.doubleValue());
                })
                .targetTypeMapping(Double.class, Integer.class, Objects::nonNull, o -> {
                    return  (int)(o.doubleValue());
                });
    }

    public static HostAccess buildALL(){
        return create().
                allowPublicAccess(true).
                allowAllImplementations(true).
                allowAllClassImplementations(true).
                allowArrayAccess(true).allowListAccess(true).allowBufferAccess(true).
                allowIterableAccess(true).allowIteratorAccess(true).allowMapAccess(true).
                allowAccessInheritance(true)
                .build();
    }

    public static HostAccess buildISOLATED(){
        return create()
                .methodScoping(true)
                .build();
    }


}
