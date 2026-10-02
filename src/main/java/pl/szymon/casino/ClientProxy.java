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
    @Override public void preInit(){RenderingRegistry.registerEntityRenderingHandler(EntityDealer.class,manager->new DealerRenderer(manager));}
    @SubscribeEvent public static void models(ModelRegistryEvent e){
        ModelLoader.setCustomModelResourceLocation(CasinoMod.TABLE_ITEM,0,new ModelResourceLocation(CasinoMod.TABLE.getRegistryName(),"inventory"));
        for(net.minecraft.item.Item item:CasinoMod.CHIPS)ModelLoader.setCustomModelResourceLocation(item,0,new ModelResourceLocation(item.getRegistryName(),"inventory"));
    }
    @Override public Object getClientGuiElement(int id,EntityPlayer p,World w,int x,int y,int z){TileEntity t=w.getTileEntity(new BlockPos(x,y,z));return t instanceof TileBlackjack?new GuiCasino(new CasinoContainer(p,(TileBlackjack)t)):null;}
    @Override public void receive(Snapshot message){Minecraft.getMinecraft().addScheduledTask(()->{
        if(Minecraft.getMinecraft().player!=null && Minecraft.getMinecraft().player.openContainer instanceof CasinoContainer){CasinoContainer c=(CasinoContainer)Minecraft.getMinecraft().player.openContainer;if(c.windowId==message.window && message.data!=null)c.view=message.data;}
    });}
    private static class DealerRenderer extends RenderLiving<EntityDealer> {
        private final ResourceLocation skin=new ResourceLocation(CasinoMod.ID,"textures/entity/dealer.png");
        DealerRenderer(RenderManager manager){super(manager,new ModelVillager(0.0F),0.5F);}
        @Override protected ResourceLocation getEntityTexture(EntityDealer entity){return skin;}
    }
}
