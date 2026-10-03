package pl.szymon.casino;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
public class ItemSmokeGun extends Item {
    public ItemSmokeGun(){setMaxStackSize(1);setMaxDamage(128);}
    @Override public ActionResult<ItemStack> onItemRightClick(World w,EntityPlayer p,EnumHand hand){
        ItemStack s=p.getHeldItem(hand);if(p.getCooldownTracker().hasCooldown(this))return new ActionResult<>(EnumActionResult.FAIL,s);
        if(!w.isRemote){
            // Spawn at the user's feet, independent of camera direction.
            EntityAreaEffectCloud cloud=new EntityAreaEffectCloud(w,p.posX,p.posY+0.05,p.posZ);
            cloud.setOwner(p);cloud.setRadius(3);cloud.setRadiusPerTick(0);cloud.setWaitTime(0);cloud.setDuration(120);cloud.setParticle(EnumParticleTypes.SMOKE_LARGE);
            // No potion effects: this cloud is visual only and cannot deal damage.
            if(w.spawnEntity(cloud)){p.getCooldownTracker().setCooldown(this,80);if(!p.capabilities.isCreativeMode)s.damageItem(1,p);w.playSound(null,p.posX,p.posY,p.posZ,SoundEvents.BLOCK_FIRE_EXTINGUISH,SoundCategory.PLAYERS,0.8F,0.7F);}
        }return new ActionResult<>(EnumActionResult.SUCCESS,s);
    }
}
