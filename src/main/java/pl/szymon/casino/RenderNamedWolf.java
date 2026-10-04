package pl.szymon.casino;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

/** Keeps vanilla wolf animations, wet shading and the tame collar layer. */
public class RenderNamedWolf extends RenderWolf {
    private static final ResourceLocation AZOR = new ResourceLocation(CasinoMod.ID, "textures/entity/azor.png");
    private static final ResourceLocation MORTY = new ResourceLocation(CasinoMod.ID, "textures/entity/morty.png");
    private static final ResourceLocation KAUKAZ = new ResourceLocation(CasinoMod.ID, "textures/entity/caucasian_shepherd.png");
    private static final ResourceLocation MALTIPOO = new ResourceLocation(CasinoMod.ID, "textures/entity/maltipoo.png");
    private final ModelWolf caucasian = new DogModel(3);
    private final ModelWolf maltipoo = new DogModel(4);
    private final ModelWolf vanilla = new ModelWolf();
    private final ModelWolf golden = new DogModel(4);
    private final ModelWolf terrier = new DogModel(2);
    public RenderNamedWolf(RenderManager manager) { super(manager); }
    @Override public void doRender(EntityWolf wolf, double x, double y, double z, float yaw, float partialTicks) {
        String name = TextFormatting.getTextWithoutFormattingCodes(wolf.getCustomNameTag());
        mainModel = "Azor".equals(name) ? golden : "Morty".equals(name) ? terrier : "Kaukaz".equals(name) ? caucasian : "Maltipoo".equals(name) ? maltipoo : vanilla;
        super.doRender(wolf, x, y, z, yaw, partialTicks);
    }
    private static final class DogModel extends ModelWolf {
        DogModel(int earLength) {
            wolfHeadMain.cubeList.clear();
            wolfHeadMain.setTextureOffset(0, 0).addBox(-2, -3, -2, 6, 6, 4, 0);
            wolfHeadMain.setTextureOffset(0, 10).addBox(-0.5F, 0, -5, 3, 3, 4, 0);
            // Hanging ears, with their own UV island outside the vanilla collar.
            wolfHeadMain.setTextureOffset(48, 16).addBox(-3, -3, -1, 1, earLength, 3, 0);
            wolfHeadMain.setTextureOffset(48, 16).addBox(4, -3, -1, 1, earLength, 3, 0);
        }
    }
    @Override protected ResourceLocation getEntityTexture(EntityWolf wolf) {
        String name = TextFormatting.getTextWithoutFormattingCodes(wolf.getCustomNameTag());
        if ("Azor".equals(name)) return AZOR;
        if ("Morty".equals(name)) return MORTY;
        if ("Kaukaz".equals(name)) return KAUKAZ;
        if ("Maltipoo".equals(name)) return MALTIPOO;
        return super.getEntityTexture(wolf);
    }
}
