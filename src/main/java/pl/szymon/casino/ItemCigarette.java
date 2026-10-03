package pl.szymon.casino;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCigarette extends ItemCasinoConsumable {
    public ItemCigarette(){super(MobEffects.SPEED,0,false);}
    // NONE suppresses vanilla chewing sounds and food fragments on both sides.
    @Override public EnumAction getItemUseAction(ItemStack stack){return EnumAction.NONE;}
    @Override public ItemStack onItemUseFinish(ItemStack stack,World world,EntityLivingBase user){
        SmokingEffects.exhale(user);
        return super.onItemUseFinish(stack,world,user);
    }
}
