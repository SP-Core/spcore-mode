package spcore.spnet.resources;

import spcore.GlobalContext;
import spcore.spnet.models.ResourceId;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class SpNetResourceStorage {
    private static final File spNetFolder = new File("spnet");

    public static InputStream Resolve(ResourceId id) throws IOException {
        var file = ResolveIfNeed(id);
        return new FileInputStream(file);
    }

    public static File ResolveIfNeed(ResourceId id) throws IOException {
        if(!spNetFolder.exists()){
            Files.createDirectory(spNetFolder.toPath());
        }
        var file = new File(spNetFolder, id.getFileName());
        if(file.exists()){
            return file;
        }
        URL url = new URL(GlobalContext.API_URL + "spnet/" + id.Type.name() + "/" + id.getFileName());
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }


        return file;
    }
}
