package frogcraftrebirth.common.block;

import frogcraftrebirth.FrogCraftRebirth;
import frogcraftrebirth.common.lib.block.BlockFrogWrenchable;
import frogcraftrebirth.common.lib.tile.TileFrog;
import frogcraftrebirth.common.lib.util.ItemUtil;
import frogcraftrebirth.common.tile.IHasWork;
import frogcraftrebirth.common.tile.TileCombustionFurnace;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenerator extends BlockFrogWrenchable {
	
	public BlockGenerator() {
		super(MACHINE, "generator", false, 0);
		setUnlocalizedName("generator");
		setDefaultState(getDefaultState().withProperty(WORKING, false));
		setHardness(5.0F);
		setResistance(10.0F);
	}
	
	@Override
	protected IProperty<?>[] getPropertyArray() {
		return new IProperty[] { FACING_HORIZONTAL, WORKING };
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileFrog tile = (TileFrog)worldIn.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileCombustionFurnace) {
				ItemUtil.dropInventroyItems(worldIn, pos, ((TileCombustionFurnace)tile).input, ((TileCombustionFurnace)tile).output, ((TileCombustionFurnace)tile).fluidIO);
			}
		}
		
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileCombustionFurnace(); //For now it is the possibility
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof IHasWork)
			return state.withProperty(WORKING, ((IHasWork)tile).isWorking());
		else
			return state.withProperty(WORKING, false);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;
		playerIn.openGui(FrogCraftRebirth.getInstance(), 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING_HORIZONTAL).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING_HORIZONTAL, enumfacing);
	}
	
	public enum Type implements IStringSerializableEnumImpl {
		COMBUSTION/*, IMAG_FLUX?, IMAG_PHASE, [NO ACCESS]*/
		// And AcademyCraft is striking back, seal will be removed,
		// the nearly-lost descendent *art of miracle* will revive,
		// Believers are welcome with the neo-gilded age,
		// Cult of Beasts will thrive, the creep will fade
		//
		// Yo-ake made, kono yume, kochou no yume
		// This dream, till daybreak, is a butterfly dream.
		//  - Team Shanghai Alice, "Kid's Festival ~ Innocent Treasures"
    }

}
