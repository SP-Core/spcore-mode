package spcore.fabric.sounds.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class FreecamUtil {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    /**
     * @return whether freecam is currently in use
     */
    public static boolean isFreecamEnabled() {
        if (mc.player == null) {
            return false;
        }
        return false;
    }

    /**
     * Gets the proximity reference point. Unless freecam is active, this is the main camera's position.
     *
     * @return the position distances should be measured from
     */
    public static Vec3d getReferencePoint() {
        if (mc.player == null) {
            return Vec3d.ZERO;
        }
        return isFreecamEnabled() ? mc.player.getEyePos() : mc.gameRenderer.getCamera().getPos();
    }

    /**
     * Measures the distance to the provided position.
     * <p>
     * Distance is relative to either the player or camera, depending on whether freecam is enabled.
     *
     * @param pos the position to be measured
     * @return the distance to the position
     */
    public static double getDistanceTo(Vec3d pos) {
        return getReferencePoint().distanceTo(pos);
    }

    /**
     * Gets the volume for the provided distance.
     * <p>
     * Distance is relative to either the player or camera, depending on whether freecam is enabled.
     *
     * @param maxDistance the maximum distance of the sound
     * @param pos         the position of the audio
     * @return the resulting audio volume
     */
    public static float getDistanceVolume(float maxDistance, Vec3d pos) {
        return PositionalAudioUtils.getDistanceVolume(maxDistance, getReferencePoint(), pos);
    }
}
