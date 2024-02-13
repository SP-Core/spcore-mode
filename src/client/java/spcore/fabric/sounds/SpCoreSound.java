package spcore.fabric.sounds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatSupplier;
import net.minecraft.util.math.floatprovider.MultipliedFloatSupplier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class SpCoreSound {


    public final static SoundManager Manager = new SoundManager(MinecraftClient.getInstance().options);
    public final static SoundSystem System = new SoundSystem(MinecraftClient.getInstance().getSoundManager(), MinecraftClient.getInstance().options, new SpCoreResourceFactory());
}
