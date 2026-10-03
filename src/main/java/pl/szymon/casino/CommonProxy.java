package pl.szymon.casino;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
    public void preInit(){}
    public void receive(Snapshot message){}
    @Override public Object getServerGuiElement(int id,EntityPlayer p,World w,int x,int y,int z){if(id==2){net.minecraft.entity.Entity entity=w.getEntityByID(x);return entity instanceof EntityShopkeeper && ((EntityShopkeeper)entity).usable(p)?new ShopContainer(p,(EntityShopkeeper)entity):null;}TileEntity t=w.getTileEntity(new BlockPos(x,y,z));if(id==0 && t instanceof TileBlackjack && ((TileBlackjack)t).seat(p.getUniqueID())>=0)return new CasinoContainer(p,(TileBlackjack)t);if(id==1 && t instanceof TileRoulette && ((TileRoulette)t).usable(p))return new RouletteContainer(p,(TileRoulette)t);return null;}
    @Override public Object getClientGuiElement(int id,EntityPlayer p,World w,int x,int y,int z){return null;}
}
