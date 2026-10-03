package pl.szymon.casino;

import java.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemRoulette extends ItemBlock {
    public ItemRoulette(BlockRoulette block){super(block);setRegistryName(block.getRegistryName());}
    @Override public boolean placeBlockAt(ItemStack stack,EntityPlayer player,World world,BlockPos pos,EnumFacing side,float x,float y,float z,IBlockState state){
        List<BlockPos> spaces=new ArrayList<>();List<IBlockState> old=new ArrayList<>();
        for(int dx=0;dx<3;dx++)for(int dz=0;dz<2;dz++){
            BlockPos p=pos.add(dx,0,dz);
            if(!world.isBlockLoaded(p)||!world.getWorldBorder().contains(p)||!player.canPlayerEdit(p,side,stack)||!world.mayPlace(block,p,false,side,player)){
                if(!world.isRemote)player.sendMessage(new TextComponentString("Ruletka potrzebuje wolnego obszaru 3 x 2 bloki (od miejsca klikniecia na wschod i poludnie)."));return false;
            }spaces.add(p);old.add(world.getBlockState(p));
        }
        if(!super.placeBlockAt(stack,player,world,pos,side,x,y,z,state))return false;
        TileRoulette master=(TileRoulette)world.getTileEntity(pos);master.configure(pos);
        for(BlockPos p:spaces){
            if(!p.equals(pos) && !world.setBlockState(p,state,3)){
                for(int i=0;i<spaces.size();i++)world.setBlockState(spaces.get(i),old.get(i),3);return false;
            }
            TileEntity te=world.getTileEntity(p);if(te instanceof TileRoulette)((TileRoulette)te).configure(pos);
        }
        master.formed=true;
        for(BlockPos p:spaces){TileEntity te=world.getTileEntity(p);if(te instanceof TileRoulette)((TileRoulette)te).sync();}
        return true;
    }
}
