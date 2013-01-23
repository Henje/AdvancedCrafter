package henje.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.transport.IPipeEntry;

public class TileEntityCrafter extends TileEntity implements IInventory {
	private final static int CRAFT_DELAY = 60;
	private int counter;
	private ItemStack[] inventory;
	private boolean activated = true;

	public TileEntityCrafter() {
		inventory = new ItemStack[9];
	}
	
	public ItemStack craftItem() {
		ItemStack result = findRecipe();
		if(result != null) {
			for(int i = 0; i < 9; i++) {
				this.decrStackSize(i, 1);
			}
		}
		return result;
	}
	
	public ItemStack craftItemFromChests() {
		ItemStack result = findRecipe();
		TileEntity chest = searchChestSurrounding(worldObj, xCoord, yCoord, zCoord);
		if(chest != null && removeFromChest((IInventory)chest,getIngredients())) {
			return result;
		} else {
			return null;
		}
	}
	
	private boolean removeFromChest(IInventory chest,
			HashMap<Integer, Integer> ingredients) {
		HashMap<Integer,Integer> chestInv = getContent((IInventory)chest);
		Iterator<Entry<Integer,Integer>> set = ingredients.entrySet().iterator();
		while(set.hasNext()) {
			Entry<Integer,Integer> entry = set.next();
			if(!(chestInv.containsKey(entry.getKey()) && chestInv.get(entry.getKey()) >= entry.getValue())) {
				return false;
			}
		}
		set = ingredients.entrySet().iterator();
		while(set.hasNext()) {
			Entry<Integer,Integer> entry = set.next();
			for(int i = 0; i < chest.getSizeInventory(); i++) {
				if(chest.getStackInSlot(i) != null && chest.getStackInSlot(i).itemID == entry.getKey()) {
					int toSubtract = Math.min(chest.getStackInSlot(i).stackSize, entry.getValue());
					chest.decrStackSize(i, toSubtract);
					entry.setValue(entry.getValue()-toSubtract);
				}
			}
		}
		return true;
	}

