package pl.szymon.casino;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
public class ItemFishFeet extends ItemArmor {
    public ItemFishFeet(){super(ArmorMaterial.DIAMOND,0,EntityEquipmentSlot.FEET);}
    @Override public String getArmorTexture(ItemStack stack,Entity entity,EntityEquipmentSlot slot,String type){return CasinoMod.ID+":textures/models/armor/fish_layer_1.png";}
}
