package pl.szymon.casino;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockBlackjack extends Block implements ITileEntityProvider {
    public static final PropertyDirection FACING=PropertyDirection.create("facing",EnumFacing.Plane.HORIZONTAL);
    private static final AxisAlignedBB BOX=new AxisAlignedBB(0,0,0,1,0.875,1);
    public BlockBlackjack(){super(Material.WOOD);setRegistryName(CasinoMod.ID,"blackjack_table");setUnlocalizedName(CasinoMod.ID+".blackjack_table");setCreativeTab(CasinoMod.TAB);setHardness(2.5F);setResistance(6000000F);setSoundType(SoundType.WOOD);setDefaultState(blockState.getBaseState().withProperty(FACING,EnumFacing.NORTH));}
    @Override protected BlockStateContainer createBlockState(){return new BlockStateContainer(this,FACING);}
    @Override public IBlockState getStateFromMeta(int meta){return getDefaultState().withProperty(FACING,EnumFacing.getHorizontal(meta));}
    @Override public int getMetaFromState(IBlockState state){return state.getValue(FACING).getHorizontalIndex();}
    @Override public IBlockState getStateForPlacement(World w,BlockPos p,EnumFacing f,float x,float y,float z,int meta,EntityLivingBase placer){return getDefaultState().withProperty(FACING,placer.getHorizontalFacing().getOpposite());}
    @Override public TileEntity createNewTileEntity(World w,int meta){return new TileBlackjack();}
    @Override public boolean isOpaqueCube(IBlockState s){return false;}
    @Override public boolean isFullCube(IBlockState s){return false;}
    @Override public AxisAlignedBB getBoundingBox(IBlockState s,IBlockAccess w,BlockPos p){return BOX;}
    @Override public BlockFaceShape getBlockFaceShape(IBlockAccess w,IBlockState s,BlockPos p,EnumFacing f){return BlockFaceShape.UNDEFINED;}
    @Override public EnumPushReaction getMobilityFlag(IBlockState s){return EnumPushReaction.BLOCK;}
    @Override public boolean onBlockActivated(World w,BlockPos p,IBlockState s,EntityPlayer player,EnumHand hand,EnumFacing side,float x,float y,float z){
        if(!w.isRemote){TileEntity t=w.getTileEntity(p);if(t instanceof TileBlackjack && ((TileBlackjack)t).usable(player) && ((TileBlackjack)t).join(player)>=0)player.openGui(CasinoMod.instance,0,w,p.getX(),p.getY(),p.getZ());}return true;
    }
    @Override public float getPlayerRelativeBlockHardness(IBlockState s,EntityPlayer p,World w,BlockPos pos){TileEntity t=w.getTileEntity(pos);if(t instanceof TileBlackjack && ((TileBlackjack)t).locked())return 0;return super.getPlayerRelativeBlockHardness(s,p,w,pos);}
    @Override public void breakBlock(World w,BlockPos p,IBlockState s){TileEntity t=w.getTileEntity(p);if(t instanceof TileBlackjack)((TileBlackjack)t).cancel();super.breakBlock(w,p,s);}
}
