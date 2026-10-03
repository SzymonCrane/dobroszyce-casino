package pl.szymon.casino;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
@Mod.EventBusSubscriber(modid=CasinoMod.ID)
public class LegendaryEvents {
    @SubscribeEvent public static void fishingLoot(net.minecraftforge.event.LootTableLoadEvent event){
        if(!event.getName().toString().equals("minecraft:gameplay/fishing/fish"))return;
        // Add an independent 1% bonus only to fish catches, preserving other mods' loot.
        net.minecraft.world.storage.loot.LootEntry entry=new net.minecraft.world.storage.loot.LootEntryItem(SpecialItems.KARP_GEORGE,1,0,new net.minecraft.world.storage.loot.functions.LootFunction[0],new net.minecraft.world.storage.loot.conditions.LootCondition[0],"casino_karp_george");
        event.getTable().addPool(new net.minecraft.world.storage.loot.LootPool(new net.minecraft.world.storage.loot.LootEntry[]{entry},new net.minecraft.world.storage.loot.conditions.LootCondition[]{new net.minecraft.world.storage.loot.conditions.RandomChance(0.01F)},new net.minecraft.world.storage.loot.RandomValueRange(1),new net.minecraft.world.storage.loot.RandomValueRange(0),"casino_rare_fish"));
    }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event){
        EntityPlayer p=event.player;
        if(event.phase!=TickEvent.Phase.END||p.world.isRemote||p.isDead||p.isSpectator())return;
        // Without Baubles the offhand is the explicitly supported fallback slot.
        if(!net.minecraftforge.fml.common.Loader.isModLoaded("baubles")&&p.getHeldItemOffhand().getItem()==SpecialItems.WATROUBLE)ItemWatrouble.worn(p);
        if(!CasinoBaubles.isWorn(p,SpecialItems.BROWN_LEGGINGS))return;
        if(p.ticksExisted%20==0)p.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY,40,0,false,false));
        long now=p.getServer().getWorld(0).getTotalWorldTime();
        String key="casinoLeggingsNext";
        if(!p.getEntityData().hasKey(key)){p.getEntityData().setLong(key,now+200);return;}
        if(now<p.getEntityData().getLong(key))return;
        p.getEntityData().setLong(key,now+200+p.getRNG().nextInt(201));
        if(p.isRiding()||p.isPlayerSleeping())return;
        World w=p.world;
        for(int attempt=0;attempt<16;attempt++){
            BlockPos target=new BlockPos(p.posX+p.getRNG().nextInt(17)-8,p.posY+p.getRNG().nextInt(7)-3,p.posZ+p.getRNG().nextInt(17)-8);
            if(target.getY()<1||target.getY()>w.getActualHeight()-3||!w.isBlockLoaded(target)||!w.getWorldBorder().contains(target))continue;
            if(!w.getBlockState(target.down()).isFullCube()||w.getBlockState(target.down()).getBlock()==Blocks.MAGMA)continue;
            if(!w.isAirBlock(target)||!w.isAirBlock(target.up()))continue;
            if(p.attemptTeleport(target.getX()+0.5,target.getY(),target.getZ()+0.5)){p.fallDistance=0;break;}
        }
    }
}
