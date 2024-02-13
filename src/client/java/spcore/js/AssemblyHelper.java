package spcore.js;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AssemblyHelper {
    private final static List<String> blackList = new ArrayList<>();

    public final static Set<Class<? extends Object>> allTypes;
    public final static Set<Class<? extends Enum>> allEnums;

    static {
        Reflections reflections = new Reflections("net.minecraft", new SubTypesScanner(false));
        allTypes = reflections.getSubTypesOf(Object.class);

        allEnums = reflections.getSubTypesOf(Enum.class);

    }

    public static String ResolveName(String name, String sample){
        if(name.contains("$")){
            int lastDotIndex = name.lastIndexOf(".");
            String result = name.substring(lastDotIndex + 1);
            return result.replace("$", "_").replace(";", "");
        }

        return sample;
    }
}
