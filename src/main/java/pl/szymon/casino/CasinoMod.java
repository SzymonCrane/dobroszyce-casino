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

@Mod(modid=CasinoMod.ID, name="Dobroszyce Casino", version="1.3.0", acceptedMinecraftVersions="[1.12.2]", dependencies="required-after:forge@[14.23.5.2860,);after:baubles")
@Mod.EventBusSubscriber(modid=CasinoMod.ID)
public class CasinoMod {
    public static final String ID="dobroszycecasino";
    public static final CreativeTabs TAB=new CreativeTabs(ID) {
        @Override public ItemStack getTabIconItem(){return new ItemStack(TABLE_ITEM);}
    };
    @Mod.Instance(ID) public static CasinoMod instance;
    @SidedProxy(clientSide="pl.szymon.casino.ClientProxy",serverSide="pl.szymon.casino.CommonProxy")
    public static CommonProxy proxy;
    public static final SimpleNetworkWrapper NETWORK=NetworkRegistry.INSTANCE.newSimpleChannel("dobroszycecasino");
    public static final BlockBlackjack TABLE=new BlockBlackjack();
    public static final ItemBlock TABLE_ITEM=(ItemBlock)new ItemBlock(TABLE).setRegistryName(TABLE.getRegistryName());
    public static final BlockRoulette ROULETTE=new BlockRoulette();
    public static final ItemRoulette ROULETTE_ITEM=new ItemRoulette(ROULETTE);
    public static final Item[] CHIPS=new Item[ChipMath.VALUES.length];
    public static final int[] VALUES=ChipMath.VALUES;
    static { for(int i=0;i<CHIPS.length;i++) CHIPS[i]=new Item().setRegistryName(ID,"chip_"+VALUES[i]).setUnlocalizedName(ID+".chip_"+VALUES[i]).setCreativeTab(TAB); }
    @SubscribeEvent public static void blocks(RegistryEvent.Register<Block> e) { e.getRegistry().registerAll(TABLE,ROULETTE); }
    @SubscribeEvent public static void items(RegistryEvent.Register<Item> e) { e.getRegistry().registerAll(TABLE_ITEM,ROULETTE_ITEM);e.getRegistry().registerAll(CHIPS);e.getRegistry().registerAll(SpecialItems.ALL); }
    @Mod.EventHandler public void preInit(FMLPreInitializationEvent e) {
        ShopConfig.initialize(new java.io.File(e.getModConfigurationDirectory(),"dobroszycecasino-shop.json"),e.getModLog());
        GameRegistry.registerTileEntity(TileBlackjack.class,new ResourceLocation(ID,"blackjack_table"));
        GameRegistry.registerTileEntity(TileRoulette.class,new ResourceLocation(ID,"roulette_table"));
        EntityRegistry.registerModEntity(new ResourceLocation(ID,"dealer"),EntityDealer.class,"dealer",1,instance,64,3,false);
        EntityRegistry.registerModEntity(new ResourceLocation(ID,"shopkeeper"),EntityShopkeeper.class,"shopkeeper",2,instance,64,3,false);
        NETWORK.registerMessage(Snapshot.Handler.class,Snapshot.class,0,Side.CLIENT);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance,proxy);
        proxy.preInit();
    }
    @Mod.EventHandler public void serverStart(FMLServerStartingEvent e) { e.registerServerCommand(new CommandChips());e.registerServerCommand(new CommandShop()); }
}
