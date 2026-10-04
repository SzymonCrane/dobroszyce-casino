package pl.szymon.casino;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAlternatywka extends RenderBiped<EntityAlternatywka> {
    private static final ResourceLocation SKIN = new ResourceLocation(CasinoMod.ID, "textures/entity/alternatywka.png");
    public RenderAlternatywka(RenderManager manager) { super(manager, new ModelBiped(0, 0, 64, 64), 0.5F); }
    @Override protected ResourceLocation getEntityTexture(EntityAlternatywka mob) { return SKIN; }
}
