package frogcraftrewrite.client.gui;

import org.lwjgl.opengl.GL11;

import frogcraftrewrite.common.gui.ContainerAdvChemReactor;
import frogcraftrewrite.common.tile.TileAdvChemReactor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAdvChemReactor extends GuiContainer {

	TileAdvChemReactor tile;

	public GuiAdvChemReactor(InventoryPlayer playerInv, TileAdvChemReactor tile) {
		super(new ContainerAdvChemReactor(playerInv, tile));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture_AdvChemReactor);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	private static ResourceLocation texture_AdvChemReactor = new ResourceLocation(
			"frogcraftrewrite:textures/gui/GUI_AdvanceChemicalReactor.png");
}
