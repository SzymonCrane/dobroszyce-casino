package pl.szymon.casino;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/** A full meal leaves its paper container behind in Survival. */
public class ItemKfcBucket extends ItemLegendaryFood {
    public ItemKfcBucket(){
        super(false,true,new PotionEffect(MobEffects.ABSORPTION,1200,0),new PotionEffect(MobEffects.REGENERATION,200,0));
    }
    @Override public ItemStack onItemUseFinish(ItemStack stack,World world,EntityLivingBase user){
        if(stack.isEmpty())return stack;
        ItemStack remaining=super.onItemUseFinish(stack,world,user);
        if(!world.isRemote && user instanceof EntityPlayer){
            EntityPlayer player=(EntityPlayer)user;
            if(!player.capabilities.isCreativeMode){
                ItemStack bowl=new ItemStack(SpecialItems.PAPER_BOWL);
                if(remaining.isEmpty())return bowl;
                // Adds to inventory, or drops it if the inventory is full.
                Chips.deliver(player,bowl);
            }
        }
        return remaining;
    }
}
