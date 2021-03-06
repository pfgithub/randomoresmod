package pw.pfg.randomoresmod.modresource;

import java.util.Random;
import net.minecraft.util.Rarity;
import pw.pfg.randomoresmod.ObjectDetails;
import pw.pfg.randomoresmod.TextureInfo;

public class ResourceDetails extends ObjectDetails {
	public float materialHardness;
	public float materialResistance;

	public TextureInfo ore;
	public TextureInfo gem;
	public TextureInfo storageBlock;
	public TextureInfo nugget; // NULLABLE!!!!!

	public boolean requiresSmelting;
	public boolean dropsMany;

	public boolean isFuel;
	public int fuelSmeltingTime;

	public int smeltingTime;

	public Rarity rarityMC;
	public double rarity; // 0 to 4.99

	public int oreMinSpawn;
	public int oreMaxSpawn;
	public int oresPerChunk;
	public int oreVeinSize;

	public boolean isShiny;

	// public boolean torch;
	// public boolean emissive;
	// public boolean brightArmor; // night vision on helmet
	// public int luminance; // 0..15
	// public boolean blocksHoldFire
	static double halfLife(
		double current,
		double initial,
		double halfLife,
		double change
	) { // half life 3 formula
		return initial * Math.pow(change, current / halfLife);
	}

	public static ResourceDetails random(String resourceNameId) {
		// note that new features must be inserted at the bottom of a section
		Random setupRandom = new Random(resourceNameId.hashCode());
		Random featureRandom = new Random(setupRandom.nextInt());
		Random abilityRandom = new Random(setupRandom.nextInt());
		Random cosmeticRandom = new Random(setupRandom.nextInt());
		Random essentialRandom = new Random(setupRandom.nextInt());
		Random oreGenRandom = new Random(setupRandom.nextInt());

		// essential
		double rarity = essentialRandom.nextDouble() * 5d;

		// ore gen
		int oreSpawnArea = (int) Math.ceil(halfLife(rarity, 100d, 1d, 0.5d));
		int oreMinCenterLimit = 0 + oreSpawnArea / 2; // 0
		int oreMaxCenterLimit = 128 - oreSpawnArea / 2;

		int oreCenter = (int) Math.ceil(
			(halfLife(oreGenRandom.nextDouble() * 5d, 1d, 1d, 0.5d) * // distributed(1, 0.5, 0.25, 0.125, 0.0625, 0.03125)
				(oreMaxCenterLimit - oreMinCenterLimit)) +
				oreMinCenterLimit
		); // higher chance for lower heights. matters more for smaller center ranges

		// int oreCenter = oreGenRandom.nextInt(
		// 	oreMaxCenterLimit - oreMinCenterLimit + 1
		// ) +
		// 	oreMinCenterLimit;
		int oreMinSpawn = oreCenter - oreSpawnArea / 2;
		int oreMaxSpawn = oreCenter + oreSpawnArea / 2;

		// vanilla:
		// coal: 20 (0..128)
		// iron: 20 (0..64)
		// gold: 2 (0..32)
		// redstone: 8 (0..16)
		// diamond: 1 (0..16)
		// lapis: countdepthdecorator (count=1, baseline=16, spread=16)
		int oresPerChunk = (int) Math.floor(halfLife(rarity, 16d, 1.25d, 0.5d)); // rarity1..5(16, 9, 5, 3, 1, 1)

		// vanilla:
		// coal: 17
		// iron: 9
		// gold: 9
		// redstone: 8
		// diamond: 8
		// lapis: 7
		int oreVeinSize = (int) Math.floor(
			halfLife(oreGenRandom.nextDouble() * 5d, 15d, 2.222223d, 0.5d)
		); // distributed random (15, 9, 8, 5, 4, 3)

		// feature
		boolean requiresSmelting = featureRandom.nextBoolean();
		boolean dropsMany = requiresSmelting ? false : featureRandom.nextBoolean();
		boolean isIngot = requiresSmelting && featureRandom.nextBoolean();

		float materialHardness = featureRandom.nextFloat() * 10.0f;
		float materialResistance = featureRandom.nextFloat() * 10.0f; // it should be possible for some materials to have a very high resistance

		boolean hasUnusualSmeltingTime = featureRandom.nextInt(4) == 0; // 25% chance, another 25% for if it's extra long
		boolean unusualSmeltingTimeIsLong = featureRandom.nextInt(4) == 0; // maybe unusualsmeltingtime should be related to materialhardness
		int longUnusualSmeltingTime = featureRandom.nextInt(1800) + 200;
		int shortUnusualSmeltingTime = featureRandom.nextInt(190) + 10;

		// ability
		boolean hasNugget = isIngot &&
		(abilityRandom.nextBoolean() || abilityRandom.nextBoolean());

		boolean isFuel = abilityRandom.nextInt(4) == 0;
		int fuelSmeltingTime = abilityRandom.nextInt(64) * 100; // 200 smelts 1 item in a furnace or 2 items in a blast furnace

		// cosmetic
		int color = cosmeticRandom.nextInt(0xFFFFFF); // can't generate 0xFFFFFF. maybe change?

		boolean isShiny = rarity > 3f && cosmeticRandom.nextBoolean();

		// computed
		String englishName = resourceNameId.substring(0, 1).toUpperCase() +
			resourceNameId.substring(1);

		TextureInfo oreStyle = ResourceObjectOre.random(resourceNameId);
		TextureInfo gemStyle = isIngot ? ResourceObjectIngot.random(resourceNameId)
			: ResourceObjectGem.random(resourceNameId);
		TextureInfo storageBlockStyle = ResourceObjectStorageBlock.random(
			resourceNameId
		);
		TextureInfo nuggetStyle = hasNugget
			? ResourceObjectNugget.random(resourceNameId)
			: null;

		int smeltingTime = hasUnusualSmeltingTime
			? (unusualSmeltingTimeIsLong ? longUnusualSmeltingTime // 10s to 100s
			: shortUnusualSmeltingTime) // 0.5s to 10s
			: 200; // default 10s

		Rarity rarityMC;

		if (rarity > 4d) {
			rarityMC = Rarity.EPIC;
		} else if (rarity > 3d) {
			rarityMC = Rarity.RARE;
		} else if (rarity > 2d) {
			rarityMC = Rarity.UNCOMMON;
		} else {
			rarityMC = Rarity.COMMON;
		}

		String baseId = resourceNameId;
		String translationKey = "name.randomoresmod.resource." + baseId;

		// ---
		//
		//
		//
		// minHeight
		// maxHeight
		// (it might be interesting to have something with a min height of 100)
		// SmeltingProduct smeltingProduces = 90%gem | 5%block | 5%nugget if hasNugget
		// TODO: isOvergrown - adds an overlay to ore and block that uses the grass color and all items that makes it look overgrown
		// inspired by https://i.redd.it/6lalhnzbann31.png
		return new ResourceDetails(
			color,
			baseId,
			translationKey,
			englishName,
			materialHardness,
			materialResistance,
			oreStyle,
			gemStyle,
			storageBlockStyle,
			nuggetStyle,
			requiresSmelting,
			dropsMany,
			isFuel,
			fuelSmeltingTime,
			smeltingTime,
			rarityMC,
			rarity,
			oreMinSpawn,
			oreMaxSpawn,
			oresPerChunk,
			oreVeinSize,
			isShiny
		);
	}

