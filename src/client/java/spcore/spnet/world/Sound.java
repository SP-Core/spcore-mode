package spcore.spnet.world;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Sound implements AutoCloseable {
    private boolean released = false;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;

    public Sound(InputStream f) throws UnsupportedAudioFileException, IOException {
        try {
            var buffer = new BufferedInputStream(f);
            stream = AudioSystem.getAudioInputStream(buffer);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.addLineListener(new Listener());
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
            released = false;

            close();
        }
    }

    // true если звук успешно загружен, false если произошла ошибка
    public boolean isReleased() {
        return released;
    }

    // проигрывается ли звук в данный момент
    public boolean isPlaying() {
        return playing;
    }

    // Запуск
	/*
	  breakOld определяет поведение, если звук уже играется
	  Если breakOld==true, о звук будет прерван и запущен заново
	  Иначе ничего не произойдёт
	*/
    public void play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    // То же самое, что и play(true)
    public void play() {
        play(true);
    }

    // Останавливает воспроизведение
    public void stop() {
        if (playing) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null)
            clip.close();

        if (stream != null)
            try {
                stream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
    }

    // Установка громкости
	/*
	  x долже быть в пределах от 0 до 1 (от самого тихого к самому громкому)
	*/
    public void setVolume(float x) {
        if (x<0) x = 0;
        if (x>1) x = 1;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        volumeControl.setValue((max-min)*x+min);
    }

    // Возвращает текущую громкость (число от 0 до 1)
    public float getVolume() {
        float v = volumeControl.getValue();
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        return (v-min)/(max-min);
    }

    // Дожидается окончания проигрывания звука
    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing)
                    clip.wait();
            } catch (InterruptedException exc) {}
        }
    }

    // Статический метод, для удобства
    public static Sound playSound(InputStream stream) throws UnsupportedAudioFileException, IOException {
        Sound snd = new Sound(stream);
        snd.play();
        return snd;
    }

    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}