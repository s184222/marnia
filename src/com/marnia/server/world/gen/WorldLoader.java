package com.marnia.server.world.gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.marnia.entity.registry.EntityContainer;
import com.marnia.entity.registry.EntityRegistry;
import com.marnia.entity.registry.IEntityProvider;
import com.marnia.world.WorldStorage;

public final class WorldLoader {

	private static final char SEPARATOR_CHAR = ';';
	private static final char ENTITY_SPLITTER_CHAR = ':';
	
	private WorldLoader() {
	}

	public static WorldFile loadFromFile(String path) throws IOException {
		InputStream is = WorldLoader.class.getResourceAsStream(path);
		if (is == null)
			throw new IOException("World file does not exist!");

		List<String> rows = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
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
		List<WorldEntityInfo> entityInfos = new ArrayList<WorldEntityInfo>();

		for(int yt = 0; yt < height; yt++) {
			String[] cells = rows.get(yt).split(Character.toString(SEPARATOR_CHAR), -1);
			if (cells.length != width)
				throw new IOException("Invalid number of cells at row: " + yt);
			
			for(int xt = 0; xt < width; xt++) {
				String cell = cells[xt];
				if(!cell.isEmpty()){
					if (cell.indexOf(ENTITY_SPLITTER_CHAR) >= 0) {
						String[] args = cell.split(Character.toString(ENTITY_SPLITTER_CHAR), -1);
						if (args.length != 2) {
							throw new IOException("Invalid entity encoding '" + cell + 
									"' at (row, column): (" + xt + ", " + yt + ")");
						}
						
						if (!args[0].isEmpty())
							decodeTile(storage, xt, yt, args[0]);
						if (!args[1].isEmpty())
							decodeEntity(entityInfos, xt, yt, args[1]);
					} else {
						decodeTile(storage, xt, yt, cell);
					}
				}
			}
		}

		return new WorldFile(storage, entityInfos);
	}

	private static void decodeEntity(List<WorldEntityInfo> entityInfos, int xt, int yt, String entityArg) throws IOException {
		int entityId = decodeInt(xt, yt, entityArg);
		if (entityId == EntityRegistry.PLAYER_PROVIDER_ID)
			throw new IOException("Invalid entity id at (column, row): (" + xt + ", " + yt + ")");
		
		@SuppressWarnings("rawtypes")
		IEntityProvider provider = EntityRegistry.getInstance().getEntityProvider(entityId);
		if (provider.getContainerClass() != EntityContainer.class) {
			throw new IOException("Unable to decode " + entityId + 
					" entity! Does not have default entity container.");
		}
		
		// Might have to be changed to ensure unique ids.
		EntityContainer container = new EntityContainer(xt, yt, null);
		entityInfos.add(new WorldEntityInfo(entityId, container));
	}
	
	private static void decodeTile(WorldStorage storage, int xt, int yt, String tileArg) throws IOException {
		storage.setTileIndex(xt, yt, decodeInt(xt, yt, tileArg));
	}
	
	private static int decodeInt(int xt, int yt, String intString) throws IOException {
		try {
			return Integer.parseInt(intString);
		} catch (NumberFormatException e) {
			throw new IOException("Invalid integer at (column, row): (" + xt + ", " + yt + ")");
		}
	}
}
