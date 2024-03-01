package spcore.fabric.resources;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import spcore.GlobalContext;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SpCoreResourceManager implements ResourceManager {

    private final ResourceManager _parent;

    public SpCoreResourceManager(ResourceManager parent) {
        _parent = parent;
    }

    @Override
    public Set<String> getAllNamespaces() {
        var s = _parent.getAllNamespaces();
        s.add("spnet");
        return s;
    }

    @Override
    public List<Resource> getAllResources(Identifier id) {
        if(id.getNamespace().equals("spnet")){
            GlobalContext.LOGGER.info("spnet: " + id.getPath());
            return new ArrayList<>();
        }

        return _parent.getAllResources(id);
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return _parent.findResources(startingPath, allowedPathPredicate);
    }

    @Override
    public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return _parent.findAllResources(startingPath, allowedPathPredicate);
    }

    @Override
    public Stream<ResourcePack> streamResourcePacks() {
        return _parent.streamResourcePacks();
    }

    @Override
    public Optional<Resource> getResource(Identifier id) {
        if(id.getNamespace().equals("spnet")){
            GlobalContext.LOGGER.info("spnet: " + id.getPath());
            return Optional.of(new Resource(new SpNetResourcePack("spnet"), () -> {
                return getStream(id.getPath());
            }, () -> {
                return null;
            }));
        }
        return _parent.getResource(id);
    }

    public InputStream getStream(String path) throws IOException {
        URL url = new URL("https://spcore.ru/spnet/" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        var in = connection.getInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        in.close();
        connection.disconnect();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