	public HashMap<Integer,Integer> getIngredients() {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i = 0; i < 9; i++) {
			ItemStack now = getStackInSlot(i);
			if(now != null) {
				if(map.containsKey(now.itemID)) {
					int amount = map.get(now.itemID) + 1;
					map.remove(now.itemID);
					map.put(now.itemID, amount);
				} else {
					map.put(now.itemID, 1);
				}
			}
		}
		return map;		
	}
	
	private HashMap<Integer,Integer> getContent(IInventory inv) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i = 0; i < 9; i++) {
			ItemStack now = inv.getStackInSlot(i);
			if(now != null) {
				if(map.containsKey(now.itemID)) {
					int amount = map.get(now.itemID) + now.stackSize;
					map.remove(now.itemID);
					map.put(now.itemID, amount);
				} else {
					map.put(now.itemID, now.stackSize);
				}
			}
		}
		return map;		
	}
	
	private boolean canUseItem(ItemStack is, EntityPlayer player) {
		ItemStack inv = is, mouse = player.inventory.getItemStack();
		return (mouse == null) || (inv.getItemName().equals(mouse.getItemName())) && (mouse.getMaxStackSize()-mouse.stackSize >= is.stackSize);
	}
	
	public ItemStack craftItemAsPlayer(EntityPlayer player) {
		ItemStack result = findRecipe();
		if(canUseItem(result, player)) {
			return craftItem();
		} else {
			return null;
		}
	}
	
	public void shiftCraftAsPlayer(EntityPlayer player) {
		ItemStack result = craftItem();
		while(result != null && player.inventory.addItemStackToInventory(result)) {
			result = craftItem();
		}
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
		player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		NBTTagList tagList = tagCompound.getTagList("inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		NBTTagCompound flags = tagCompound.getCompoundTag("flags");
		activated = flags.getBoolean("activated");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		NBTTagCompound flags = new NBTTagCompound();
		flags.setBoolean("activated", activated);
		tagCompound.setTag("flags", flags);
		tagCompound.setTag("inventory", itemList);
	}

	@Override
	public String getInvName() {
		return "henje.tilentitycrafter";
	}
	
	public ItemStack findRecipe() {
		InventoryCrafting craftMatrix = new InventoryCrafting(new Container() {

			@Override
			public boolean canInteractWith(EntityPlayer var1) {
				// TODO Auto-generated method stub
				return false;
			}
			
		}, 3, 3);

		for (int i = 0; i < getSizeInventory(); ++i) {
			ItemStack stack = getStackInSlot(i);

			craftMatrix.setInventorySlotContents(i, stack);
		}

		ItemStack recipe = CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj);

		return recipe;
	}
	
	@Override
	public void updateEntity() {
		if(activated) {
			counter++;
			if(counter == CRAFT_DELAY) {
				counter = 0;
				craft();
			}
		}
	}
	
	private void craft() {
		IPipeEntry pipe = searchPipeSurrounding(worldObj, xCoord, yCoord, zCoord);
		ItemStack result = craftItemFromChests();
		if(pipe != null && result != null) {
			pipe.entityEntering(result, computeDirection((TileEntity)pipe));
		}
	}
	
	private IPipeEntry searchPipeSurrounding(World world, int x, int y, int z) {
		return (IPipeEntry) searchTileEntitySurrounding(world, x, y, z, IPipeEntry.class);
	}
	
	private TileEntity searchChestSurrounding(World world, int x, int y, int z) {
		try {
			Class c = Class.forName("cpw.mods.ironchest.TileEntityIronChest");
			TileEntity tile = searchTileEntitySurrounding(world, x, y, z, c);
			if(tile != null) {
				return tile;
			} else {
				return (TileEntityChest) searchTileEntitySurrounding(world, x, y, z, TileEntityChest.class);
			}
		} catch (ClassNotFoundException e) {
			return (TileEntityChest) searchTileEntitySurrounding(world, x, y, z, TileEntityChest.class);			
		}
	}
	
	private TileEntity searchTileEntitySurrounding(World world, int x, int y, int z, Class c) {
		if(c.isInstance(world.getBlockTileEntity(x+1, y, z))) {
			return world.getBlockTileEntity(x+1, y, z);
		} else if(c.isInstance(world.getBlockTileEntity(x-1, y, z))) {
			return world.getBlockTileEntity(x-1, y, z);
		} else if(c.isInstance(world.getBlockTileEntity(x, y+1, z))) {
			return world.getBlockTileEntity(x, y+1, z);
		} else if(c.isInstance(world.getBlockTileEntity(x, y-1, z))) {
			return world.getBlockTileEntity(x, y-1, z);
		} else if(c.isInstance(world.getBlockTileEntity(x, y, z+1))) {
			return world.getBlockTileEntity(x, y, z+1);
		} else if(c.isInstance(world.getBlockTileEntity(x, y, z-1))) {
			return world.getBlockTileEntity(x, y, z-1);
		} else {
			return null;
		}
	}
	
	private ForgeDirection computeDirection(TileEntity tile) {
		if(xCoord > tile.xCoord) {
			return ForgeDirection.WEST;
		} else if(xCoord < tile.xCoord) {
			return ForgeDirection.EAST;
		} else if(yCoord > tile.yCoord) {
			return ForgeDirection.DOWN;
		} else if(yCoord < tile.yCoord) {
			return ForgeDirection.UP;
		} else if(zCoord > tile.zCoord) {
			return ForgeDirection.NORTH;
		} else if(zCoord < tile.zCoord) {
			return ForgeDirection.SOUTH;
		} else {
			return ForgeDirection.UNKNOWN;
		}
	}
	
	public void setActivated(boolean b) {
		activated = b;
	}
}
