/*
 * Copyright (c) 2015 - 2017 3TUSK, et al.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * This file is a part of FrogCraftRebirth, 
 * created by 3TUSK at 10:21:32 PM, Jul 27, 2016, 
 * FrogCraftRebirth, is open-source under MIT license,
 * check https://github.com/FrogCraft-Rebirth/
 * FrogCraft-Rebirth/LICENSE_FrogCraft_Rebirth for 
 * more information.
 */
package frogcraftrebirth.common.gui;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

class SlotDischarger extends SlotFrog {

	public SlotDischarger(IItemHandler inv, int index, int x, int y) {
		super(inv, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack != null && stack.getItem() instanceof IElectricItem && ElectricItem.manager.discharge(stack, Double.MAX_VALUE, Integer.MAX_VALUE, true, true, true) > 0;
	}

}
