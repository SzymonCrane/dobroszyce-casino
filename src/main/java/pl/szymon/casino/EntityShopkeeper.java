package pl.szymon.casino;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

/** Placed once; permanent, invulnerable and anchored even after world reload. */
public class EntityShopkeeper extends EntityVillager {
    private double anchorX,anchorY,anchorZ;
    private float anchorYaw;
    private boolean anchored;
    public EntityShopkeeper(World w){super(w);setNoAI(true);setNoGravity(true);enablePersistence();setCustomNameTag("Sklepikarz kasyna");setAlwaysRenderNameTag(true);}
    public void anchor(double x,double y,double z,float yaw){anchorX=x;anchorY=y;anchorZ=z;anchorYaw=yaw;anchored=true;setLocationAndAngles(x,y,z,yaw,0);}
    @Override public void onLivingUpdate(){
        super.onLivingUpdate();motionX=motionY=motionZ=0;
        if(!world.isRemote){if(!anchored)anchor(posX,posY,posZ,rotationYaw);setPosition(anchorX,anchorY,anchorZ);rotationYaw=rotationYawHead=renderYawOffset=anchorYaw;}
    }
    public boolean usable(EntityPlayer p){return isEntityAlive() && p.isEntityAlive() && p.world==world && getDistanceSq(p)<64;}
    @Override public boolean processInteract(EntityPlayer p,EnumHand hand){
        if(!world.isRemote && usable(p)){
            if(p.isSneaking() && p.capabilities.isCreativeMode && p.canUseCommand(2,"casinoshop")){setDead();return true;}
            p.openGui(CasinoMod.instance,2,world,getEntityId(),0,0);
        }return true;
    }
    @Override public boolean attackEntityFrom(DamageSource s,float amount){return false;}
    @Override public void onStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt lightning){}
    @Override public boolean canBePushed(){return false;}
    @Override public boolean canBeLeashedTo(EntityPlayer p){return false;}
    @Override public boolean startRiding(net.minecraft.entity.Entity e,boolean force){return false;}
    @Override protected boolean canDespawn(){return false;}
    @Override public void writeEntityToNBT(NBTTagCompound n){super.writeEntityToNBT(n);n.setBoolean("anchored",anchored);n.setDouble("shopX",anchorX);n.setDouble("shopY",anchorY);n.setDouble("shopZ",anchorZ);n.setFloat("shopYaw",anchorYaw);}
    @Override public void readEntityFromNBT(NBTTagCompound n){super.readEntityFromNBT(n);if(n.getBoolean("anchored"))anchor(n.getDouble("shopX"),n.getDouble("shopY"),n.getDouble("shopZ"),n.getFloat("shopYaw"));setNoAI(true);setNoGravity(true);}
}
