package pl.szymon.casino;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
public class ItemBrownLeggings extends ItemArmor {
    public ItemBrownLeggings(){super(ArmorMaterial.LEATHER,0,EntityEquipmentSlot.LEGS);}
    @Override public int getColor(ItemStack stack){return 0x704020;}
    @Override public String getArmorTexture(ItemStack stack,Entity entity,EntityEquipmentSlot slot,String type){return "minecraft:textures/models/armor/leather_layer_2"+("overlay".equals(type)?"_overlay":"")+".png";}
}
