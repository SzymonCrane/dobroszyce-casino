package pl.szymon.casino;

import java.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraft.world.World;
import net.minecraft.world.storage.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/** Credits stay available when a player disconnects or their table is removed. */
@Mod.EventBusSubscriber(modid=CasinoMod.ID)
public class PayoutLedger extends WorldSavedData {
    private final Map<UUID,Integer> credits=new HashMap<>();
    public PayoutLedger(){super("dobroszycecasino_payouts");}
    public PayoutLedger(String name){super(name);}
    public static PayoutLedger get(World world) {
        MapStorage storage=world.getMinecraftServer().getWorld(0).getMapStorage();
        PayoutLedger data=(PayoutLedger)storage.getOrLoadData(PayoutLedger.class,"dobroszycecasino_payouts");
        if(data==null){data=new PayoutLedger();storage.setData("dobroszycecasino_payouts",data);}return data;
    }

    public void credit(UUID id,int amount) {
        if(id!=null && amount>0) {
            credits.put(id,credits.getOrDefault(id,0)+amount);
            markDirty();
        }
    }

    public void claim(EntityPlayer p) {
        Integer amount=credits.remove(p.getUniqueID());
        if(amount!=null) {
            markDirty();
            Chips.give(p,amount);
        }

    }
    @Override public void readFromNBT(NBTTagCompound tag) {
        credits.clear();NBTTagList list=tag.getTagList("credits",10);
        for(int i=0;i<list.tagCount();i++){NBTTagCompound t=list.getCompoundTagAt(i);if(t.hasUniqueId("player"))credits.put(t.getUniqueId("player"),t.getInteger("amount"));}
    }
    @Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList list=new NBTTagList();for(Map.Entry<UUID,Integer> e:credits.entrySet()){NBTTagCompound t=new NBTTagCompound();t.setUniqueId("player",e.getKey());t.setInteger("amount",e.getValue());list.appendTag(t);}tag.setTag("credits",list);return tag;
    }
    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e){if(!e.player.world.isRemote)get(e.player.world).claim(e.player);}
}
