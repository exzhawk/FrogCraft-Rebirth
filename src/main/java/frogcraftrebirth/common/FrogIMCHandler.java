/**
 * This file is a part of FrogCraftRebirth, 
 * created by 3TUSK at 3:37:04 PM, Apr 3, 2016, EST
 * FrogCraftRebirth, is open-source under MIT license,
 * check https://github.com/FrogCraft-Rebirth/
 * FrogCraft-Rebirth/LICENSE_FrogCraft_Rebirth for 
 * more information.
 */
package frogcraftrebirth.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import frogcraftrebirth.api.FrogAPI;
import frogcraftrebirth.api.OreStack;
import frogcraftrebirth.api.mps.MPSUpgradeManager;
import frogcraftrebirth.common.lib.AdvChemRecRecipe;
import frogcraftrebirth.common.lib.CondenseTowerRecipe;
import frogcraftrebirth.common.lib.PyrolyzerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public final class FrogIMCHandler {
	
	private FrogIMCHandler() {
		throw new UnsupportedOperationException();
	}
	
	static void resolveIMCMessage(Collection<FMLInterModComms.IMCMessage> messages) {
		for (FMLInterModComms.IMCMessage message : messages) {
			if (message.isNBTMessage()) {
				NBTTagCompound theTag = message.getNBTValue();
				String mode = theTag.getString("mode");

				if ("recipe".toLowerCase(Locale.ENGLISH).equals(mode)) {
					String machine = theTag.getString("machine").toLowerCase(Locale.ENGLISH);
						switch (machine) {
						case ("pyrolyzer"): {
							ItemStack input = new ItemStack(theTag.getCompoundTag("input"));
							ItemStack output = new ItemStack(theTag.getCompoundTag("output"));
							FluidStack outputFluid = FluidStack.loadFluidStackFromNBT(theTag.getCompoundTag("fluid"));
							int time = theTag.getInteger("time");
							int energyPerTick = theTag.getInteger("energyPerTick");
							FrogAPI.managerPyrolyzer.add(new PyrolyzerRecipe(input, output, outputFluid, time, energyPerTick));
							break;
						}
						case ("advchemreactor"): {
							NBTTagCompound inputs = theTag.getCompoundTag("inputs"), outputs = theTag.getCompoundTag("outputs");
							ArrayList<OreStack> inputsArray = new ArrayList<>();
							ArrayList<ItemStack> outputsArray = new ArrayList<>();
							for (int n = 0; n < 5; n++) {
								int index = n + 1;
								inputsArray.add(OreStack.loadFromNBT(inputs.getCompoundTag("input" + index)));
								outputsArray.add(new ItemStack(outputs.getCompoundTag("output" + index)));
							}
							inputsArray.removeIf(Objects::isNull);
							outputsArray.removeIf(Objects::isNull);
							int time = theTag.getInteger("time");
							int energyPerTick = theTag.getInteger("energyPerTick");
							ItemStack catalyst = new ItemStack(theTag.getCompoundTag("catalyst"));
							int cellReq = theTag.getInteger("cellReq");
							int cellProduce = theTag.getInteger("cellProduce");
							FrogAPI.managerACR.add(new AdvChemRecRecipe(inputsArray, outputsArray, catalyst, time, energyPerTick, cellReq, cellProduce));
							break;
						}
						case ("condensetower"): {
							FluidStack input = FluidStack.loadFluidStackFromNBT(theTag.getCompoundTag("input"));
							NBTTagCompound outputs = theTag.getCompoundTag("outputs");
							FluidStack[] outputArray = new FluidStack[5];
							for (int index = 0; index < 5; index++) {
								outputArray[index] = FluidStack.loadFluidStackFromNBT(outputs.getCompoundTag("output" + index));
							}
							int time = theTag.getInteger("time");
							int energyPerTick = theTag.getInteger("energyPerTick");
							FrogAPI.managerCT.add(new CondenseTowerRecipe(time, energyPerTick, input, outputArray));
							break;
						}
						case ("combustionfurnace"): {
							ItemStack input = new ItemStack(theTag.getCompoundTag("input"));
							ItemStack output = new ItemStack(theTag.getCompoundTag("output"));
							FluidStack outputFluid = FluidStack.loadFluidStackFromNBT(theTag.getCompoundTag("fluid"));
							String ore = theTag.getString("ore");
							if (!input.isEmpty()) {
								if (!output.isEmpty())
									FrogAPI.FUEL_REG.regFuelByproduct(input, output);
								if (outputFluid != null)
									FrogAPI.FUEL_REG.regFuelByproduct(input, outputFluid);
							} else if (!ore.isEmpty()) {
								if (!output.isEmpty())
									FrogAPI.FUEL_REG.regFuelByproduct(ore, output);
								if (outputFluid != null)
									FrogAPI.FUEL_REG.regFuelByproduct(ore, outputFluid);
							} else {
								FrogAPI.FROG_LOG.warn("A broken Combustion Furnace sent by %s byproduct registry is detected. Please double check the code, or report to FrogCraftRebirth immediately!", message.getSender());
							}
							break;
						}
						default:
							break;
					}
				}
				
				if ("mps".toLowerCase(Locale.ENGLISH).equals(mode)) {
					String type = theTag.getString("type");
					ItemStack item = new ItemStack((NBTTagCompound) theTag.getTag("item"));
					int value = theTag.getInteger("value");
					if (!item.isEmpty()) {
						switch (type.toLowerCase(Locale.ENGLISH)) {
							case ("solar"): {
								MPSUpgradeManager.INSTANCE.registerSolarUpgrade(item);
								break;
							}
							case ("voltage"):
							case ("transformer"): {
								MPSUpgradeManager.INSTANCE.registerVoltageUpgrades(item, value);
								break;
							}
							case ("capacity"):
							case ("storage"): {
								MPSUpgradeManager.INSTANCE.registerStorageUpgrade(item, value);
								break;
							}
						}
					} else {
						FrogAPI.FROG_LOG.warn("'%s' is trying to register Mobile Power Station with NULL ItemStack, which is not allowed.", message.getSender());
					}
				}

			}
		}
	}

}
