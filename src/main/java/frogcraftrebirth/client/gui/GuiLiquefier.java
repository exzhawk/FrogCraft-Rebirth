/**
 * This file is a part of FrogCraftRebirth, 
 * created by U_Knowledge at 11:27:28 PM, Apr 2, 2016, 
 * FrogCraftRebirth, is open-source under MIT license,
 * check https://github.com/FrogCraft-Rebirth/
 * FrogCraft-Rebirth/LICENSE_FrogCraft_Rebirth for 
 * more information.
 */
package frogcraftrebirth.client.gui;

import org.lwjgl.opengl.GL11;

import frogcraftrebirth.client.GuiUtil;
import frogcraftrebirth.common.gui.ContainerLiquefier;
import frogcraftrebirth.common.tile.TileLiquefier;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiLiquefier extends GuiContainer {
	
	TileLiquefier tile;
	
	public GuiLiquefier(InventoryPlayer playerInv, TileLiquefier tile) {
		super(new ContainerLiquefier(playerInv, tile));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiUtil.getGuiBackground("Liquifier"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		int chargeIcon = (int) (14 * tile.charge / 10000); // TileLiquefier has max charge of 10000;
		this.drawTexturedModalRect(this.guiLeft + 81, this.guiTop + 27 + 14 - chargeIcon, 176, 52 + 14 - chargeIcon, 14, chargeIcon);
	
		int progress = (int) (24 * tile.process / 100);
		this.drawTexturedModalRect(this.guiLeft + 77, this.guiTop + 56, 176, 70, progress, 17);
	}
	
}
