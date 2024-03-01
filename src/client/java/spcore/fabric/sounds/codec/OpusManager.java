package spcore.fabric.sounds.codec;

import spcore.GlobalContext;
import spcore.fabric.sounds.managers.SpCoreSoundManager;
import spcore.fabric.sounds.utils.Utils;

public class OpusManager {

    public static CoreOpusDecoder createDecoder() {
        return new JavaOpusDecoderImpl(SpCoreSoundManager.SAMPLE_RATE, SpCoreSoundManager.FRAME_SIZE);
    }


}
