package spcore.js.functions;

public class GetCommandFunc implements JsFunc{
    @Override
    public String get() {
        return "function getCommand(commands, commandName){\n" +
                "    for(var i = 0; i < commands.length; i++)\n" +
                "    {\n" +
                "        if(commands[i].name == commandName){\n" +
                "            return i;\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    return null;\n" +
                "}";
    }
}
