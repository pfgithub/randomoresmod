package pw.pfg.randomoresmod.modresource;

import java.util.List;
import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;
import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.assets.TranslationBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import pw.pfg.randomoresmod.IRegisterable;
import pw.pfg.randomoresmod.RandomOresMod;
import pw.pfg.randomoresmod.RegistrationHelper;
import pw.pfg.randomoresmod.TextureInfo;

public class ModResourceNugget extends Item implements IRegisterable {
	ResourceDetails resource;
	TextureInfo texture;

	public ModResourceNugget(ResourceDetails resource) {
		super(
			new Item.Settings()
				.group(RandomOresMod.RESOURCES)
				.rarity(resource.rarityMC)
		);
		this.resource = resource;
		this.texture = resource.nugget;
	}

	@Override
	public boolean hasEnchantmentGlint(ItemStack itemStack_1) {
		return resource.isShiny || super.hasEnchantmentGlint(itemStack_1);
	}

	@Override
	public final String getTranslationKey() {
		return RegistrationHelper.getTranslationKey();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public Text getName() {
		return RegistrationHelper.getName(texture, resource);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public Text getName(ItemStack itemStack) {
		return this.getName();
	}

	@Override
	public void registerTranslations(TranslationBuilder trans) {}

	@Override
	public void registerData(ServerResourcePackBuilder data) {
		data.addShapedRecipe(
			new Identifier("randomoresmod", resource.gem.id + "_from_nugget"),
			shaped -> {
				shaped.group(new Identifier("randomoresmod", this.texture.id))
					.pattern("###", "###", "###")
					.ingredientItem('#', new Identifier("randomoresmod", this.texture.id))
					.result(new Identifier("randomoresmod", resource.gem.id), 1);
			}
		);
		data.addShapelessRecipe(
			new Identifier("randomoresmod", this.texture.id + "_from_gem"),
			shapeless -> {
				shapeless.ingredientItem(
					new Identifier("randomoresmod", resource.gem.id)
				)
					.result(new Identifier("randomoresmod", this.texture.id), 9);
			}
		);
	}

	@Override
	public void registerAssets(ClientResourcePackBuilder pack) {
		RegistrationHelper.registerItemModels(pack, texture);
	}

	@Override
	public void register() {
		RegistrationHelper.register(this.texture.id, this);
		if (resource.isFuel) {
			FuelRegistry.INSTANCE.add(this, resource.fuelSmeltingTime / 9);
		}
	}

	@Override
	public void registerClient() {
		RegistrationHelper.registerColorProvider(this, resource);
	}

	@Override
	public void registerBiomeFeatures(Biome biome) {}

	@Override
	public void registerItemGroup(List<ItemStack> stacks) {
		stacks.add(new ItemStack(this));
	}
}
