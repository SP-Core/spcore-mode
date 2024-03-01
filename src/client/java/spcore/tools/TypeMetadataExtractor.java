package spcore.tools;

import net.minecraft.Bootstrap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Formatting;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TypeMetadataExtractor {

    private static final List<String> blackList = new ArrayList<>();
    public static void main(String[] args) {

        blackList.add("net.minecraft.entity.mob.MobEntity");
        blackList.add("net.minecraft.client.util.MacWindowUtil");
        blackList.add("net.minecraft.client.gui.screen.option.VideoOptionsScreen");
        blackList.add("net.minecraft.entity.passive.MerchantEntity");
        blackList.add("net.minecraft.entity.passive.AbstractHorseEntity");
        blackList.add("net.minecraft.entity.passive.CamelEntity");
        blackList.add("net.minecraft.village.raid.Raid$Member");
        blackList.add("net.minecraft.world.gen.OreVeinSampler$VeinType");
        blackList.add("net.minecraft.client.toast.Toast$Visibility");
        blackList.add("net.minecraft.world.gen.densityfunction.DensityFunctionTypes$BlendAlpha");
        blackList.add("net.minecraft.entity.passive.MooshroomEntity$Type");
        blackList.add("net.minecraft.item.ArmorMaterials");
        blackList.add("net.minecraft.registry.ServerDynamicRegistryType");
        blackList.add("net.minecraft.world.gen.structure.MineshaftStructure$Type");
        blackList.add("net.minecraft.client.network.ClientDynamicRegistryType");
        blackList.add("net.minecraft.world.gen.densityfunction.DensityFunctionTypes$Wrapping$Type");
        blackList.add("net.minecraft.client.gui.screen.GameModeSelectionScreen$GameModeSelection");
        blackList.add("net.minecraft.world.gen.densityfunction.DensityFunctionTypes$BlendOffset");
        // Настройка списка пакетов для сканирования
        // Получение метаданных всех типов в указанных пакетах
        Reflections reflections = new Reflections("net.minecraft", new SubTypesScanner(false));
        Set<Class<? extends Object>> allTypes = reflections.getSubTypesOf(Object.class);
//        MinecraftClient mc = MinecraftClient.getInstance().player.getStackInHand()
        var builder = new StringBuilder();
        builder.append("// @ts-nocheck\n");
        // Получение публичного API каждого типа
        for (Class<?> type : allTypes) {

            if(blackList.contains(type.getName())){
                continue;
            }
            if(type.getName().contains("net.minecraft.entity.passive")){
                continue;
            }
            if(type.getName().contains("net.minecraft.entity.mob")){
                continue;
            }
            if(type.getName().contains("net.minecraft.entity.boss")){
                continue;
            }
            if(type.getName().contains("net.minecraft.entity.raid")){
                continue;
            }
            System.out.println("Type: " + type.getName());

            builder.append("//" + type.getName());
            builder.append("\n");
            try{
                // Генерация интерфейса для каждого типа на TypeScript
                var code = generateTypeScriptInterface(type);
                builder.append(code);
                builder.append("\n\n");
            }
            catch (Exception e){
                builder.append("//").append(e.getMessage());
                builder.append("\n\n");
            }

        }

        Set<Class<? extends Enum>> allEnums = reflections.getSubTypesOf(Enum.class);

        var i = 0;
        // Получение публичного API каждого типа
        for (Class<? extends Enum> type : allEnums) {

            i++;
            if(blackList.contains(type.getName())){
                continue;
            }
            if(type.getName().contains("net.minecraft.client.realms")){
                continue;
            }
            if(type.getName().contains("net.minecraft.world.gen")){
                continue;
            }
            if(type.getName().contains("net.minecraft.client.recipebook")){
                continue;
            }
            System.out.println(i + "ENUM: " + type.getName());

            builder.append("//" + type.getName());
            builder.append("\n");
            try{
                // Генерация интерфейса для каждого типа на TypeScript
                var code = generateTypeScriptEnum(type);
                builder.append(code);
                builder.append("\n\n");
            }
            catch (Error | Exception error){
                builder.append("//").append(error.getMessage());
                builder.append("\n\n");
            }

        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:\\Users\\jackf\\Desktop\\myApp\\core\\api\\mc\\minecraft-main-bundle.ts"), StandardCharsets.UTF_8))) {
            try {
                writer.write(builder.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateTypeScriptInterface(Class<?> type) {
        if(type.getSimpleName().equals("")){
            return "";
        }

        var t = "class";
        if(type.isInterface()){
            t = "interface";
        }
        StringBuilder interfaceBuilder = new StringBuilder();
        interfaceBuilder.append(t).append(" ")
                .append(ResolveName(type.getName(), type.getSimpleName()))
                .append(" ");

        List<String> exList = new ArrayList<>();
        Class<?> superclass = type.getSuperclass();
        if (superclass != null && superclass != Object.class) {
            exList.add(ResolveName(superclass.getName(), superclass.getSimpleName()));
        }
        else{
            exList.add("JObject");
        }
        for (var r: type.getInterfaces()
             ) {
            exList.add(ResolveName(r.getName(), r.getSimpleName()));
        }
        if(exList.size() > 0){
            interfaceBuilder.append("extends ");
            interfaceBuilder.append(String.join(", ", exList));
        }
        interfaceBuilder
                .append("{")
                .append("\n");

        List<Field> staticFields = new ArrayList<>();
        List<Method> staticMethods = new ArrayList<>();
        for (Method method : type.getDeclaredMethods()) {
            if (java.lang.reflect.Modifier.isPublic(method.getModifiers()) &&
                    !java.lang.reflect.Modifier.isStatic(method.getModifiers())) {

                var m = CreateMethod(method, "");
                interfaceBuilder.append(m);


            }
            else if(java.lang.reflect.Modifier.isStatic(method.getModifiers())){
                staticMethods.add(method);
            }
        }

        for (Field field : type.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isPublic(field.getModifiers()) &&
                    !java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                var f = CreateField(field, "");
                interfaceBuilder.append(f);
            }
            else if(java.lang.reflect.Modifier.isStatic(field.getModifiers())){
                staticFields.add(field);
            }
        }

        interfaceBuilder.append("}");

        if(staticFields.size() != 0 || staticMethods.size() != 0){
            interfaceBuilder
                    .append("\n");
            interfaceBuilder.append("class").append(" ")
                    .append("Static_")
                    .append(ResolveName(type.getName(), type.getSimpleName()))
                    .append(" ");
            interfaceBuilder
                    .append("{")
                    .append("\n");
            for (var method: staticMethods
                 ) {
                var m = CreateMethod(method, "static");
                interfaceBuilder.append(m);
            }

            for (var field: staticFields
            ) {
                var m = CreateField(field, "static");
                interfaceBuilder.append(m);
            }
            interfaceBuilder.append("}");
        }

        return  interfaceBuilder.toString();
    }


    private static String CreateMethod(Method method, String mod){
        var interfaceBuilder = new StringBuilder();
        interfaceBuilder.append(mod);
        interfaceBuilder.append(" ");
        interfaceBuilder.append(method.getName())
                .append("(");

        List<String> pars = new ArrayList<>();
        for (var p: method.getParameters()
        ) {

            pars.add(parName(p.getName()) + ": " + getTypeName(p.getType().getName(), p.getType().getSimpleName()));
        }
        interfaceBuilder.append(String.join(",", pars));
        interfaceBuilder.append("): ")
                .append(getTypeName(method.getReturnType().getName(), method.getReturnType().getSimpleName())).append(";")
                .append("\n");
        return interfaceBuilder.toString();
    }

    private static String CreateField(Field field, String mod){
        String interfaceBuilder = mod +
                " " +
                field.getName() + ": " + getTypeName(field.getType().getName(), field.getType().getSimpleName()) + ";" + "\n";
        return interfaceBuilder;
    }

    private static <T extends Enum<T>> String generateTypeScriptEnum(Class<T> type) {
        if(type.getSimpleName().equals("")){
            return "";
        }
        StringBuilder interfaceBuilder = new StringBuilder();
        interfaceBuilder.append("enum ")
                .append(ResolveName(type.getName(), type.getSimpleName()))
                .append(" {")
                .append("\n");

        T[] enumConstants = type.getEnumConstants();
        for (int i = 0; i < enumConstants.length; i++) {
            var v = enumConstants[i].toString();
            if(v.contains("§")){
                v = v.replace("§", "$");
            }
            else if(v.contains(" ")){
                v = v.replace(" ", "_");
            }
            interfaceBuilder.append("  ").append(v);
            if (i < enumConstants.length - 1) {
                interfaceBuilder.append(",\n");
            } else {
                interfaceBuilder.append("\n");
            }
        }

        interfaceBuilder.append("}");

        return  interfaceBuilder.toString();
    }

    public static String ResolveName(String name, String sample){
        if(name.contains("$")){
            int lastDotIndex = name.lastIndexOf(".");
            String result = name.substring(lastDotIndex + 1);
            return result.replace("$", "_").replace(";", "");
        }

        return sample;
    }

    private static String getTypeName(String name, String sample){
        name = ResolveName(name, sample);
        if(name.equals("String")){
            return "string";
        }
        else if(name.equals("int") || name.equals("float") || name.equals("double")){
            return "number";
        }
        else if(name.equals("int[]") || name.equals("float[]") || name.equals("double[]")){
            return "number[]";
        }
        else if(name.equals("int[][]") || name.equals("float[][]") || name.equals("double[][]")){
            return "number[][]";
        }
        return name;
    }

    private static String parName(String name){
        if(name.equals("function")){
            return "fun";
        }
        return name;
    }
}