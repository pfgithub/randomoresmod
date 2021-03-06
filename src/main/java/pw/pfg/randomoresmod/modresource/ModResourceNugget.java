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

public class ModResourceNugget
	extends Item
	implements IRegisterable, RegisterableItemDefaults {
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
	public boolean hasGlint(ItemStack itemStack_1) {
		return resource.isShiny || super.hasGlint(itemStack_1);
	}

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

	// --------- boilerplate code ---------
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
	public ResourceDetails getResource() {
		return resource;
	}

	@Override
	public TextureInfo getTexture() {
		return texture;
	}

	@Override
	public Item getItem() {
		return this;
	}
}
