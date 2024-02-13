package spcore.js;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.client.util.SelectionManager;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import spcore.engine.models.CommandMapItem;

import java.util.Map;

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
