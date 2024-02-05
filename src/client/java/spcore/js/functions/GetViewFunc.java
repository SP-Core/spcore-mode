package spcore.js.functions;

public class GetViewFunc implements JsFunc {
    @Override
    public String get() {
        return "function getView(routes, url){\n" +
                "    for(var i = 0; i < routes.length; i++)\n" +
                "    {\n" +
                "        if(routes[i].route == url){\n" +
                "            return i;\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    return null;\n" +
                "}";
    }
}
