package spcore.appapi.configuration;

import net.fabricmc.loader.api.FabricLoader;
import spcore.GlobalContext;
import spcore.api.SpCoreApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PATHS {

    private static final Map<String, String> values = getOrCreateValues();

    public static void setPath(String spath){
        Path path = Paths.get(spath);
        var name = path.getFileName();
        values.put(name.toString(), spath);
        Save();
    }

    public static String get(String key){
        return values.get(key);
    }

    private static List<String> GetOrCreatePaths(){
        try {
            Path pathsPath = FabricLoader
                    .getInstance().getGameDir().resolve(".paths");
            if(!Files.exists(pathsPath)){
                Files.writeString(pathsPath, "");
                return new ArrayList<>();
            }
            return Files.readAllLines(pathsPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> getOrCreateValues(){
        var paths = GetOrCreatePaths();
        Map<String, String> values = new LinkedHashMap<>();
        for (var spath: paths) {
            if(spath.equals("")){
                continue;
            }
            Path path = Paths.get(spath);
            var name = path.getFileName();
            values.put(name.toString(), spath);
        }

        return values;
    }

    private static void Save(){
        var builder = new StringBuilder();
        for (var value: values.values()) {
            builder.append(value);
            builder.append("\n");
        }
        var data = builder.toString();

        try {
            Path pathsPath = FabricLoader
                    .getInstance().getGameDir().resolve(".paths");

            Files.writeString(pathsPath, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
