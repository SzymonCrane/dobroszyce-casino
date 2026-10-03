package pl.szymon.casino;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public final class SmokingEffects {
    private SmokingEffects(){}
    public static void inhaleTick(EntityLivingBase user,int remaining){
        // Short bursts follow actual use ticks; releasing stops new sounds immediately.
        if(!user.world.isRemote && remaining>8 && remaining%8==0){
            user.world.playSound(null,user.posX,user.posY+user.getEyeHeight(),user.posZ,
                CasinoSounds.SMOKE_INHALE,SoundCategory.PLAYERS,0.9F,1.0F);
        }
    }
    public static void exhale(EntityLivingBase user){
        if(!(user.world instanceof WorldServer))return;
        WorldServer world=(WorldServer)user.world;
        Vec3d look=user.getLookVec();
        double x=user.posX+look.x*0.45,y=user.posY+user.getEyeHeight()-0.12+look.y*0.45,z=user.posZ+look.z*0.45;
        // Server broadcast includes the user exactly once and nearby players.
        world.playSound(null,x,y,z,CasinoSounds.SMOKE_EXHALE,SoundCategory.PLAYERS,0.65F,1.0F);
        for(int i=0;i<12;i++){
            double spread=0.04;
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,x+world.rand.nextGaussian()*spread,y+world.rand.nextGaussian()*spread,z+world.rand.nextGaussian()*spread,
                0,look.x*0.09,look.y*0.09+0.02,look.z*0.09,1.0);
        }
    }
}
