package pl.szymon.casino;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
public final class SpecialItems {
    private SpecialItems(){}
    private static Item named(Item item,String id){return item.setRegistryName(CasinoMod.ID,id).setUnlocalizedName(CasinoMod.ID+"."+id).setCreativeTab(CasinoMod.TAB);}
    public static final Item FISHS_FEET=named(new ItemFishFeet(),"fishs_feet");
    public static final Item BANANA=named(new ItemCasinoConsumable(MobEffects.STRENGTH,1,true),"banana_special");
    public static final Item CLUB=named(new ItemFourHandClub(),"daniels_4_hand_club");
    public static final Item SMOKE=named(new ItemSmokeGun(),"sisters_device");
    public static final Item MALBORO=named(new ItemCigarette(),"malboro_red");
    public static final Item SHOPKEEPER=named(new ItemShopkeeper(),"shopkeeper_placer");
    public static final Item KFC=named(new ItemKfcBucket(),"legendary_kfc_bucket");
    public static final Item PAPER_BOWL=named(new Item(),"paper_bowl");
    public static final Item KFC_WING=named(new ItemFood(6,0.6F,true),"kfc_wing");
    private static Item energy(String id){return named(new ItemLegendaryFood(true,false,new PotionEffect(MobEffects.HASTE,1200,0),new PotionEffect(MobEffects.SPEED,1200,0),new PotionEffect(MobEffects.NIGHT_VISION,1200,0)),id);}
    public static final Item WHITE_MONSTER=energy("white_monster");
    public static final Item DZIK=energy("dzik_energy");
    public static final Item KARP_GEORGE=named(new Item(),"karp_george");
    public static final Item BROWN_LEGGINGS=named(new ItemBrownLeggings(),"ceasars_brown_leggings");
    public static final Item AMNESIA=named(new ItemLegendaryFood(false,false,new PotionEffect(MobEffects.LEVITATION,200,0),new PotionEffect(MobEffects.SLOWNESS,200,0),new PotionEffect(MobEffects.BLINDNESS,200,0)),"amnesias_weed");
    public static final Item RED_BULL=named(new ItemLegendaryFood(false,false,new PotionEffect(MobEffects.HASTE,1200,0),new PotionEffect(MobEffects.RESISTANCE,1200,0)),"red_bull_tabacco");
    public static final Item WATROUBLE=named(new ItemWatrouble(),"watrouble");
    public static final Item[] PRODUCTS={FISHS_FEET,BANANA,CLUB,SMOKE,MALBORO,KFC,WHITE_MONSTER,DZIK,BROWN_LEGGINGS,AMNESIA,RED_BULL,WATROUBLE};
    public static final Item[] ALL={FISHS_FEET,BANANA,CLUB,SMOKE,MALBORO,SHOPKEEPER,KFC,PAPER_BOWL,KFC_WING,WHITE_MONSTER,DZIK,KARP_GEORGE,BROWN_LEGGINGS,AMNESIA,RED_BULL,WATROUBLE};
}
