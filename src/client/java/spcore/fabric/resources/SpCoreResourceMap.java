package spcore.fabric.resources;

import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpCoreResourceMap implements Map<Identifier, Resource> {
    private final HashMap<Identifier, Resource> hashMap = new HashMap<>();
    private final ResourceProvider _provider;

    public SpCoreResourceMap(ResourceProvider provider) {
        _provider = provider;
    }

    @Override
    public int size() {
        return hashMap.size() + _provider.getSize();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty() && _provider.getSize() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return hashMap.containsKey(key) || _provider.contains((Identifier)key);
    }

    @Override
    public boolean containsValue(Object value) {
        return hashMap.containsValue(value);
    }

    @Override
    public Resource get(Object key) {
        if(hashMap.containsKey(key)){
            return hashMap.get(key);
        }

        if(_provider.contains((Identifier)key)){
            return _provider.get((Identifier)key);
        }
        return null;
    }

    @Nullable
    @Override
    public Resource put(Identifier key, Resource value) {
        return hashMap.put(key, value);
    }

    @Override
    public Resource remove(Object key) {
        return hashMap.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends Identifier, ? extends Resource> m) {
        hashMap.putAll(m);
    }

    @Override
    public void clear() {
        hashMap.clear();
    }

    @NotNull
    @Override
    public Set<Identifier> keySet() {
        return hashMap.keySet();
    }

    @NotNull
    @Override
    public Collection<Resource> values() {
        return hashMap.values();
    }

    @NotNull
    @Override
    public Set<Entry<Identifier, Resource>> entrySet() {
        return hashMap.entrySet();
    }
}
