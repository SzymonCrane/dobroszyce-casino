package pl.szymon.casino;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
    public void preInit(){}
    public void receive(Snapshot message){}
    @Override public Object getServerGuiElement(int id,EntityPlayer p,World w,int x,int y,int z){TileEntity t=w.getTileEntity(new BlockPos(x,y,z));if(id==0 && t instanceof TileBlackjack && ((TileBlackjack)t).seat(p.getUniqueID())>=0)return new CasinoContainer(p,(TileBlackjack)t);return null;}
    @Override public Object getClientGuiElement(int id,EntityPlayer p,World w,int x,int y,int z){return null;}
}
