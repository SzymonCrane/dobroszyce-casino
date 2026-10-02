package pl.szymon.casino;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityDealer extends EntityVillager {
    private BlockPos tablePos;
    public EntityDealer(World w){super(w);setNoAI(true);setNoGravity(true);enablePersistence();setCustomNameTag("Krupier Blackjack");setAlwaysRenderNameTag(true);setSize(0.6F,1.95F);}
    public void bind(BlockPos p){tablePos=p.toImmutable();}
    public boolean belongsTo(BlockPos p){return p.equals(tablePos);}
    public void reposition(){
        if(tablePos==null || !world.isBlockLoaded(tablePos) || world.getBlockState(tablePos).getBlock()!=CasinoMod.TABLE)return;
        EnumFacing front=world.getBlockState(tablePos).getValue(BlockBlackjack.FACING);
        setPosition(tablePos.getX()+0.5-front.getFrontOffsetX()*1.25,tablePos.getY(),tablePos.getZ()+0.5-front.getFrontOffsetZ()*1.25);
        rotationYaw=front.getHorizontalAngle();rotationYawHead=rotationYaw;renderYawOffset=rotationYaw;
    }
    @Override public void onLivingUpdate(){
        super.onLivingUpdate();motionX=motionY=motionZ=0;
        if(!world.isRemote && ticksExisted%20==0){
            if(tablePos==null){setDead();return;}
            if(world.isBlockLoaded(tablePos)){if(!(world.getTileEntity(tablePos) instanceof TileBlackjack)){setDead();return;}
                if(!((TileBlackjack)world.getTileEntity(tablePos)).ownsDealer(getUniqueID())){setDead();return;}
                reposition();}
        }
    }
    @Override public boolean processInteract(EntityPlayer p,EnumHand hand){
        if(!world.isRemote && tablePos!=null && world.isBlockLoaded(tablePos)){TileEntity t=world.getTileEntity(tablePos);if(t instanceof TileBlackjack && ((TileBlackjack)t).usable(p) && ((TileBlackjack)t).join(p)>=0)p.openGui(CasinoMod.instance,0,world,tablePos.getX(),tablePos.getY(),tablePos.getZ());}return true;
    }
    @Override public boolean attackEntityFrom(DamageSource s,float amount){return false;}
    @Override public boolean canBePushed(){return false;}
    @Override protected boolean canDespawn(){return false;}
    @Override public void writeEntityToNBT(NBTTagCompound tag){super.writeEntityToNBT(tag);if(tablePos!=null)tag.setLong("casinoTable",tablePos.toLong());}
    @Override public void readEntityFromNBT(NBTTagCompound tag){super.readEntityFromNBT(tag);if(tag.hasKey("casinoTable"))tablePos=BlockPos.fromLong(tag.getLong("casinoTable"));setNoAI(true);setNoGravity(true);}
}
