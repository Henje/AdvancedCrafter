package henje.common;

import buildcraft.core.proxy.CoreProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class ContainerCrafter extends Container {

	protected TileEntityCrafter tileEntity;
	protected IInventory result;
	
	public ContainerCrafter (InventoryPlayer inventoryPlayer, TileEntityCrafter te){
		tileEntity = te;
		result = new InventoryCraftResult();

		addSlotToContainer(new SlotCrafting(result, 0, 124, 35));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(tileEntity, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}
		
		bindPlayerInventory(inventoryPlayer);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
	
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return tileEntity.isUseableByPlayer(var1);
	}
	
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
            ItemStack stack = null;
            Slot slotObject = (Slot) inventorySlots.get(slot);

            //null checks and checks if the item can be stacked (maxStackSize > 1)
            if (slotObject != null && slotObject.getHasStack()) {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();

                    //merges the item into player inventory since its in the tileEntity
                    if (slot < 9) {
                            if (!this.mergeItemStack(stackInSlot, 9, 45, true)) {
                                    return null;
                            }
                    }
                    //places it into the tileEntity is possible since its in the player inventory
                    else if (!this.mergeItemStack(stackInSlot, 0, 9, false)) {
                            return null;
                    }

                    if (stackInSlot.stackSize == 0) {
                            slotObject.putStack(null);
                    } else {
                            slotObject.onSlotChanged();
                    }

                    if (stackInSlot.stackSize == stack.stackSize) {
                            return null;
                    }
                    slotObject.onPickupFromSlot(player, stackInSlot);
            }
            return stack;
    }
	
	@Override
	public void detectAndSendChanges() {
		result.setInventorySlotContents(0, tileEntity.findRecipe());
		super.detectAndSendChanges();
	}
	
	private boolean isResultSLot(int i) {
		return i == 0;
	}
	
	private boolean isShiftClicking(int flags) {
		return (flags&0x1) == 0x1;
	}
	
	@Override
	public ItemStack slotClick(int slot, int j, int flag, EntityPlayer entityplayer) {
		if(isResultSLot(slot)) {
			if(isShiftClicking(flag)) {
				tileEntity.shiftCraftAsPlayer(entityplayer);
			} else {
				tileEntity.craftItemAsPlayer(entityplayer);
			}
		}
		
		//result.setInventorySlotContents(0, tileEntity.findRecipe());

		ItemStack ret = super.slotClick(slot, j, flag, entityplayer);
		notifyChange(tileEntity);

		return ret;
	}
	
	private void notifyChange(IInventory inv) {
		onCraftMatrixChanged(inv);
	}

	public class SlotCrafting extends Slot {

		public SlotCrafting(IInventory iinventory, int i, int j, int k) {
			super(iinventory, i, j, k);
		}

		@Override
		public boolean isItemValid(ItemStack itemstack) {
			return false;
		}
	}
	
	public TileEntityCrafter getCrafter() {
		return tileEntity;
	}
}
