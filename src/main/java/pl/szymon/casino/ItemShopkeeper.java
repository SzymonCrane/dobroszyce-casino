package pl.szymon.casino;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
public class ItemShopkeeper extends Item {
    @Override public EnumActionResult onItemUse(EntityPlayer p,World w,BlockPos pos,EnumHand hand,EnumFacing face,float x,float y,float z){
        if(!p.capabilities.isCreativeMode || !p.canUseCommand(2,"casinoshop"))return EnumActionResult.FAIL;
        BlockPos at=pos.offset(face);if(!p.canPlayerEdit(at,face,p.getHeldItem(hand)))return EnumActionResult.FAIL;
        EntityShopkeeper shop=new EntityShopkeeper(w);shop.anchor(at.getX()+0.5,at.getY(),at.getZ()+0.5,p.rotationYaw+180);
        if(!w.getCollisionBoxes(shop,shop.getEntityBoundingBox()).isEmpty() || !w.checkNoEntityCollision(shop.getEntityBoundingBox()))return EnumActionResult.FAIL;
        if(!w.isRemote && !w.spawnEntity(shop))return EnumActionResult.FAIL;
        return EnumActionResult.SUCCESS;
    }
}
