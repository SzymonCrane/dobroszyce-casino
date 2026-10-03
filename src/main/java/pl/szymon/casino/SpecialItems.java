package pl.szymon.casino;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
public final class SpecialItems {
    private SpecialItems(){}
    private static Item named(Item item,String id){return item.setRegistryName(CasinoMod.ID,id).setUnlocalizedName(CasinoMod.ID+"."+id).setCreativeTab(CreativeTabs.MISC);}
    public static final Item FISHS_FEET=named(new ItemFishFeet(),"fishs_feet");
    public static final Item BANANA=named(new ItemCasinoConsumable(MobEffects.STRENGTH,1,true),"banana_special");
    public static final Item CLUB=named(new ItemFourHandClub(),"daniels_4_hand_club");
    public static final Item SMOKE=named(new ItemSmokeGun(),"sisters_device");
    public static final Item MALBORO=named(new ItemCasinoConsumable(MobEffects.SPEED,0,false),"malboro_red");
    public static final Item SHOPKEEPER=named(new ItemShopkeeper(),"shopkeeper_placer");
    public static final Item[] PRODUCTS={FISHS_FEET,BANANA,CLUB,SMOKE,MALBORO};
    public static final Item[] ALL={FISHS_FEET,BANANA,CLUB,SMOKE,MALBORO,SHOPKEEPER};
}
