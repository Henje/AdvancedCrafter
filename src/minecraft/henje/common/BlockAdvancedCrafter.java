package henje.common;

import java.util.Random;

import buildcraft.api.transport.IPipeEntry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockAdvancedCrafter extends BlockContainer {
	
	public BlockAdvancedCrafter(int id) {
		super(id, Material.wood);
		super.setResistance(1.0f);
		super.setHardness(2.0f);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setTickRandomly(true);
	}
	
	@Override
	public String getTextureFile() {
		return "/texture/blocks.png";
	}
	
	@Override
	public int getBlockTextureFromSide(int i) {
		if(i == 0 || i == 1) {
			return 0;
		} else {
			return 1;
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float a, float b, float c) {
		if(!player.isSneaking()) {
			player.openGui(AdvancedCrafting.instance, 0, world, x, y, z);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int i, float j, float k, float h, int g) {
		world.scheduleBlockUpdate(x, y, z, blockID, 1);
	return super.onBlockPlaced(world, x, y, z, i, j, k, h, g);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
	}
	
	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;
		
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			
			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;
				
				EntityItem entityItem = new EntityItem(world,
				x + rx, y + ry, z + rz,
				new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));
				
				if (item.hasTagCompound()) {
					entityItem.readEntityFromNBT((NBTTagCompound) item.getTagCompound().copy());
				}
				
				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

		
	@Override
	public TileEntity createNewTileEntity(World world) {
	return new TileEntityCrafter();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int redstonePower) {
		world.scheduleBlockUpdate(x, y, z, blockID, 1);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
			TileEntityCrafter crafter = (TileEntityCrafter) world.getBlockTileEntity(x, y, z);
		if(world.isBlockIndirectlyGettingPowered(x, y, z)) {
			crafter.setActivated(false);
		} else {
			crafter.setActivated(true);
		}
	}
}
