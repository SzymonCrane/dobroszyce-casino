package pl.szymon.casino;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
public class ItemSmokeGun extends Item {
    public ItemSmokeGun(){setMaxStackSize(1);setMaxDamage(128);}
    @Override public int getMaxItemUseDuration(ItemStack stack){return 32;}
    @Override public EnumAction getItemUseAction(ItemStack stack){return EnumAction.NONE;}
    @Override public ActionResult<ItemStack> onItemRightClick(World w,EntityPlayer p,EnumHand hand){
        ItemStack s=p.getHeldItem(hand);if(p.getCooldownTracker().hasCooldown(this))return new ActionResult<>(EnumActionResult.FAIL,s);
        p.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS,s);
    }
    @Override public void onUsingTick(ItemStack stack,EntityLivingBase user,int count){
        SmokingEffects.inhaleTick(user,count);
    }
    @Override public ItemStack onItemUseFinish(ItemStack s,World w,EntityLivingBase user){
        if(!w.isRemote && user instanceof EntityPlayer){
            EntityPlayer p=(EntityPlayer)user;
            if(p.getCooldownTracker().hasCooldown(this))return s;
            EntityAreaEffectCloud cloud=new EntityAreaEffectCloud(w,p.posX,p.posY+0.05,p.posZ);
            cloud.setOwner(p);cloud.setRadius(3);cloud.setRadiusPerTick(0);cloud.setWaitTime(0);cloud.setDuration(120);cloud.setParticle(EnumParticleTypes.SMOKE_LARGE);
            // Visual cloud only; costs and cooldown apply only after a completed puff.
            if(w.spawnEntity(cloud)){
                p.getCooldownTracker().setCooldown(this,80);
                if(!p.capabilities.isCreativeMode)s.damageItem(1,p);
                SmokingEffects.exhale(p);
            }
        }
        return s;
    }
}
