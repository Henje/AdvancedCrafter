package henje.client;

import henje.common.ContainerCrafter;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiAdvancedCrafter extends GuiContainer {
	public final int xSizeOfTexture = 256;
	public final int ySizeOfTexture = 256;
	
	public GuiAdvancedCrafter(ContainerCrafter cc) {
		super(cc);
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
            fontRenderer.drawString("Advanced Autocrafter", 16, 6, 4210752);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		int var4 = this.mc.renderEngine.getTexture("/gui/crafting.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);

		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;

		drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);
	}
}
