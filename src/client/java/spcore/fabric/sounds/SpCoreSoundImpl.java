package spcore.fabric.sounds;

import net.minecraft.client.sound.Sound;
import net.minecraft.util.math.floatprovider.FloatSupplier;

public class SpCoreSoundImpl extends Sound {
    public SpCoreSoundImpl(String id, FloatSupplier volume, FloatSupplier pitch, int weight, RegistrationType registrationType, boolean stream, boolean preload, int attenuation) {
        super(id, volume, pitch, weight, registrationType, stream, preload, attenuation);
    }


}
