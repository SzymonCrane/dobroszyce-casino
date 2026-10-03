package pl.szymon.casino;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public final class KfcRecipes {
    private KfcRecipes(){}
    public static void registerSmelting(){
        FurnaceRecipes furnace=FurnaceRecipes.instance();
        // Match the old kfc-mod: replace raw chicken's vanilla furnace result.
        // Remove prior entries first; adding alone leaves ambiguous lookup order.
        Iterator<Map.Entry<ItemStack,ItemStack>> recipes=furnace.getSmeltingList().entrySet().iterator();
        while(recipes.hasNext()){
            if(recipes.next().getKey().getItem()==Items.CHICKEN)recipes.remove();
        }
        furnace.addSmelting(Items.CHICKEN,new ItemStack(SpecialItems.KFC_WING),0.35F);
    }
}
