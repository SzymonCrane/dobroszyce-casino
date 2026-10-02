package pl.szymon.casino;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.*;

public final class Chips {
    private Chips() {}
    public static int value(ItemStack stack) {
        if(stack.isEmpty())return 0;
        for(int i=0;i<4;i++)if(stack.getItem()==CasinoMod.CHIPS[i])return CasinoMod.VALUES[i];
        return 0;
    }
    public static int balance(EntityPlayer p) { int sum=0;for(ItemStack s:p.inventory.mainInventory)sum+=value(s)*s.getCount();return sum; }
    public static boolean take(EntityPlayer p,int amount) {
        if(amount<=0 || amount%25!=0 || balance(p)<amount)return false;
        int removed=0;
        for(int i=0;i<p.inventory.mainInventory.size() && removed<amount;i++) {
            ItemStack s=p.inventory.mainInventory.get(i);int v=value(s);if(v==0)continue;
            int count=Math.min(s.getCount(),(amount-removed+v-1)/v);s.shrink(count);removed+=count*v;
        }
        give(p,removed-amount);p.inventory.markDirty();return true;
    }
    public static void give(EntityPlayer p,int amount) {
        for(int i=3;i>=0;i--) {
            int count=amount/CasinoMod.VALUES[i];amount%=CasinoMod.VALUES[i];
            while(count>0) { int n=Math.min(64,count);deliver(p,new ItemStack(CasinoMod.CHIPS[i],n));count-=n; }
        }
    }
    public static void deliver(EntityPlayer p,ItemStack s) {
        p.inventory.addItemStackToInventory(s);
        if (!s.isEmpty()) {
            EntityItem dropped=p.dropItem(s,false);
            if (dropped!=null) {
                dropped.setOwner(p.getName());
                dropped.setNoPickupDelay();
            }
        }
        p.inventory.markDirty();
    }
    public static boolean buy(EntityPlayer p) {
        for(ItemStack s:p.inventory.mainInventory)if(!s.isEmpty() && s.getItem()==Items.EMERALD){s.shrink(1);give(p,100);return true;}
        return false;
    }
    public static boolean sell(EntityPlayer p) { if(!take(p,100))return false;deliver(p,new ItemStack(Items.EMERALD));return true; }
}
