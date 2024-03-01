package spcore.imgui.nodes.providers;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import spcore.GlobalContext;
import spcore.imgui.nodes.NodeProvider;
import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DebugProvider implements NodeProvider {
    private final String viewName;

    private final String path;
    private final String blueprintPath;
    public DebugProvider(String viewName, String path, String blueprintPath) {
        this.viewName = viewName;
        this.path = path;
        this.blueprintPath = blueprintPath;
    }

    @Override
    public String name() {
        return viewName;
    }

    @Override
    public boolean save(ViewFile viewFile) {

        var mapper = new ObjectMapper(new Gson());
        var json = mapper.writeValueAsString(viewFile);
        var mainFolder = new File("studio-views");
        var hashFile = new File(mainFolder, viewName + ".hash");
        try {
            if(Files.exists(hashFile.toPath())){
                var lastHash = Files.readString(hashFile.toPath());
                if(Integer.parseInt(lastHash) == json.hashCode()){
                    return false;
                }
            }

            var viewF = new File(this.path);

            Files.writeString(viewF.toPath(),json);
            Files.writeString(hashFile.toPath(), Integer.toString(json.hashCode()));
        } catch (IOException e) {
            GlobalContext.LOGGER.error(e.getMessage());
        }

        return true;
    }

    @Override
    public ViewFile load() throws IOException {
        var mainFolder = new File("studio-views");
        if(!Files.exists(mainFolder.toPath())){
            Files.createDirectories(mainFolder.toPath());
        }

        var vf = new ViewFile();
        var viewFile = new File(this.path);
        if(!Files.exists(viewFile.toPath())){
            vf.nodes = new ArrayList<>();
            vf.links = new ArrayList<>();
            vf.lastId = 1;
            save(vf);
            return vf;
        }

        var json = Files.readString(viewFile.toPath());
        var mapper = new ObjectMapper(new Gson());
        vf = mapper.readValue(json, ViewFile.class);

        var hashFile = new File(mainFolder, viewName + ".hash");
        Files.writeString(hashFile.toPath(), Integer.toString(json.hashCode()));

        return vf;
    }

    @Override
    public String getBlueprintPath() {
        return blueprintPath;
    }
}
