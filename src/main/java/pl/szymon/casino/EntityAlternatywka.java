package pl.szymon.casino;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/** Peaceful, persistent visitor. No attack or target tasks are installed. */
public class EntityAlternatywka extends EntityCreature {
    public EntityAlternatywka(World world) {
        super(world);
        setSize(0.6F, 1.8F);
        enablePersistence();
    }
    @Override protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28);
    }
    @Override protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new FollowJakub(this));
        tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.7));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
    }
    @Override protected boolean canDespawn() { return false; }
    @Override protected void dropFewItems(boolean hitByPlayer, int looting) {
        // Exactly 5%, independent of Looting and who dealt the last hit.
        if (rand.nextInt(100) < 5) dropItem(SpecialItems.FEET, 1);
    }
    private static final class FollowJakub extends EntityAIBase {
        private final EntityAlternatywka mob;
        private EntityPlayer player;
        private int repath;
        FollowJakub(EntityAlternatywka mob) { this.mob = mob; setMutexBits(3); }
        @Override public boolean shouldExecute() {
            player = mob.world.getPlayerEntityByName("JakubJanPajdzik");
            return valid();
        }
        private boolean valid() {
            return player != null && player.isEntityAlive() && !player.isSpectator()
                && mob.getDistanceSq(player) <= 25.0;
        }
        @Override public boolean shouldContinueExecuting() { return valid(); }
        @Override public void startExecuting() { repath = 0; }
        @Override public void resetTask() { player = null; mob.getNavigator().clearPath(); }
        @Override public void updateTask() {
            mob.getLookHelper().setLookPositionWithEntity(player, 30.0F, 30.0F);
            if (mob.getDistanceSq(player) <= 2.25) { mob.getNavigator().clearPath(); return; }
            if (--repath <= 0) { repath = 10; mob.getNavigator().tryMoveToEntityLiving(player, 1.25); }
        }
    }
}
