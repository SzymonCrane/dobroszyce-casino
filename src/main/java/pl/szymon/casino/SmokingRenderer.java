package pl.szymon.casino;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid=CasinoMod.ID,value=Side.CLIENT)
public final class SmokingRenderer {
    private SmokingRenderer(){}
    @SubscribeEvent public static void hand(RenderSpecificHandEvent event){
        Minecraft mc=Minecraft.getMinecraft();
        EntityPlayerSP player=mc.player;
        Item item=event.getItemStack().getItem();
        if(player==null || !player.isHandActive() || player.getActiveHand()!=event.getHand()
            || !(item instanceof ItemCigarette || item instanceof ItemSmokeGun))return;
        boolean right=(player.getPrimaryHand()==EnumHandSide.RIGHT)==(event.getHand()==EnumHand.MAIN_HAND);
        float direction=right?1.0F:-1.0F;
        float elapsed=event.getItemStack().getMaxItemUseDuration()-player.getItemInUseCount()+event.getPartialTicks();
        float lift=Math.min(1.0F,Math.max(0.0F,elapsed/8.0F));
        lift=lift*lift*(3.0F-2.0F*lift);
        // Smoothly raise and hold near the mouth; no vanilla eating bob or crumbs.
        event.setCanceled(true);
        GlStateManager.pushMatrix();
        try {
            GlStateManager.translate(direction*(0.56F-0.32F*lift),-0.52F+0.30F*lift,-0.72F+0.14F*lift);
            GlStateManager.rotate(direction*(-12.0F*lift),0,0,1);
            mc.getItemRenderer().renderItemSide(player,event.getItemStack(),right?ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND:ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,!right);
        } finally {GlStateManager.popMatrix();}
    }
}
