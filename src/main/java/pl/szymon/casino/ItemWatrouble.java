package pl.szymon.casino;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.capabilities.*;
/** Optional Baubles adapter: no Baubles classes are linked when the mod is absent. */
public class ItemWatrouble extends Item {
    public ItemWatrouble(){setMaxStackSize(1);}
    public static void worn(EntityPlayer p){
        if(p.world.isRemote)return;
        cleanse(p);
        long now=p.getServer().getWorld(0).getTotalWorldTime();
        NBTTagCompound data=p.getEntityData();
        if(now<data.getLong("casinoWatroubleNext"))return;
        // Do not replace a stronger absorption effect from another mod.
        PotionEffect current=p.getActivePotionEffect(MobEffects.ABSORPTION);
        if(current==null){data.setLong("casinoWatroubleNext",now+600);p.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION,600,0));}
    }
    static void cleanse(EntityPlayer player){
        if(player.world.isRemote)return;
        player.removePotionEffect(MobEffects.POISON);
        player.removePotionEffect(MobEffects.WITHER);
    }
    @Override public ICapabilityProvider initCapabilities(ItemStack stack,NBTTagCompound nbt){
        return CasinoBaubles.capability("AMULET",ItemWatrouble::worn);
    }
}
