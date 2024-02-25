package spcore.view.styles;

import spcore.GlobalContext;

import java.util.HashMap;

public abstract class ComponentStyle {
    public final HashMap<String, String> styles = new HashMap<>();
    protected final HashMap<String, CasterEvent> casters = new HashMap<>();
    private boolean enableCache = false;
    private final HashMap<String, HashMap<String,Object>> cache = new HashMap<>();
    public final HashMap<String, Object> data = new HashMap<>();
    public <T> T get(String key, Class<T> clazz){
        var v = styles.get(key);
        if(v == null || v.equals("null"))
            return null;

        if(enableCache){
            var cv = cache.get(key);
            if(cv != null){
                var fe = cv.get(v);
                if(fe != null){
                    return (T) fe;
                }

            }
        }

        var c = casters.get(key);
        if(c == null)
            return null;
        var result = c.cast(v, clazz);
        if(enableCache){
            if(result != null){
                var container = cache
                        .computeIfAbsent(key, k -> new HashMap<>());
                container.put(v, result);
            }
        }
        return result;
    }

    public <T> void addHandler(String key, String value,
                               Class<T> clazz,
                               AdaptiveCasterEvent<T> event){
        styles.put(key, value);
        casters.put(key, new CasterEvent() {
            @Override
            public <T> T cast(String p, Class<T> v) {
                if(clazz == v){
                    return (T) event.cast(p);
                }

                return null;
            }
        });
    }

    public void cache(){
        this.enableCache = true;
    }

}
