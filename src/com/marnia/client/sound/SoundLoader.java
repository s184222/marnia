package com.marnia.client.sound;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.LineUnavailableException;

import com.g4mesoft.sound.SoundManager;
import com.g4mesoft.sound.processor.AudioSource;

public final class SoundLoader {

	public static int COLLECT_SOUND_ID = -1;
	public static int BACKGROUND_SOUND_ID = -1;

	private static final String COLLECT_SOUND_PATH = "/sounds/collect.wav";
	private static final String BACKGROUND_SOUND_PATH = "/sounds/background.wav";

	private SoundLoader() {
	}
	
	public static void loadSounds() throws IOException {
		COLLECT_SOUND_ID = readSound(COLLECT_SOUND_PATH);
		BACKGROUND_SOUND_ID = readSound(BACKGROUND_SOUND_PATH);
	}
	
	private static int readSound(String path) throws IOException {
		InputStream is = SoundLoader.class.getResourceAsStream(path);
		if (is == null)
			throw new IOException("Sound file not found: " + path);
		
		int id = SoundManager.getInstance().loadSound(is);
		try {
			SoundManager.getInstance().preparePermanantSoundThread(id);
		} catch (LineUnavailableException e) {
		}
		return id;
	}
	
	public static AudioSource playSound(int id, float volume, float pitch) {
		AudioSource source = play(id);
		if (source != null)
			source.setVolume(volume).setPitch(pitch);
		return source;
	}

	public static AudioSource playSound(int id, float volume) {
		AudioSource source = play(id);
		if (source != null)
			source.setVolume(volume);
		return source;
	}

	public static AudioSource playForever(int id, float volume) {
		AudioSource source = play(id);
		if (source != null) {
			source.setVolume(volume);
			source.setLoopAmount(AudioSource.LOOP_CONTINOUSLY);
		}
		return source;
	}
	
	private static AudioSource play(int id) {
		try {
			return SoundManager.getInstance().playSound(id);
		} catch (LineUnavailableException e) {
		}
		
		return null;
	}
}
