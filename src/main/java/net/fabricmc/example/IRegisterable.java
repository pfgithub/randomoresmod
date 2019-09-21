package net.fabricmc.example;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;
import com.swordglowsblue.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.assets.TranslationBuilder;

public interface IRegisterable {
	public void registerTranslations(TranslationBuilder trans);
	public void registerData(ServerResourcePackBuilder data);
	public void registerAssets(ClientResourcePackBuilder pack);
	public void register();
	public void registerClient();
}