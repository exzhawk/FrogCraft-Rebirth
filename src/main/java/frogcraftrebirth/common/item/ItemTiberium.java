/**
 * This file is a part of FrogCraftRebirth, 
 * created by U_Knowledge at 4:27:29 PM, Apr 3, 2016, EST
 * FrogCraftRebirth, is open-source under MIT license,
 * check https://github.com/FrogCraft-Rebirth/
 * FrogCraft-Rebirth/LICENSE_FrogCraft_Rebirth for 
 * more information.
 */
package frogcraftrebirth.common.item;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import frogcraftrebirth.api.FrogAPI;
import frogcraftrebirth.common.lib.item.ItemFrogCraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemTiberium extends ItemFrogCraft {

	public ItemTiberium() {
		super(true);
		this.setCreativeTab(FrogAPI.frogTab);
		this.setMaxStackSize(32);
		this.setUnlocalizedName("tiberium");
		this.setSubNameArray("red", "blue", "green");
		this.iconArray = new IIcon[this.nameArray.length];
	}
	
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHolding) {
		
	}

	@Override
	public List<String> getToolTip(ItemStack stack, EntityPlayer player, boolean adv) {
		return Arrays.asList("CAUTION: NOT FULLY IMPLEMENTED YET");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName()+"."+nameArray[stack.getItemDamage()];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int damage) {
		return iconArray[damage];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg) {
		for (int n=0;n<nameArray.length;n++) {
			iconArray[n] = reg.registerIcon(TEXTURE_MAIN + nameArray[n] + "Tiberium");
		}
	}

}