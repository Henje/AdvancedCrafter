package henje.client;

import henje.common.ContainerCrafter;
import henje.common.TileEntityCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return new ContainerCrafter(player.inventory, (TileEntityCrafter) world.getBlockTileEntity(x, y, z));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return new GuiAdvancedCrafter(new ContainerCrafter(player.inventory, (TileEntityCrafter) world.getBlockTileEntity(x, y, z)));
	}

}
