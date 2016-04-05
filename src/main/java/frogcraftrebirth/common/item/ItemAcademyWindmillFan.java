package frogcraftrebirth.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import frogcraftrebirth.common.lib.item.ItemFrogCraft;
import frogcraftrebirth.common.tile.TileAcademyWindmillTurbine;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemAcademyWindmillFan extends ItemFrogCraft {
	
	public ItemAcademyWindmillFan() {
		super(false);
		setMaxDamage(0);
		setMaxStackSize(1);
		setNoRepair();
		setTextureName(TEXTURE_MAIN + "ACWindMill_Fan");
		setUnlocalizedName("academyWindmillRotor.name");
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack s, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileAcademyWindmillTurbine) {
			player.inventory.consumeInventoryItem(this);
			player.inventory.markDirty();
			((TileAcademyWindmillTurbine)tile).hasRotor = true;
			return true;
		}
		return false;
	}

	@Override
	public List<String> getToolTip(ItemStack stack, EntityPlayer player, boolean adv) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List list) {
		list.add(new ItemStack(item, 1, 0));
	}

}