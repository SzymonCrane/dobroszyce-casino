package pl.szymon.casino;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
/** Effects run in player/collision events, allowing client-side water prediction. */
public class ItemCasinoBauble extends Item {
    private final String baubleType;
    public ItemCasinoBauble(String baubleType){this.baubleType=baubleType;setMaxStackSize(1);}
    @Override public ICapabilityProvider initCapabilities(ItemStack stack,NBTTagCompound nbt){
        return CasinoBaubles.capability(baubleType,player->{});
    }
}
