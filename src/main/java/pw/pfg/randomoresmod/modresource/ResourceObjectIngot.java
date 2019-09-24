package pw.pfg.randomoresmod.modresource;

import java.util.Random;

public class ResourceObjectIngot extends ResourceObject {

	public static Style[] STYLE = StyleSet.Builder()
		.resourceBase("randomoresmod:item/ingot/")
		.languageKeyBase("name.randomoresmod.ingot.")
		.add("ingot")
		.add("broken_ingot")
		.add("alternate_ingot")
		.build();

	public static ResourceObjectIngot random(String baseId) {
		String id = baseId + "_ingot";
		Random random = new Random(id.hashCode());

		Style style = STYLE[random.nextInt(STYLE.length)];

		return new ResourceObjectIngot(id, style);
	}

	public ResourceObjectIngot(String id, Style style) {
		super(id, style);
	}
}