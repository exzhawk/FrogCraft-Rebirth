package frogcraftrewrite.client.gui;

import org.lwjgl.opengl.GL11;

import frogcraftrewrite.client.GuiUtil;
import frogcraftrewrite.common.gui.ContainerFluidOutputHatch;
import frogcraftrewrite.common.tile.TileFluidOutputHatch;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFluidOutputHatch extends GuiContainer {

	TileFluidOutputHatch tile;

	public GuiFluidOutputHatch(InventoryPlayer playerInv, TileFluidOutputHatch tile) {
		super(new ContainerFluidOutputHatch(playerInv, tile));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiUtil.getGuiBackground("LiquidOutput"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		// TODO
		// GuiUtil.renderFluidTank(0, 0, 0, 0,
		// tile.getTankInfo()[0].fluid.getFluid(),
		// 100*(tile.getTankInfo()[0].fluid.amount)/(tile.getTankInfo()[0].capacity));
	}

}
