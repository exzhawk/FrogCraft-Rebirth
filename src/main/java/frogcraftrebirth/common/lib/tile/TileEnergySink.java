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

package frogcraftrebirth.common.lib.tile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileEnergySink extends TileEnergy implements IEnergySink {
	
	public int charge;
	public int maxCharge;
	private final int sinkTier;
	
	protected TileEnergySink(int sinkTier, int maxEnergy) {
		this.sinkTier = sinkTier;
		this.maxCharge = maxEnergy;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.charge = tag.getInteger("charge");
		this.maxCharge = tag.getInteger("maxCharge");
	}
	
	@Override
	public void readPacketData(DataInputStream input) throws IOException {
		charge = input.readInt();
		maxCharge = input.readInt();
	}
	
	@Override
	public void writePacketData(DataOutputStream output) throws IOException {
		output.writeInt(charge);
		output.writeInt(maxCharge);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("charge", this.charge);
		tag.setInteger("maxCharge", maxCharge);
		return super.writeToNBT(tag);
	}

	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		return maxCharge - charge;
	}

	@Override
	public int getSinkTier() {
		return sinkTier;
	}

	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		this.charge += amount;
		if (charge >= maxCharge)
			charge = maxCharge;
		return 0;
	}

}
