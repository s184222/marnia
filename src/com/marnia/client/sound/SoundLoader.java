package com.marnia.client.sound;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import com.g4mesoft.sound.SoundManager;
import com.g4mesoft.sound.processor.AudioSource;

public final class SoundLoader {

	public static int COLLECT_SOUND_ID = -1;

	private static final String COLLECT_SOUND_PATH = "/sounds/collect.wav";

	private SoundLoader() {
	}
	
	public static void loadSounds() throws IOException {
		COLLECT_SOUND_ID = readSound(COLLECT_SOUND_PATH);
	}
	
	private static int readSound(String path) throws IOException {
		return SoundManager.getInstance().loadSound(
				SoundLoader.class.getResourceAsStream(path));
	}
	
	public static void playSound(int id, float volume, float pitch) {
		AudioSource source = play(id);
		if (source != null)
			source.setVolume(volume).setPitch(pitch);
	}

	public static void playSound(int id, float volume) {
		AudioSource source = play(id);
		if (source != null)
			source.setVolume(volume);
	}
	
	private static AudioSource play(int id) {
		try {
			return SoundManager.getInstance().playSound(id);
		} catch (LineUnavailableException e) {
		}
		
		return null;
	}
}