	private ResourceDetails(
		int color,
		String baseId,
		String translationKey,
		String englishName,
		float materialHardness,
		float materialResistance,
		TextureInfo ore,
		TextureInfo gem,
		TextureInfo storageBlock,
		TextureInfo nugget,
		boolean requiresSmelting,
		boolean dropsMany,
		boolean isFuel,
		int fuelSmeltingTime,
		int smeltingTime,
		Rarity rarityMC,
		double rarity,
		int oreMinSpawn,
		int oreMaxSpawn,
		int oresPerChunk,
		int oreVeinSize,
		boolean isShiny
	) {
		super(color, baseId, translationKey, englishName);
		this.materialHardness = materialHardness;
		this.materialResistance = materialResistance;
		this.ore = ore;
		this.gem = gem;
		this.storageBlock = storageBlock;
		this.nugget = nugget;
		this.requiresSmelting = requiresSmelting;
		this.dropsMany = dropsMany;
		this.isFuel = isFuel;
		this.fuelSmeltingTime = fuelSmeltingTime;
		this.smeltingTime = smeltingTime;
		this.rarityMC = rarityMC;
		this.rarity = rarity;
		this.oreMinSpawn = oreMinSpawn;
		this.oreMaxSpawn = oreMaxSpawn;
		this.oresPerChunk = oresPerChunk;
		this.oreVeinSize = oreVeinSize;
		this.isShiny = isShiny;
	// list=`code here`.split("\n").map(l => l.trim()).filter(l => l).map(l => l.replace(/public (.+?);/, "$1").split(" "));
	//
	// console.log(list.map(l => l[1]).join(",\n"));
	// console.log(list.map(l => l.join(" ")).join(",\n"));
	// console.log(list.map(l => `this.${l[1]} = ${l[1]};`).join("\n"));
	}

	//prettier-ignore
	@Override
	public String toString() {
		return "{" +
			" \n super=" + super.toString() +
			",\n materialHardness='" + materialHardness + "'" +
			",\n materialResistance='" + materialResistance + "'" +
			",\n ore='" + ore + "'" +
			",\n gem='" + gem + "'" +
			",\n storageBlock='" + storageBlock + "'" +
			",\n nugget='" + nugget + "'" +
			",\n requiresSmelting='" + requiresSmelting + "'" +
			",\n dropsMany='" + dropsMany + "'" +
			",\n isFuel='" + isFuel + "'" +
			",\n fuelSmeltingTime='" + fuelSmeltingTime + "'" +
			",\n smeltingTime='" + smeltingTime + "'" +
			",\n rarityMC='" + rarityMC + "'" +
			",\n rarity='" + rarity + "'" +
			",\n oreMinSpawn='" + oreMinSpawn + "'" +
			",\n oreMaxSpawn='" + oreMaxSpawn + "'" +
			",\n oresPerChunk='" + oresPerChunk + "'" +
			",\n oreVeinSize='" + oreVeinSize + "'" +
			",\n isShiny='" + isShiny + "'" +
			" \n}";
	}
}
