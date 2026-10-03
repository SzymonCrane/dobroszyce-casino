package pl.szymon.casino;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RouletteContainer extends Container {
    public final TileRoulette table;
    public NBTTagCompound view=new NBTTagCompound();
    private final EntityPlayer player;
    private int selected=50,ticks;
    private long lastAction=-100;
    public RouletteContainer(EntityPlayer player,TileRoulette table){this.player=player;this.table=table;}
    @Override public boolean canInteractWith(EntityPlayer p){return table.usable(p);}
    @Override public boolean enchantItem(EntityPlayer p,int id){
        if(!(p instanceof EntityPlayerMP)||p.openContainer!=this||!canInteractWith(p))return false;
        long now=p.world.getTotalWorldTime();if(now-lastAction<3)return false;lastAction=now;
        if(id>=60&&id<=65)selected=CasinoMod.VALUES[id-60];else table.action((EntityPlayerMP)p,id,selected);
        send();return true;
    }
    private void send(){if(player instanceof EntityPlayerMP && player.openContainer==this)CasinoMod.NETWORK.sendTo(new Snapshot(windowId,table.snapshot(player,selected)),(EntityPlayerMP)player);}
    @Override public void detectAndSendChanges(){super.detectAndSendChanges();if(++ticks%5==0)send();}
    // Closing the view deliberately keeps placed bets: players can watch the physical wheel.
    @Override public ItemStack transferStackInSlot(EntityPlayer p,int index){return ItemStack.EMPTY;}
}
