package pl.szymon.casino;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

/** Vanilla spawn-egg handling (including permissions), visible in our own tab. */
public class ItemAlternatywkaEgg extends ItemMonsterPlacer {
    @Override public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            ItemStack egg = new ItemStack(this);
            applyEntityIdToItemStack(egg, new ResourceLocation(CasinoMod.ID, "alternatywka"));
            items.add(egg);
        }
    }
    private void prepare(ItemStack stack) {
        applyEntityIdToItemStack(stack, new ResourceLocation(CasinoMod.ID, "alternatywka"));
    }
    @Override public net.minecraft.util.EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player,
            net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, net.minecraft.util.EnumHand hand,
            net.minecraft.util.EnumFacing face, float x, float y, float z) {
        prepare(player.getHeldItem(hand));
        return super.onItemUse(player, world, pos, hand, face, x, y, z);
    }
    @Override public net.minecraft.util.ActionResult<ItemStack> onItemRightClick(net.minecraft.world.World world,
            net.minecraft.entity.player.EntityPlayer player, net.minecraft.util.EnumHand hand) {
        prepare(player.getHeldItem(hand));
        return super.onItemRightClick(world, player, hand);
    }
    @Override public String getItemStackDisplayName(ItemStack stack) {
        return net.minecraft.util.text.translation.I18n.translateToLocal(getUnlocalizedName()+".name");
    }
}
