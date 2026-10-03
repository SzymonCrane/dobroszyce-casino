package pl.szymon.casino;
import java.lang.reflect.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.Loader;
/** Optional Baubles adapter: no Baubles classes are linked when the mod is absent. */
public class ItemWatrouble extends Item {
    public ItemWatrouble(){setMaxStackSize(1);}
    public static void worn(EntityPlayer p){
        if(p.world.isRemote)return;
        long now=p.getServer().getWorld(0).getTotalWorldTime();
        NBTTagCompound data=p.getEntityData();
        if(now<data.getLong("casinoWatroubleNext"))return;
        // Do not replace a stronger absorption effect from another mod.
        PotionEffect current=p.getActivePotionEffect(MobEffects.ABSORPTION);
        if(current==null){data.setLong("casinoWatroubleNext",now+600);p.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION,600,0));}
    }
    @Override public ICapabilityProvider initCapabilities(ItemStack stack,NBTTagCompound nbt){
        if(!Loader.isModLoaded("baubles"))return null;
        try {
            final Capability<?> cap=(Capability<?>)Class.forName("baubles.api.cap.BaublesCapabilities").getField("CAPABILITY_ITEM_BAUBLE").get(null);
            if(cap==null)return null; // Baubles has not injected its capability yet.
            Class<?> api=Class.forName("baubles.api.IBauble");
            final Object amulet=Class.forName("baubles.api.BaubleType").getField("AMULET").get(null);
            final Object bauble=Proxy.newProxyInstance(api.getClassLoader(),new Class<?>[]{api},(proxy,method,args)->{
                switch(method.getName()){
                    case "getBaubleType":return amulet;
                    case "canEquip":case "canUnequip":return true;
                    case "willAutoSync":return false;
                    case "onWornTick":if(args[1] instanceof EntityPlayer)worn((EntityPlayer)args[1]);return null;
                    case "hashCode":return System.identityHashCode(proxy);
                    case "equals":return proxy==args[0];
                    case "toString":return "Casino Watrouble Bauble";
                    default:return null;
                }
            });
            return new ICapabilityProvider(){
                @Override public boolean hasCapability(Capability<?> requested,EnumFacing side){return requested==cap;}
                @Override @SuppressWarnings("unchecked") public <T>T getCapability(Capability<T> requested,EnumFacing side){return requested==cap?(T)bauble:null;}
            };
        }catch(ReflectiveOperationException e){throw new IllegalStateException("Unsupported Baubles API for Watrouble",e);}
    }
}
