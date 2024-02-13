package spcore.appapi.configuration;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.fabricmc.loader.api.FabricLoader;
import spcore.appapi.models.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnowApplicationManager {

    private static final List<KnownApplication> Applications = getOrCreate();
    private static final List<ApplicationResolveHandler> ResolveEvents = new ArrayList<>();
    private static final List<ApplicationRunner> RunnerEvents = new ArrayList<>();
    private static List<KnownApplication> getOrCreate(){
        try {
            ObjectMapper mapper = new ObjectMapper(new Gson());

            Path pathsPath = getPath();
            if(!Files.exists(pathsPath)){
                var json = mapper.writeValueAsString(new ArrayList<KnownApplication>());

                Files.writeString(pathsPath, json);
                return new ArrayList<>();
            }
            var json =Files.readString(pathsPath);

            return new ArrayList<KnownApplication>(Arrays.stream(mapper.readValue(json, KnownApplication[].class)).toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<KnownApplication> getApplications(){
        return Applications;
    }

    public static void addResolver(ApplicationResolveHandler handler){
        ResolveEvents.add(handler);
    }

    public static void addRunner(ApplicationRunner handler){
        RunnerEvents.add(handler);
    }


    public static void notTrust(SpCoreInfo info){
        if(Applications.stream().anyMatch(p -> p.Hash == info.hashCode())){
            Applications.remove(
                    Applications.stream()
                            .filter(p -> p.Hash == info.hashCode())
                            .findFirst().get()
            );
            Save();
        }
    }

    public static void resolveAndRun(SpCoreInfo info){
        if(Applications.stream().anyMatch(p -> p.Hash == info.hashCode())){
            Run(info);
            return;
        }

        if(resolve(info)){
            var app = new KnownApplication();
            app.Hash = info.hashCode();
            app.Manifest = info;
            Applications.add(app);
            Save();
            Run(info);
        }
    }

    private static boolean resolve(SpCoreInfo coreInfo){
        for (var resolve: ResolveEvents
        ) {
            if(!resolve.invoke(coreInfo)){
                return false;
            }
        }

        return true;
    }

    private static void Run(SpCoreInfo coreInfo){
        for (var runner: RunnerEvents
             ) {
            runner.run(coreInfo);
        }
    }

    public static void Save(){
        ObjectMapper mapper = new ObjectMapper(new Gson());
        var json = mapper.writeValueAsString(Applications);
        Path pathsPath = getPath();

        try {

            Files.writeString(pathsPath, json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getPath(){
       return FabricLoader
                .getInstance().getGameDir().resolve("applications.json");
    }
}
