package pl.szymon.casino;

import java.util.Random;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockRoulette extends Block implements ITileEntityProvider {
    private static final AxisAlignedBB BOX=new AxisAlignedBB(0,0,0,1,0.9,1);
    public BlockRoulette(){super(Material.WOOD);setRegistryName(CasinoMod.ID,"roulette_table");setUnlocalizedName(CasinoMod.ID+".roulette_table");setCreativeTab(CreativeTabs.DECORATIONS);setHardness(3);setResistance(6000000F);setSoundType(SoundType.WOOD);}
    @Override public TileEntity createNewTileEntity(World w,int meta){return new TileRoulette();}
    @Override public EnumBlockRenderType getRenderType(IBlockState state){return EnumBlockRenderType.INVISIBLE;}
    @Override public boolean isOpaqueCube(IBlockState state){return false;}
    @Override public boolean isFullCube(IBlockState state){return false;}
    @Override public AxisAlignedBB getBoundingBox(IBlockState s,IBlockAccess w,BlockPos p){return BOX;}
    @Override public BlockFaceShape getBlockFaceShape(IBlockAccess w,IBlockState s,BlockPos p,EnumFacing f){return BlockFaceShape.UNDEFINED;}
    @Override public EnumPushReaction getMobilityFlag(IBlockState state){return EnumPushReaction.BLOCK;}
    @Override public boolean canEntityDestroy(IBlockState state,IBlockAccess world,BlockPos pos,Entity entity){return false;}
    public static TileRoulette controller(IBlockAccess w,BlockPos p){TileEntity t=w.getTileEntity(p);return t instanceof TileRoulette?((TileRoulette)t).controller():null;}
    @Override public boolean onBlockActivated(World w,BlockPos p,IBlockState state,EntityPlayer player,EnumHand hand,EnumFacing face,float x,float y,float z){
        if(!w.isRemote){TileRoulette table=controller(w,p);if(table!=null && table.usable(player))player.openGui(CasinoMod.instance,1,w,table.getPos().getX(),table.getPos().getY(),table.getPos().getZ());}return true;
    }
    @Override public float getPlayerRelativeBlockHardness(IBlockState s,EntityPlayer player,World w,BlockPos p){TileRoulette t=controller(w,p);return t!=null&&t.locked()?0:super.getPlayerRelativeBlockHardness(s,player,w,p);}
    @Override public boolean removedByPlayer(IBlockState state,World w,BlockPos p,EntityPlayer player,boolean willHarvest){TileRoulette t=controller(w,p);if(t!=null)t.dropTable=!player.capabilities.isCreativeMode;return super.removedByPlayer(state,w,p,player,willHarvest);}
    @Override public Item getItemDropped(IBlockState s,Random random,int fortune){return Items.AIR;}
    @Override public ItemStack getPickBlock(IBlockState state,RayTraceResult target,World world,BlockPos pos,EntityPlayer player){return new ItemStack(CasinoMod.ROULETTE_ITEM);}
    @Override public void breakBlock(World w,BlockPos p,IBlockState state){TileRoulette table=controller(w,p);if(table!=null && !w.restoringBlockSnapshots)table.dismantle(p);super.breakBlock(w,p,state);}
}
