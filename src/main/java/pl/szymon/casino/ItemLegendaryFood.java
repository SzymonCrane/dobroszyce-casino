package pl.szymon.casino;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
/** Effects are applied only after consumption and only on the server. */
public class ItemLegendaryFood extends Item {
    private final boolean drink,fullMeal; private final PotionEffect[] effects;
    public ItemLegendaryFood(boolean drink,boolean fullMeal,PotionEffect... effects){this.drink=drink;this.fullMeal=fullMeal;this.effects=effects;setMaxStackSize(16);}
    @Override public int getMaxItemUseDuration(ItemStack stack){return 32;}
    @Override public EnumAction getItemUseAction(ItemStack stack){return drink?EnumAction.DRINK:EnumAction.EAT;}
    @Override public ActionResult<ItemStack> onItemRightClick(World w,EntityPlayer p,EnumHand hand){p.setActiveHand(hand);return new ActionResult<>(EnumActionResult.SUCCESS,p.getHeldItem(hand));}
    @Override public ItemStack onItemUseFinish(ItemStack stack,World w,EntityLivingBase user){
        if(!w.isRemote){
            if(fullMeal&&user instanceof EntityPlayer)((EntityPlayer)user).getFoodStats().addStats(20,1F);
            for(PotionEffect effect:effects)user.addPotionEffect(new PotionEffect(effect));
            if(!(user instanceof EntityPlayer)||!((EntityPlayer)user).capabilities.isCreativeMode)stack.shrink(1);
        }return stack;
    }
}
