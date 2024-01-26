package com.example.commands;

import com.example.SpCore;
import com.example.commands.commandEngine.CommandEngine;
import com.example.mixin.client.BookScreenMixin;
import com.example.spcore.SpCoreApi;
import com.example.spcore.models.DocumentPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.util.SelectionManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PatternCommand extends BaseCommand{

    public String CurrentCommand = null;
    public HashMap<String, String> CurrentArgs = null;
    public DocumentPattern CurrentPattern = null;
    public boolean currentOpenedPattern = false;
    private final Pattern varPattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
    private final Pattern globalRegex = Pattern.compile("Заполните\\s*\\{\\s*((?:\\s*.+?\\s*\\n*:\\s*.+?\\s*\\n*)*)}\\s*\\nsave:|\\s*(\\*)");
    private final Pattern innerRegex = Pattern.compile("([^\\s:]+): ([^\\n\\r]+)");


    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        CurrentArgs = args;
        CurrentCommand = null;
        var size = args.entrySet().size();
        if(size == 0){
            Help(manager);
            CurrentCommand = "help";
            return false;
        }

        if(size == 1){
            manager.insert("требуется название шаблона");
            return false;
        }

        if(size == 2){
            var name = args.get("1");
            var patternName = args.get("2");

            if(name.equals("open")){
                CurrentCommand = "open";
                return Open(manager, patternName);
            }
            else if(name.equals("fill")){
                CurrentCommand = "fill";
                return Fill(manager, patternName);
            }
            else if(name.equals("put")){
                CurrentCommand = "put";
                return Put(manager, patternName);
            }
        }
        manager.insert("такой команды не существует");
        return false;
    }

    public void Help(SelectionManager manager){
        String message = """
                put {name}: создать или обновить шаблон
                open {name}: открыть шаблон
                fill {name}: заполнить шаблон""";

        manager.insert(message);
    }


    public boolean Put(SelectionManager manager, String patternName){
        List<DocumentPattern.Field> fields = new ArrayList<>();

        var pages = new ArrayList<>(CommandEngine.CurrentPages);
        pages.remove(CommandEngine.CurrentPageIndex);

        for (String page: pages
             ) {
            var ms = varPattern.matcher(page);
            while (ms.find()) {
                var f = new DocumentPattern.Field();
                f.name = ms.group(1);
                fields.add(f);
            }
        }
        CurrentPattern = new DocumentPattern();
        CurrentPattern.pattentName = patternName;
        CurrentPattern.fields = fields;
        CurrentPattern.template = pages;

        SpCoreApi.PutPattern(CurrentPattern, (p, x) -> {
            if(p){
                CommandEngine.currentManages.selectAll();
                CommandEngine.currentManages.delete(-1);
                CommandEngine.currentManages.insert("Шаблон успешно сохранен");
            }
            else{
                CommandEngine.currentManages.selectAll();
                CommandEngine.currentManages.delete(-1);
                CommandEngine.currentManages.insert("Ошибка");
            }
        });

        return true;
    }

    public boolean Open(SelectionManager manager, String patternName){

        SpCoreApi.GetPattern(patternName, (s, p) ->{
            if(!s){
                manager.selectAll();
                manager.delete(-1);
                manager.insert("Ошибка");
                return;
            }
            var mc = MinecraftClient.getInstance();
            var screen = (BookEditScreen)mc.currentScreen;

            CurrentPattern = p;
            //currentOpenedPattern = true; на подумать
            Method method = null;
            try {
                method = BookEditScreen.class.getDeclaredMethod("openNextPage");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            method.setAccessible(true);
            for (String page: p.template
            ) {

                try {
                    manager.selectAll();
                    manager.delete(-1);
                    manager.insert(page);
                    method.invoke(screen);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        return true;
    }


    public boolean Fill(SelectionManager manager, String patternName){
        SpCoreApi.GetPattern(patternName, (s, p) ->{
            if(!s){
                manager.selectAll();
                manager.delete(-1);
                manager.insert("Ошибка");
                return;
            }
            CurrentPattern = p;

            CommandEngine.currentManages.selectAll();
            CommandEngine.currentManages.delete(-1);
            CommandEngine.currentManages.insert(CreateConfigMessage());

        });
        return true;
    }


    private String CreateConfigMessage(){
        var builder = new StringBuilder();
        builder.append("Заполните{\n");
        for (DocumentPattern.Field field: CurrentPattern.fields
             ) {
            builder.append(field.name)
                    .append(": ")
                    .append("\n");
        }
        builder.append("}\nsave: *");

        return builder.toString();
    }


    public void SavePattern(String page){

        var matcher = globalRegex.matcher(page);
        String inner = null;
        while (matcher.find()){
            inner = matcher.group(1);
            if(inner != null){
                break;
            }
        }
        if(inner == null){
            return;
        }

        matcher = innerRegex.matcher(inner);
        while (matcher.find()){
            var key = matcher.group(1);
            var value = matcher.group(2);

            for (var i = 0; i < CurrentPattern.template.size(); i++){
                CurrentPattern.template.set(i, CurrentPattern.template.get(i).replaceAll("\\{\\{" + key + "}}", value));
            }

        }


        var mc = MinecraftClient.getInstance();
        var screen = (BookEditScreen)mc.currentScreen;

        Method method = null;
        try {
            method = BookEditScreen.class.getDeclaredMethod("openNextPage");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        for (String p: CurrentPattern.template
        ) {

            try {
                CommandEngine.currentManages.selectAll();
                CommandEngine.currentManages.delete(-1);
                CommandEngine.currentManages.insert(p);
                method.invoke(screen);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

    }



    @Override
    public boolean OnKeyDown(int keyCode, int scanCode, int modifiers, String page){
        if(CurrentCommand.equals("fill")){
            var matcher = globalRegex.matcher(page);

            boolean save = true;
            while (matcher.find()){
                var gr = matcher.group(2);
                if(gr != null){
                    save = false;
                }
            }

            if(save){
                try {
                    SavePattern(page);
                }
                catch (Exception e){
                    SpCore.LOGGER.error(e.getMessage());
                    CommandEngine.currentManages.selectAll();
                    CommandEngine.currentManages.delete(-1);
                    CommandEngine.currentManages.insert("Ошибка");
                }
                return false;
            }
            else {
                return true;
            }
        }

        return false;
    }

    @Override
    public String GetDescription() {
        return "шаблоны";
    }
}
