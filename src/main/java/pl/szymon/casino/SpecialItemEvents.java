package pl.szymon.casino;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=CasinoMod.ID)
public class SpecialItemEvents {
    @SubscribeEvent public static void water(GetCollisionBoxesEvent e){
        if(!(e.getEntity() instanceof EntityPlayer))return;
        EntityPlayer p=(EntityPlayer)e.getEntity();
        if(!CasinoBaubles.isWorn(p,SpecialItems.FISHS_FEET)||p.isSneaking()||p.isSpectator()||p.capabilities.isFlying)return;
        // Add collisions only at the surface below the feet, never trapping a submerged player.
        int feet=MathHelper.floor(p.getEntityBoundingBox().minY);
        AxisAlignedBB query=e.getAabb();
        for(int y=Math.max(MathHelper.floor(query.minY)-1,feet-16);y<=feet;y++)
        for(int x=MathHelper.floor(p.posX-0.8);x<=MathHelper.floor(p.posX+0.8);x++)for(int z=MathHelper.floor(p.posZ-0.8);z<=MathHelper.floor(p.posZ+0.8);z++){
            BlockPos pos=new BlockPos(x,y,z);
            if(e.getWorld().getBlockState(pos).getMaterial()!=Material.WATER || e.getWorld().getBlockState(pos.up()).getMaterial()==Material.WATER)continue;
            if(p.getEntityBoundingBox().minY<y+0.95)continue;
            AxisAlignedBB surface=new AxisAlignedBB(x,y,z,x+1,y+1,z+1);
            if(surface.intersects(query))e.getCollisionBoxesList().add(surface);
        }
    }
    @SubscribeEvent public static void club(AttackEntityEvent e){
        if(e.getEntityPlayer().getHeldItemMainhand().getItem()==SpecialItems.CLUB){e.setCanceled(true);if(!e.getEntityPlayer().world.isRemote)((ItemFourHandClub)SpecialItems.CLUB).swing(e.getEntityPlayer(),e.getEntityPlayer().getHeldItemMainhand());}
    }
}
