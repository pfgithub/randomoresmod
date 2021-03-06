package pw.pfg.randomoresmod.modbiome;

import java.util.Random;

public class BiomeDetails {
	String id;
	String englishName;

	// String[] preferredOres
	// String[] preferredCreatures
	public static BiomeDetails random(String id) {
		Random random = new Random(id.hashCode());
		String englishName = id.substring(0, 1).toUpperCase() + id.substring(1);

		random.nextInt();

		return new BiomeDetails(id, englishName);
	}

	private BiomeDetails(String id, String englishName) {
		this.id = id;
		this.englishName = englishName;
	}
}
