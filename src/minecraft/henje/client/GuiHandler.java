package henje.client;

import henje.common.ContainerCrafter;
import henje.common.TileEntityCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	private ContainerCrafter container;
	
	private boolean server = false, client = false;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return new ContainerCrafter(player.inventory, (TileEntityCrafter) world.getBlockTileEntity(x, y, z));
		//return getServerContainer(player.inventory, (TileEntityCrafter) world.getBlockTileEntity(x, y, z));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return new GuiAdvancedCrafter(new ContainerCrafter(player.inventory, (TileEntityCrafter) world.getBlockTileEntity(x, y, z)));
		//return getClientContainer(player.inventory, (TileEntityCrafter) world.getBlockTileEntity(x, y, z));
	}
	
	private ContainerCrafter getServerContainer(InventoryPlayer inv, TileEntityCrafter crafter) {
		ContainerCrafter container;
		if(!client) {
			System.out.println("creating server");
			server = true;
			container = new ContainerCrafter(inv, crafter);
			this.container = container;
		} else {
			server = false;
			client = false;
			container = this.container;
			this.container = null;
		}
		return container;
	}
	
	private GuiAdvancedCrafter getClientContainer(InventoryPlayer inv, TileEntityCrafter crafter) {
		ContainerCrafter container;
		if(!server) {
			client = true;
			container = new ContainerCrafter(inv, crafter);
			this.container = container;
		} else {
			server = false;
			client = false;
			container = this.container;
			this.container = null;
		}
		return new GuiAdvancedCrafter(container);
	}

}
