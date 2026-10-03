package pl.szymon.casino;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
public class ItemFourHandClub extends ItemSword {
    public ItemFourHandClub(){super(ToolMaterial.DIAMOND);setMaxDamage(500);}
    @Override public ActionResult<ItemStack> onItemRightClick(World w,EntityPlayer p,EnumHand hand){
        if(hand!=EnumHand.MAIN_HAND)return new ActionResult<>(EnumActionResult.FAIL,p.getHeldItem(hand));
        if(!w.isRemote)swing(p,p.getHeldItem(hand));return new ActionResult<>(EnumActionResult.SUCCESS,p.getHeldItem(hand));
    }
    public void swing(EntityPlayer p,ItemStack stack){
        if(p.world.isRemote || p.getCooldownTracker().hasCooldown(this))return;
        p.getCooldownTracker().setCooldown(this,20);
        float base=(float)p.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        // Every target gets the full attack value, not vanilla sweep's reduced damage.
        for(EntityLivingBase target:p.world.getEntitiesWithinAABB(EntityLivingBase.class,p.getEntityBoundingBox().grow(3))){
            if(target==p||!target.isEntityAlive()||target.getDistanceSq(p)>9||!p.canEntityBeSeen(target))continue;
            if(target instanceof EntityPlayer && (!p.world.getMinecraftServer().isPVPEnabled() || !p.canAttackPlayer((EntityPlayer)target)))continue;
            float damage=base+EnchantmentHelper.getModifierForCreature(stack,target.getCreatureAttribute());
            if(target.attackEntityFrom(DamageSource.causePlayerDamage(p),damage)){EnchantmentHelper.applyThornEnchantments(target,p);EnchantmentHelper.applyArthropodEnchantments(p,target);}
        }
        if(!p.capabilities.isCreativeMode){int resistance=p.hurtResistantTime;p.hurtResistantTime=0;p.attackEntityFrom(new DamageSource("casinoClubRecoil").setDamageBypassesArmor().setDamageIsAbsolute(),0.5F);p.hurtResistantTime=Math.max(resistance,p.hurtResistantTime);stack.damageItem(1,p);}
        p.resetCooldown();p.swingArm(EnumHand.MAIN_HAND);
        p.world.playSound(null,p.posX,p.posY,p.posZ,SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,SoundCategory.PLAYERS,1,0.8F);
    }
}
