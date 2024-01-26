package com.example.commands.commandEngine;

import com.example.SpCore;
import com.example.commands.*;
import com.example.spcore.SpCoreApi;
import com.example.spcore.SpCoreApiContext;
import com.example.spcore.SpCryptoLink;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;
import java.util.List;

public class CommandEngine {

    private static HashMap<String, BaseCommand> _commands;
    public static SelectionManager currentManages;
    public static List<String> CurrentPages;
    public static int CurrentPageIndex;

    static {
        _commands = new HashMap<>();
        _commands.put("help", new HelpCommand());
        _commands.put("hello", new HelloCommand());
        _commands.put("cls", new CLSCommand());
        _commands.put("lout", new LoutCommand());
        _commands.put("pattern", new PatternCommand());
    }

    public static String CommandCallbackName = null;
    public static HashMap<String, BaseCommand> GetCommands(){
        return _commands;
    }

    public static void onKeyDown(int keyCode, int scanCode, int modifiers, String page, SelectionManager currentPageSelectionManager){

        currentManages = currentPageSelectionManager;
        if(CommandCallbackName != null){
            var r = _commands.get(CommandCallbackName).OnKeyDown(keyCode, scanCode, modifiers, page);
            if (!r) {
                CommandCallbackName = null;
                currentManages.insert("\n$/");
            }
            return;
        }
        if(!SpCoreApiContext.IsAuthorized){
            if(BookEditScreen.isPaste(keyCode)){
                Authorized(page);
            }
            return;
        }

        if(page.equals("spt\n")){
            OpenTerminal();
        }

        if(page.startsWith(MessageOpenTerminal())){
            if(page.endsWith("\n"))
            {
                var endLine = page.lastIndexOf("\n");
                var startCommand = page.lastIndexOf("$/");
                var allCommand = page.substring(startCommand + 2, endLine);

                var command = CommandParser.Parse(allCommand);
                if(_commands.containsKey(command.Target)){
                    var result = _commands.get(command.Target).Invoke(currentManages, command.Args);
                    if(result){
                        CommandCallbackName = command.Target;
                        return;
                    }

                }
                else{
                    currentManages.insert("Команда не найдена");
                }
                currentManages.insert("\n$/");
            }
        }



    }

    public static String MessageOpenTerminal() {
        return "=$=$SP-TERMINAL=$=$";
    }

    private static void OpenTerminal(){

        var message = MessageOpenTerminal() + "\n$/";
        if(GlobalContext.ZeroOpenTerminal){
            message += "help\n";
        }
        currentManages.selectAll();
        currentManages.delete(-1);
        currentManages.insert(message);

        if(GlobalContext.ZeroOpenTerminal) {
            onKeyDown(0, 0, 0, message, currentManages);
            GlobalContext.ZeroOpenTerminal = false;
        }



    }

    private static void Authorized(String page){
        var code = SpCryptoLink.parse(page);
        if(code == null){
            return;
        }
        SpCoreApiContext.Code = code;
        SpCoreApi.PostIsAuthorized(CommandEngine::SetSuccessAuthMessage, CommandEngine::SetErrorAuthMessage);
    }

    private static void SetSuccessAuthMessage(){
        currentManages.selectAll();
        currentManages.delete(-1);
        currentManages.insert(GetSuccessAuthMessage());
    }

    private static void SetErrorAuthMessage(){
        currentManages.selectAll();
        currentManages.delete(-1);
        currentManages.insert("Ошибка авторизации");
    }

    private static String GetSuccessAuthMessage(){
        return "======SP-CORE======\n" +
                "Авторизация успешно пройдена!\n" +
                "\n" +
                "Для того чтобы открыть терминал, напишите \"spt\" в первой строке книги и нажмите Enter\n" +
                "  \n";
    }

}
