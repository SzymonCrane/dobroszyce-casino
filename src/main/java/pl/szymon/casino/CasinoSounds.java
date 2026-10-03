package pl.szymon.casino;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=CasinoMod.ID)
public final class CasinoSounds {
    public static final SoundEvent NO_MORE_BETS=sound("no_more_bets");
    public static final SoundEvent SMOKE_EXHALE=sound("smoke_exhale");
    public static final SoundEvent SMOKE_INHALE=sound("smoke_inhale");
    private CasinoSounds(){}
    private static SoundEvent sound(String name){
        ResourceLocation id=new ResourceLocation(CasinoMod.ID,name);
        return new SoundEvent(id).setRegistryName(id);
    }
    @SubscribeEvent public static void register(RegistryEvent.Register<SoundEvent> event){
        event.getRegistry().registerAll(NO_MORE_BETS,SMOKE_EXHALE,SMOKE_INHALE);
    }
}
