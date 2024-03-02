package spcore.spnet.world;

import net.minecraft.client.MinecraftClient;
import spcore.GlobalContext;
import spcore.spnet.SpNetConnectionClient;
import spcore.spnet.models.ResourceId;
import spcore.spnet.resources.SpNetResourceStorage;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class SoundChannel extends Thread{
    private final SpNetConnectionClient client;
    private final ResourcesResolver resourcesResolver;
    private final ResourceId id;
    private final WorldThread worldThread;
    private boolean start;
    public SoundChannel(SpNetConnectionClient client, ResourcesResolver resourcesResolver, ResourceId id, WorldThread worldThread) {
        this.client = client;
        this.resourcesResolver = resourcesResolver;
        this.id = id;
        this.worldThread = worldThread;
    }

    @Override
    public void run() {
        Sound sound = null;
        try{
            start = true;
            resourcesResolver.addResource(id);
            while (!resourcesResolver.containsLoadedResource(id)){
                Thread.sleep(1000);
            }
            var stream = SpNetResourceStorage.Resolve(id);
            sound = new Sound(stream);
            sound.play();

            while (client.connect && MinecraftClient.getInstance().player != null && start){
                var resPos = worldThread.getResourcePos(id);
                var playerPos = MinecraftClient.getInstance().player.getPos();
                var volume = calculateSoundVolume(resPos.x, resPos.y, resPos.z, playerPos.x, playerPos.y, playerPos.z);
                sound.setVolume((float) volume);
                Thread.sleep(200);
            }
        } catch (IOException | UnsupportedAudioFileException | InterruptedException e) {
            GlobalContext.LOGGER.error("sound channel error " + e.getMessage());
        } finally {
            if(sound != null){
                sound.stop();
                sound.close();
            }
        }

    }

    public void Stop(){
        start = false;
    }

    public static double calculateSoundVolume(double x1, double y1, double z1, double x2, double y2, double z2) {
        double distance = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2));

        // Пример формулы для расчета громкости в зависимости от расстояния
        double maxDistance = 100.0; // Максимальное расстояние для нормализации
        double volume = 1.0 - Math.min(distance / maxDistance, 1.0); // Нормализация до диапазона от 0 до 1

        return volume;
    }
}
