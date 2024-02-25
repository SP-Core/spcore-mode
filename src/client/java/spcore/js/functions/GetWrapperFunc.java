package spcore.js.functions;

public class GetWrapperFunc implements JsFunc{
    @Override
    public String get() {
        return "function getWrapper(routes, id){\n" +
                "    for(var i = 0; i < routes.length; i++)\n" +
                "    {\n" +
                "        if(routes[i].name == id){\n" +
                "            return i;\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    return null;\n" +
                "}";
    }
}
