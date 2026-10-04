package pl.szymon.casino;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/** Blocks applications and clears existing debuffs before they can tick. */
@Mod.EventBusSubscriber(modid=CasinoMod.ID)
public final class WatroubleEvents {
    private WatroubleEvents() {}
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void potion(PotionEvent.PotionApplicableEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        Potion potion = event.getPotionEffect().getPotion();
        if (potion != MobEffects.POISON && potion != MobEffects.WITHER) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!player.world.isRemote && CasinoBaubles.isWorn(player, SpecialItems.WATROUBLE))
            event.setResult(Event.Result.DENY);
    }
    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.player.world.isRemote
                && CasinoBaubles.isWorn(event.player, SpecialItems.WATROUBLE))
            ItemWatrouble.cleanse(event.player);
    }
}
