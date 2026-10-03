package pl.szymon.casino;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
public class ItemCasinoConsumable extends Item {
    private final Potion effect;private final int amplifier;private final boolean bottle;
    public ItemCasinoConsumable(Potion effect,int amplifier,boolean bottle){this.effect=effect;this.amplifier=amplifier;this.bottle=bottle;setMaxStackSize(bottle?1:16);}
    @Override public int getMaxItemUseDuration(ItemStack s){return 32;}
    @Override public EnumAction getItemUseAction(ItemStack s){return bottle?EnumAction.DRINK:EnumAction.EAT;}
    @Override public ActionResult<ItemStack> onItemRightClick(World w,EntityPlayer p,EnumHand hand){p.setActiveHand(hand);return new ActionResult<>(EnumActionResult.SUCCESS,p.getHeldItem(hand));}
    @Override public ItemStack onItemUseFinish(ItemStack s,World w,EntityLivingBase user){
        if(!w.isRemote){user.addPotionEffect(new PotionEffect(effect,600,amplifier));
            if(!(user instanceof EntityPlayer)||!((EntityPlayer)user).capabilities.isCreativeMode){s.shrink(1);if(bottle){if(s.isEmpty())return new ItemStack(Items.GLASS_BOTTLE);if(user instanceof EntityPlayer)Chips.deliver((EntityPlayer)user,new ItemStack(Items.GLASS_BOTTLE));}}
        }return s;
    }
}
