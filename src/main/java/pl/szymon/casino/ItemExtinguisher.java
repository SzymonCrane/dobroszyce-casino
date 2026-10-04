package pl.szymon.casino;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.common.MinecraftForge;

/** Short, server-authoritative spray; solids stop it and protected blocks are skipped. */
public class ItemExtinguisher extends Item {
    public ItemExtinguisher() { setMaxStackSize(1); setMaxDamage(256); }
    @Override public int getMaxItemUseDuration(ItemStack stack) { return 72000; }
    @Override public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.NONE; }
    @Override public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
    @Override public void onUsingTick(ItemStack stack, EntityLivingBase user, int count) {
        if (user.world.isRemote || !(user instanceof EntityPlayerMP) || count % 4 != 0) return;
        EntityPlayerMP player = (EntityPlayerMP) user;
        WorldServer world = (WorldServer) user.world;
        Vec3d start = user.getPositionEyes(1.0F), direction = user.getLookVec();
        Vec3d end = start.add(direction.scale(6.0));
        RayTraceResult hit = world.rayTraceBlocks(start, end, false, true, false);
        if (hit != null) end = hit.hitVec;
        double length = start.distanceTo(end);
        // Liquids have no collision box. Stop explicitly at the first lava block,
        // so one burst cannot cool an entire column or spray through new obsidian.
        BlockPos lava = null;
        for (double distance = 0; distance <= length; distance += 0.125) {
            Vec3d at = start.add(direction.scale(distance));
            BlockPos pos = new BlockPos(at);
            if (world.getBlockState(pos).getMaterial() == Material.LAVA) {
                lava = pos;
                length = distance;
                end = at;
                hit = null;
                break;
            }
        }
        if (lava != null) extinguishBlock(world, player, stack, lava);
        // Count-zero particles use the supplied vector as velocity: visibly directional white cloud.
        world.spawnParticle(EnumParticleTypes.CLOUD, start.x, start.y - 0.15, start.z,
            0, direction.x, direction.y, direction.z, 0.35);
        for (double distance = 0.5; distance <= length; distance += 0.5) {
            Vec3d at = start.add(direction.scale(distance));
            world.spawnParticle(EnumParticleTypes.CLOUD, at.x, at.y, at.z, 2, 0.13, 0.13, 0.13, 0.01);
            extinguishBlock(world, player, stack, new BlockPos(at));
        }
        if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK) {
            extinguishBlock(world, player, stack, hit.getBlockPos());
            extinguishBlock(world, player, stack, hit.getBlockPos().offset(hit.sideHit));
        }
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(player,
                new AxisAlignedBB(start.x,start.y,start.z,end.x,end.y,end.z).grow(0.5))) {
            Vec3d center = entity.getPositionVector().addVector(0, entity.height * 0.5, 0);
            double along = center.subtract(start).dotProduct(direction);
            Vec3d nearest = start.add(direction.scale(MathHelper.clamp(along, 0.0, length)));
            if (along >= 0 && along <= length && center.squareDistanceTo(nearest) <= 0.64
                    && world.rayTraceBlocks(start, center, false, true, false) == null) entity.extinguish();
        }
        if (count % 12 == 0) world.playSound(null, user.posX, user.posY, user.posZ,
            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.45F, 1.3F);
        if (!player.capabilities.isCreativeMode) stack.damageItem(1, player);
        if (stack.isEmpty()) player.stopActiveHand();
    }
    private void extinguishBlock(WorldServer world, EntityPlayerMP player, ItemStack stack, BlockPos pos) {
        if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos)
                || !player.canPlayerEdit(pos, EnumFacing.UP, stack)) return;
        IBlockState state = world.getBlockState(pos);
        IBlockState replacement;
        if (state.getBlock() == Blocks.FIRE) replacement = Blocks.AIR.getDefaultState();
        else if (state.getMaterial() == Material.LAVA && state.getBlock() instanceof BlockLiquid)
            replacement = state.getValue(BlockLiquid.LEVEL) == 0
                ? Blocks.OBSIDIAN.getDefaultState() : Blocks.COBBLESTONE.getDefaultState();
        else return;
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, player);
        if (MinecraftForge.EVENT_BUS.post(event)) return;
        world.setBlockState(pos, replacement, 3);
    }
}
