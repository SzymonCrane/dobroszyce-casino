package pl.szymon.casino;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid=CasinoMod.ID,value=Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @SubscribeEvent public static void tooltip(net.minecraftforge.event.entity.player.ItemTooltipEvent e){
        net.minecraft.item.Item item=e.getItemStack().getItem();
        for(int i=0;i<SpecialItems.PRODUCTS.length;i++)if(item==SpecialItems.PRODUCTS[i])e.getToolTip().add(net.minecraft.util.text.TextFormatting.GRAY+ShopCatalog.HELP[i]);
        if(item==SpecialItems.FISHS_FEET || item==SpecialItems.BROWN_LEGGINGS)e.getToolTip().add("Bez Baubles: trzymaj w drugiej rece");
        if(item==SpecialItems.EXTINGUISHER)e.getToolTip().add(net.minecraft.util.text.translation.I18n.translateToLocal("tooltip.dobroszycecasino.extinguisher"));
        if(item==SpecialItems.KARP_GEORGE)e.getToolTip().add("Rzadka ryba (1% polowow ryb). Skladnik Fish's Feet.");
    }
    @Override public void preInit(){
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileRoulette.class,new RenderRoulette());
        RenderingRegistry.registerEntityRenderingHandler(EntityShopkeeper.class,ShopkeeperRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDealer.class,DealerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(net.minecraft.entity.passive.EntityWolf.class,RenderNamedWolf::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAlternatywka.class,RenderAlternatywka::new);
        RenderingRegistry.registerEntityRenderingHandler(net.minecraft.entity.passive.EntityOcelot.class,RenderNamedOcelot::new);
    }
    @SubscribeEvent public static void models(ModelRegistryEvent e){
        ModelLoader.setCustomModelResourceLocation(CasinoMod.TABLE_ITEM,0,new ModelResourceLocation(CasinoMod.TABLE.getRegistryName(),"inventory"));
        ModelLoader.setCustomModelResourceLocation(CasinoMod.ROULETTE_ITEM,0,new ModelResourceLocation(CasinoMod.ROULETTE.getRegistryName(),"inventory"));
        for(net.minecraft.item.Item item:SpecialItems.ALL)ModelLoader.setCustomModelResourceLocation(item,0,new ModelResourceLocation(item.getRegistryName(),"inventory"));
        for(net.minecraft.item.Item item:CasinoMod.CHIPS)ModelLoader.setCustomModelResourceLocation(item,0,new ModelResourceLocation(item.getRegistryName(),"inventory"));
    }
    @Override public Object getClientGuiElement(int id,EntityPlayer p,World w,int x,int y,int z){if(id==2){net.minecraft.entity.Entity entity=w.getEntityByID(x);return entity instanceof EntityShopkeeper?new GuiShop(new ShopContainer(p,(EntityShopkeeper)entity)):null;}TileEntity t=w.getTileEntity(new BlockPos(x,y,z));if(id==1 && t instanceof TileRoulette)return new GuiRoulette(new RouletteContainer(p,(TileRoulette)t));return id==0 && t instanceof TileBlackjack?new GuiCasino(new CasinoContainer(p,(TileBlackjack)t)):null;}
    @Override public void receive(Snapshot message){Minecraft.getMinecraft().addScheduledTask(()->{
        if(Minecraft.getMinecraft().player!=null && Minecraft.getMinecraft().player.openContainer instanceof ShopContainer){ShopContainer c=(ShopContainer)Minecraft.getMinecraft().player.openContainer;if(c.windowId==message.window && message.data!=null)c.view=message.data;}
        if(Minecraft.getMinecraft().player!=null && Minecraft.getMinecraft().player.openContainer instanceof RouletteContainer){RouletteContainer c=(RouletteContainer)Minecraft.getMinecraft().player.openContainer;if(c.windowId==message.window && message.data!=null)c.view=message.data;}
        if(Minecraft.getMinecraft().player!=null && Minecraft.getMinecraft().player.openContainer instanceof CasinoContainer){CasinoContainer c=(CasinoContainer)Minecraft.getMinecraft().player.openContainer;if(c.windowId==message.window && message.data!=null)c.view=message.data;}
    });}
    private static class ShopkeeperRenderer extends RenderLiving<EntityShopkeeper> {
        private final ResourceLocation skin=new ResourceLocation(CasinoMod.ID,"textures/entity/shopkeeper.png");
        ShopkeeperRenderer(RenderManager manager){super(manager,new ModelVillager(0.0F),0.5F);}
        @Override protected ResourceLocation getEntityTexture(EntityShopkeeper entity){return skin;}
    }
    private static class DealerRenderer extends RenderLiving<EntityDealer> {
        private final ResourceLocation skin=new ResourceLocation(CasinoMod.ID,"textures/entity/dealer.png");
        DealerRenderer(RenderManager manager){super(manager,new ModelVillager(0.0F),0.5F);}
        @Override protected ResourceLocation getEntityTexture(EntityDealer entity){return skin;}
    }
}
