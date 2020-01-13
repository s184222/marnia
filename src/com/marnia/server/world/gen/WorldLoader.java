package com.marnia.server.world.gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.marnia.world.WorldStorage;

public final class WorldLoader {

	private static final char SEPARATOR_CHAR = ';';

	private WorldLoader() {
	}

	public static WorldStorage loadFromFile(String path) throws IOException {
		InputStream is = WorldLoader.class.getResourceAsStream(path);
		if (is == null) {
			throw new IOException("World file does not exist!");
		}

		List<String> rows = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = br.readLine()) != null)
				rows.add(line);
		}

		int width = 0;
		int height = rows.size();

		if (height > 0) {
			width = 1;

			String firstRow = rows.get(0);
			for (int i = 0; i < firstRow.length(); i++) {
				if (firstRow.charAt(i) == SEPARATOR_CHAR)
					width++;
			}
		}

		WorldStorage storage = new WorldStorage(width, height);

		for(int yt = 0; yt < height; yt++) {
			String[] cells = rows.get(yt).split(Character.toString(SEPARATOR_CHAR));
			if (cells.length != width)
				throw new IOException("Invalid number of cells at row: " + yt);

			for(int xt = 0; xt<width; xt++) {
				if(!cells[xt].isEmpty()){
					int tileId;
					try {
						tileId = Integer.parseInt(cells[xt]);
					} catch (NumberFormatException e) {
						throw new IOException("Invalid integer at column, row:" + xt + ", " + yt);
					}

					storage.setTileIndex(xt, yt, tileId);
				}
			}
		}

		return storage;
	}
}
