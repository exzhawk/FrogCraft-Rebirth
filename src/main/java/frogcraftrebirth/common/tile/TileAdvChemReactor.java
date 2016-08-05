package frogcraftrebirth.common.tile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import frogcraftrebirth.api.FrogAPI;
import frogcraftrebirth.api.OreStack;
import frogcraftrebirth.api.recipes.IAdvChemRecRecipe;
import frogcraftrebirth.common.lib.tile.TileEnergySink;
import frogcraftrebirth.common.lib.util.ItemUtil;
import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileAdvChemReactor extends TileEnergySink implements IHasWork {
	
	//0 for module, 1-5 for input, 6-10 for output, 11 for cell input and 12 for cell output
	public final IItemHandler module = new ItemStackHandler();
	public final IItemHandler input = new ItemStackHandler(5);
	public final IItemHandler output = new ItemStackHandler(5);
	public final IItemHandler cellInput = new ItemStackHandler();
	public final IItemHandler cellOutput = new ItemStackHandler();
	
	public int process, processMax;
	private boolean working;
	private IAdvChemRecRecipe recipe;
	
	public TileAdvChemReactor() {
		super(2, 50000);
	}
	
	@Override
	public boolean isWorking() {
		return working;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.working = tag.getBoolean("working");
		this.process = tag.getInteger("process");
		this.processMax = tag.getInteger("processMax");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setBoolean("working", this.working);
		tag.setInteger("process", this.process);
		tag.setInteger("processMax", this.processMax);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readPacketData(DataInputStream input) throws IOException {
		super.readPacketData(input);
		this.process = input.readInt();
		this.processMax = input.readInt();
		this.working = input.readBoolean();
	}
	
	@Override
	public void writePacketData(DataOutputStream output) throws IOException {
		super.writePacketData(output);
		output.writeInt(process);
		output.writeInt(processMax);
		output.writeBoolean(working);
	}
	
	@Override
	public void update() {
		if (worldObj.isRemote) {
			worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
			return;
		}
		super.update();
		
		if (!working || recipe == null) {
			ItemStack[] inputs = new ItemStack[] {input.getStackInSlot(0), input.getStackInSlot(1), input.getStackInSlot(2), input.getStackInSlot(3), input.getStackInSlot(4)};
			recipe = (IAdvChemRecRecipe)FrogAPI.managerACR.<ItemStack>getRecipe(inputs);
			
			if (checkRecipe(recipe)) {
				this.consumeIngredient(recipe.getInputs());
				this.process = 0;
				this.processMax = recipe.getTime();
				this.working = true;
			} else {
				this.working = false;
				this.sendTileUpdatePacket(this);
				this.markDirty();
				return;
			}
		}
		
		if (recipe != null && charge >= recipe.getEnergyRate()) {
			this.charge -= recipe.getEnergyRate();
			++process;
		}
		
		if (recipe != null && process == processMax) {
			this.produce();
			this.process = 0;
			this.processMax = 0;
			this.recipe = null;
		}
		
		this.sendTileUpdatePacket(this);
		this.markDirty();
	}
	
	private boolean checkRecipe(IAdvChemRecRecipe recipe) {
		if (recipe == null)
			return false;
		if (cellInput.getStackInSlot(0) != null) {
			if (!IC2Items.getItem("fluid_cell").isItemEqual(cellInput.getStackInSlot(0)))
				return false;
			if (cellInput.getStackInSlot(0).stackSize < recipe.getRequiredCellAmount())
				return false;
		} else {
			if (recipe.getRequiredCellAmount() > 0)
				return false;
		}
		if (recipe.getCatalyst() != null && !recipe.getCatalyst().isItemEqual(module.getStackInSlot(0)))
			return false;
		for (ItemStack outputStack : recipe.getOutputs()) {
			ItemStack remain = ItemHandlerHelper.insertItemStacked(output, outputStack.copy(), true);
			if (remain != null)
				return false;
		}
		return true;
	}
	
	private void consumeIngredient(Collection<OreStack> toBeConsumed) {
		for (OreStack ore : toBeConsumed) {
			for (int i = 0; i < 5; i++) {
				if (ore.consumable(input.getStackInSlot(i)))
					input.extractItem(i, ore.getAmount(), false);
			}
		}
		if (recipe.getRequiredCellAmount() > 0) {
			cellInput.extractItem(0, recipe.getRequiredCellAmount(), false);
		}
	}
	
	private void produce() {
		for (ItemStack outputStack : recipe.getOutputs()) {
			ItemStack remain = ItemHandlerHelper.insertItemStacked(output, outputStack.copy(), false);
			if (remain != null)
				ItemUtil.dropItemStackAsEntityInsanely(getWorld(), getPos(), remain);
		}
		if (recipe.getProducedCellAmount() > 0) {
			if (cellOutput.getStackInSlot(0) != null) {
				ItemStack cell = cellOutput.getStackInSlot(0).copy();
				cell.stackSize = recipe.getProducedCellAmount();
				ItemStack remain = cellOutput.insertItem(0, cell, false);
				if (remain != null)
					ItemUtil.dropItemStackAsEntityInsanely(getWorld(), getPos(), remain);
			} else {
				ItemStack stack = IC2Items.getItem("fluid_cell");
				stack.stackSize = recipe.getProducedCellAmount();
				cellOutput.insertItem(0, stack, false);
			}
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? true : super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			switch (facing) {
			case UP:
				return (T)input;
			case DOWN:
				return (T)output;
			case NORTH:
			case EAST:
				return (T)cellInput;
			case SOUTH:
			case WEST:
				return (T)cellOutput;
			default:
				break;
			}
		}	
		return super.getCapability(capability, facing);
	}

}
