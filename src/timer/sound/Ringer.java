package timer.sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Ringer {

	private Clip clip;
	private AudioInputStream ais;

	public Ringer(File file) throws Exception {
		clip = AudioSystem.getClip();
		setSound(file);
	}

	public void ring() {
		try {
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSound(File file) throws Exception {
		clip.close();
		ais = AudioSystem.getAudioInputStream(file);
		clip.open(ais);
	}

	public void stop() {
		clip.stop();
	}
}
