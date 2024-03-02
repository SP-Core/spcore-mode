package spcore.spnet.world;

import spcore.GlobalContext;
import spcore.spnet.SpNetConnectionClient;
import spcore.spnet.models.ResourceId;
import spcore.spnet.resources.SpNetResourceStorage;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ResourcesResolver extends Thread{
    private final BlockingQueue<ResourceId> resourceIds = new LinkedBlockingQueue<>();
    private final SpNetConnectionClient client;
    private final HashSet<ResourceId> loadResources = new HashSet<>();

    public ResourcesResolver(SpNetConnectionClient client) {
        this.client = client;
    }

    public void addResource(ResourceId id){
        if(resourceIds.stream().anyMatch(p -> p.hashCode() == id.hashCode())){
            return;
        }

        if(loadResources.contains(id)){
            return;
        }
        resourceIds.add(id);
    }

    public boolean containsLoadedResource(ResourceId id){
        return loadResources.contains(id);
    }

    @Override
    public void run() {
        while (client.connect){
            try {
                var resource = resourceIds.take();
                SpNetResourceStorage.ResolveIfNeed(resource);
                loadResources.add(resource);
            } catch (InterruptedException | IOException e) {
                GlobalContext.LOGGER.error(e.getMessage());
            }
        }
    }
}
