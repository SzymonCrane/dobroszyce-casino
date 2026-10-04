package pl.szymon.casino;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

/** Name-only appearance override; vanilla handles tame types, kittens and animations. */
public class RenderNamedOcelot extends RenderOcelot {
    private static final ResourceLocation YUKI = new ResourceLocation(CasinoMod.ID, "textures/entity/yuki.png");

    public RenderNamedOcelot(RenderManager manager) { super(manager); }

    @Override protected ResourceLocation getEntityTexture(EntityOcelot ocelot) {
        String name = TextFormatting.getTextWithoutFormattingCodes(ocelot.getCustomNameTag());
        return "Yuki".equals(name) ? YUKI : super.getEntityTexture(ocelot);
    }
}
