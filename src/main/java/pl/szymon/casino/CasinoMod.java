package pl.szymon.casino;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid=CasinoMod.ID, name="Dobroszyce Casino", version="1.0.0", acceptedMinecraftVersions="[1.12.2]", dependencies="required-after:forge@[14.23.5.2860,)")
@Mod.EventBusSubscriber(modid=CasinoMod.ID)
public class CasinoMod {
    public static final String ID="dobroszycecasino";
    @Mod.Instance(ID) public static CasinoMod instance;
    @SidedProxy(clientSide="pl.szymon.casino.ClientProxy",serverSide="pl.szymon.casino.CommonProxy")
    public static CommonProxy proxy;
    public static final SimpleNetworkWrapper NETWORK=NetworkRegistry.INSTANCE.newSimpleChannel("dobroszycecasino");
    public static final BlockBlackjack TABLE=new BlockBlackjack();
    public static final ItemBlock TABLE_ITEM=(ItemBlock)new ItemBlock(TABLE).setRegistryName(TABLE.getRegistryName());
    public static final Item[] CHIPS=new Item[4];
    public static final int[] VALUES={25,50,100,500};
    static { for(int i=0;i<4;i++) CHIPS[i]=new Item().setRegistryName(ID,"chip_"+VALUES[i]).setUnlocalizedName(ID+".chip_"+VALUES[i]).setCreativeTab(CreativeTabs.MISC); }
    @SubscribeEvent public static void blocks(RegistryEvent.Register<Block> e) { e.getRegistry().register(TABLE); }
    @SubscribeEvent public static void items(RegistryEvent.Register<Item> e) { e.getRegistry().register(TABLE_ITEM);e.getRegistry().registerAll(CHIPS); }
    @Mod.EventHandler public void preInit(FMLPreInitializationEvent e) {
        GameRegistry.registerTileEntity(TileBlackjack.class,new ResourceLocation(ID,"blackjack_table"));
        EntityRegistry.registerModEntity(new ResourceLocation(ID,"dealer"),EntityDealer.class,"dealer",1,instance,64,3,false);
        NETWORK.registerMessage(Snapshot.Handler.class,Snapshot.class,0,Side.CLIENT);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance,proxy);
        proxy.preInit();
    }
    @Mod.EventHandler public void serverStart(FMLServerStartingEvent e) { e.registerServerCommand(new CommandChips()); }
}
