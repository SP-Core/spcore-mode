package spcore.fabric.sounds.managers;

import javax.annotation.Nullable;

public class SpeakerManager {

    public static AudioManager createSpeaker(SpCoreSoundManager soundManager, @Nullable long audioChannel) throws Exception {
        var speaker = new AudioManager(soundManager, SpCoreSoundManager.SAMPLE_RATE, SpCoreSoundManager.FRAME_SIZE, audioChannel);
        speaker.open();
        return speaker;
    }
}
