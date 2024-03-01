package spcore.appapi.commands;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.SelectionManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import spcore.GlobalContext;
import spcore.api.SpCoreApi;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.configuration.PATHS;
import spcore.appapi.helpers.PathHelper;
import spcore.appapi.models.SpCoreInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CoreGetCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {

        String functionName;
        if(args.containsKey("1")){
            functionName = args.get("1");
        }
        else{
            return false;
        }

        switch (functionName) {
            case "create" -> createApp(manager, args.get("2"),
                    getPath(args.values()
                    .stream().skip(2).toList()));
            case "publish" -> publish(manager, getPath(args.values()
                    .stream().skip(1).toList()));
            default -> {
                get(manager, functionName);
            }
        }

        return false;
    }

    private String getPath(List<String> values){
        var builder = new StringBuilder();
        for (var p: values
        ) {
            builder.append(p);
        }
        return builder.toString();
    }

    private void get(SelectionManager manager, String pack){
        SpCoreApi.CORE_GET.Get(pack, (c, r) ->{
            if(!r.isSuccessful()){
                manager.selectAll();
                manager.delete(-1);
                manager.insert("\nПроизошла непредвиденная ошибка\n");
                return;
            }


            try {
                assert r.body() != null;
                var inputStream = r.body().byteStream();
                ZipInputStream zipInputStream = new ZipInputStream
                        (new BufferedInputStream(inputStream));

                ZipEntry entry;

                File appsFolder = new File("apps");
                var nodes = pack.split("\\.");

                var p = String.join(".", Arrays.stream(nodes).skip(1).toList());
                var appId = Arrays.stream(p.split(":")).findFirst();
                if(appId.isEmpty()){
                    manager.selectAll();
                    manager.delete(-1);
                    manager.insert("\nОшибка в названии пакета\n");
                    return;
                }
                appsFolder = new File(appsFolder, appId.get());
                Path apps = appsFolder.toPath();
                if(!Files.exists(apps)){
                    Files.createDirectories(apps);
                }
                while ((entry = zipInputStream.getNextEntry()) != null) {

                    Path entryPath = Paths.get(apps.toString(), entry.getName());
                    if (entry.isDirectory()) {
                        Files.createDirectories(entryPath);
                    } else {
                        var par = entryPath.getParent();
                        if(!Files.exists(par)){
                            Files.createDirectories(par);
                        }
                        Files.copy(zipInputStream, entryPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                    zipInputStream.closeEntry();
                }
                zipInputStream.close();

                var absolPath = apps.toAbsolutePath().toString();
                PATHS.setPath(absolPath);

                var spCorePath = PathHelper.combine(absolPath, "spcore.json");
                if(!Files.exists(Path.of(spCorePath))){
                    manager.insert("файл spcore.json не найден");
                    return;
                }
                String content;
                try {
                    content = Files.readString(Path.of(spCorePath));
                } catch (IOException e) {
                    manager.insert("ошибка чтения файла");
                    return;
                }

                Gson gson = new Gson();
                ObjectMapper mapper = new ObjectMapper(gson);
                var spCoreInfo = mapper.readValue(content, SpCoreInfo.class);
                spCoreInfo.absolute = absolPath;
                KnowApplicationManager.resolveAndRun(spCoreInfo);
            } catch (IOException e) {
                GlobalContext.LOGGER.error(e.getMessage());
                manager.selectAll();
                manager.delete(-1);
                manager.insert("\nПроизошла непредвиденная ошибка\n");
            }

        });
    }

    private void publish(SelectionManager manager, String path){

        File zipFile = new File("temp.zip");
        try {
            zipFolder(manager, path, zipFile);
            var body = RequestBody.create
                    (MediaType.parse("application/zip"), zipFile);

            SpCoreApi.CORE_GET.Publish(body, (c, r) ->{
                if(!r.isSuccessful()){
                    manager.selectAll();
                    manager.delete(-1);
                    manager.insert("\nПроизошла непредвиденная ошибка\n");
                    return;
                }

                manager.selectAll();
                manager.delete(-1);
                manager.insert("\nПакет опубликован\n");
            });

        } catch (IOException e) {
            GlobalContext.LOGGER.error(e.getMessage());
            manager.selectAll();
            manager.delete(-1);
            manager.insert("\nПроизошла непредвиденная ошибка\n");
        }



    }

    private void createApp(SelectionManager manager, String packName, String path){
        SpCoreApi.CORE_GET.GetTemplate(packName, (c, r) ->{
            if(!r.isSuccessful()){
                manager.selectAll();
                manager.delete(-1);
                manager.insert("\nПроизошла непредвиденная ошибка\n");
                return;
            }

            assert r.body() != null;
            var inputStream = r.body().byteStream();
            ZipInputStream zipInputStream = new ZipInputStream
                    (new BufferedInputStream(inputStream));

            ZipEntry entry;

            File file1 = new File(path);
            File file2 = new File(file1, packName);
            var outputFolder = file2.toPath();

            try {
                if(!Files.exists(outputFolder)){
                    Files.createDirectories(outputFolder);
                }
                while ((entry = zipInputStream.getNextEntry()) != null) {

                    Path entryPath = Paths.get(outputFolder.toString(), entry.getName());
                    if (entry.isDirectory()) {
                        Files.createDirectories(entryPath);
                    } else {
                        var par = entryPath.getParent();
                        if(!Files.exists(par)){
                            Files.createDirectories(par);
                        }
                        Files.copy(zipInputStream, entryPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                    zipInputStream.closeEntry();
                }
                zipInputStream.close();
                manager.selectAll();
                manager.delete(-1);
                manager.insert("\nШаблон приложения загружен\n");
            }
            catch (IOException e) {
                GlobalContext.LOGGER.error(e.getMessage());
                manager.selectAll();
                manager.delete(-1);
                manager.insert("\nПроизошла непредвиденная ошибка\n");
            }



        });

    }

    private static void zipFolder(SelectionManager manager, String sourceFolderPath, File zipFile) throws IOException {

        var spCorePath = PathHelper.combine(sourceFolderPath, "spcore.json");
        if(!Files.exists(Path.of(spCorePath))){
            manager.insert("файл spcore.json не найден");
            return;
        }

        String content;
        try {
            content = Files.readString(Path.of(spCorePath));
        } catch (IOException e) {
            manager.insert("ошибка чтения файла");
            return;
        }

        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper(gson);
        var spCoreInfo = mapper.readValue(content, SpCoreInfo.class);


        Path sourcePath = Paths.get(sourceFolderPath);
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(Path.of(spCorePath)).toString());
            zos.putNextEntry(zipEntry);
            Files.copy(Path.of(spCorePath), zos);
            zos.closeEntry();
            var exePath = Path.of(PathHelper.combine(sourceFolderPath, spCoreInfo.exe));
            zipEntry = new ZipEntry(sourcePath.relativize(exePath).toString());
            zos.putNextEntry(zipEntry);
            Files.copy(exePath, zos);
            zos.closeEntry();
        }
    }

    @Override
    public String GetDescription() {
        return "менеджер пакетов";
    }
}
