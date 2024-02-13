package spcore.fabric.resources;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import spcore.fabric.handlers.TerminalHandler;
import spcore.fabric.sounds.SpCoreResource;

public class TestResourceProvider implements ResourceProvider {

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public boolean contains(Identifier key) {
        return TerminalHandler.Id == key;
    }

    @Override
    public Resource get(Identifier key) {
        if(TerminalHandler.Id != key){
            return null;
        }
        return new SpCoreResource(() -> {
            return null;
        });
    }


    public static ResourceFactory createFactory(ResourceFactory resourceFactory){
        return p -> {
            if(TerminalHandler.Id != p){
                return resourceFactory.getResource(p);
            }
            return java.util.Optional.of(new SpCoreResource(() -> {
                return null;
            }));
        };
    }
}
