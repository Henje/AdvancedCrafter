package henje.client;

import net.minecraftforge.client.MinecraftForgeClient;
import henje.common.CommonProxy;

public class ClientProxy extends CommonProxy {
	@Override
	public void loadTexture() {
		MinecraftForgeClient.preloadTexture("/texture/blocks.png");
	}
}
