package pl.szymon.casino;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CasinoContainer extends Container {
    public final TileBlackjack table;
    private final EntityPlayer player;
    public NBTTagCompound view=new NBTTagCompound();
    private int ticks;
    public CasinoContainer(EntityPlayer p,TileBlackjack t){player=p;table=t;}
    @Override public boolean canInteractWith(EntityPlayer p){return table.usable(p) && (p.world.isRemote || table.seat(p.getUniqueID())>=0);}
    @Override public boolean enchantItem(EntityPlayer p,int id){
        if(p instanceof EntityPlayerMP && p.openContainer==this && canInteractWith(p) && id>=1 && id<=10){table.action((EntityPlayerMP)p,id);send();return true;}return false;
    }
    private void send(){if(player instanceof EntityPlayerMP && player.openContainer==this)CasinoMod.NETWORK.sendTo(new Snapshot(windowId,table.snapshot(player)),(EntityPlayerMP)player);}
    @Override public void detectAndSendChanges(){super.detectAndSendChanges();if(++ticks%5==0)send();}
    @Override public void onContainerClosed(EntityPlayer p){super.onContainerClosed(p);if(!p.world.isRemote)table.leave(p);}
    @Override public ItemStack transferStackInSlot(EntityPlayer p,int index){return ItemStack.EMPTY;}
}
