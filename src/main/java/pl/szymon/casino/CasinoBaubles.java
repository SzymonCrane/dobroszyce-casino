package pl.szymon.casino;
import java.lang.reflect.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.Loader;
/** Baubles remains optional: its API is resolved only when installed. */
public final class CasinoBaubles {
    private CasinoBaubles(){}
    private static Method equipped;
    public static boolean isWorn(EntityPlayer player,Item item){
        if(!Loader.isModLoaded("baubles"))return player.getHeldItemOffhand().getItem()==item;
        try {
            if(equipped==null)equipped=Class.forName("baubles.api.BaublesApi").getMethod("isBaubleEquipped",EntityPlayer.class,Item.class);
            return ((Integer)equipped.invoke(null,player,item))>=0;
        }catch(ReflectiveOperationException e){throw new IllegalStateException("Unsupported Baubles inventory API",e);}
    }
    public static ICapabilityProvider capability(String type,java.util.function.Consumer<EntityPlayer> wornTick){
        if(!Loader.isModLoaded("baubles"))return null;
        try {
            final Capability<?> cap=(Capability<?>)Class.forName("baubles.api.cap.BaublesCapabilities").getField("CAPABILITY_ITEM_BAUBLE").get(null);
            if(cap==null)return null; // Baubles has not injected its capability yet.
            Class<?> api=Class.forName("baubles.api.IBauble");
            final Object slotType=Class.forName("baubles.api.BaubleType").getField(type).get(null);
            final Object bauble=Proxy.newProxyInstance(api.getClassLoader(),new Class<?>[]{api},(proxy,method,args)->{
                switch(method.getName()){
                    case "getBaubleType":return slotType;
                    case "canEquip":case "canUnequip":return true;
                    case "willAutoSync":return false;
                    case "onWornTick":if(args[1] instanceof EntityPlayer)wornTick.accept((EntityPlayer)args[1]);return null;
                    case "hashCode":return System.identityHashCode(proxy);
                    case "equals":return proxy==args[0];
                    case "toString":return "Casino Bauble: "+type;
                    default:return null;
                }
            });
            return new ICapabilityProvider(){
                @Override public boolean hasCapability(Capability<?> requested,EnumFacing side){return requested==cap;}
                @Override @SuppressWarnings("unchecked") public <T>T getCapability(Capability<T> requested,EnumFacing side){return requested==cap?(T)bauble:null;}
            };
        }catch(ReflectiveOperationException e){throw new IllegalStateException("Unsupported Baubles API",e);}
    }
}
