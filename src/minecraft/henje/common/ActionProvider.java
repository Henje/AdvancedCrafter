package henje.common;

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionProvider;

public class ActionProvider implements IActionProvider {

	@Override
	public LinkedList<IAction> getNeighborActions(Block block, TileEntity tile) {
		if(tile != null && tile instanceof TileEntityCrafter) {
			LinkedList<IAction> list = new LinkedList<IAction>();
			list.add(new ActionStopCrafter());
			list.add(new ActionStartCrafter());
			return list;
		} else {
			return new LinkedList<IAction>();
		}
	}
	
}
