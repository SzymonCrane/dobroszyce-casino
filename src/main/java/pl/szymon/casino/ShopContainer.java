package pl.szymon.casino;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
public class ShopContainer extends Container {
    public final EntityShopkeeper shop;
    private final EntityPlayer player;
    private int ticks;
    private long lastBuy=-100;
    private final int[] quoted=new int[ShopCatalog.IDS.length];
    public NBTTagCompound view=new NBTTagCompound();
    public ShopContainer(EntityPlayer p,EntityShopkeeper s){player=p;shop=s;}
    @Override public boolean canInteractWith(EntityPlayer p){return shop.usable(p);}
    @Override public boolean enchantItem(EntityPlayer p,int id){
        if(!(p instanceof EntityPlayerMP)||p!=player||p.openContainer!=this||!canInteractWith(p)||id<0||id>=quoted.length)return false;
        long now=p.world.getTotalWorldTime();if(now-lastBuy<5)return false;lastBuy=now;
        int price=ShopConfig.price(id);
        if(price<=0 || price!=quoted[id]){p.sendMessage(new TextComponentString("Oferta zmieniona lub niedostepna. Sprawdz aktualna cene."));send();return false;}
        // Both debit and delivery run once on the server thread, never trusting client prices.
        if(!Chips.take(p,price)){p.sendMessage(new TextComponentString("Za malo zetonow."));send();return false;}
        Chips.deliver(p,new ItemStack(SpecialItems.PRODUCTS[id]));send();return true;
    }
    private void send(){
        if(!(player instanceof EntityPlayerMP)||player.openContainer!=this)return;
        NBTTagCompound n=new NBTTagCompound();n.setInteger("balance",Chips.balance(player));
        for(int i=0;i<quoted.length;i++)quoted[i]=ShopConfig.price(i);n.setIntArray("prices",quoted);
        CasinoMod.NETWORK.sendTo(new Snapshot(windowId,n),(EntityPlayerMP)player);
    }
    @Override public void detectAndSendChanges(){super.detectAndSendChanges();if(++ticks%5==0)send();}
    @Override public ItemStack transferStackInSlot(EntityPlayer p,int index){return ItemStack.EMPTY;}
}
