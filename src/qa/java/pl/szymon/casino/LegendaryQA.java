package pl.szymon.casino;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
/** Run only with tools/legendary-qa.gradle. Never included in the release JAR. */
@Mod.EventBusSubscriber(modid=CasinoMod.ID,value=Side.CLIENT)
public class LegendaryQA {
    private static int stage,ticks; private static volatile boolean done,failed;
    private static void ok(boolean b,String message){if(!b)throw new AssertionError(message);System.out.println("LEGENDARY_QA_OK "+message);}
    @SubscribeEvent public static void client(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END)return;
        Minecraft mc=Minecraft.getMinecraft();
        if(++ticks>2400)throw new AssertionError("QA timeout");
        if(stage==0&&mc.currentScreen instanceof GuiMainMenu){stage=1;mc.gameSettings.pauseOnLostFocus=false;mc.gameSettings.renderDistanceChunks=2;mc.launchIntegratedServer("LegendaryQA","Legendary QA",new WorldSettings(7,GameType.CREATIVE,true,false,WorldType.FLAT));}
        if(stage==1&&mc.player!=null&&mc.world!=null){stage=2;mc.getIntegratedServer().addScheduledTask(()->{
            try{
                EntityPlayerMP p=mc.getIntegratedServer().getPlayerList().getPlayers().get(0);WorldServer w=p.getServerWorld();p.setGameType(GameType.SURVIVAL);
                NonNullList<ItemStack> items=NonNullList.create();CasinoMod.TAB.displayAllRelevantItems(items);
                ok(items.size()==SpecialItems.ALL.length+CasinoMod.CHIPS.length+2,"creative tab contains every casino item");
                ok(ShopCatalog.IDS.length==SpecialItems.PRODUCTS.length,"catalog alignment");
                for(int i=0;i<ShopCatalog.IDS.length;i++)ok(SpecialItems.PRODUCTS[i].getRegistryName().getResourcePath().equals(ShopCatalog.IDS[i]),"offer identity "+i);
                p.clearActivePotions();p.getFoodStats().setFoodLevel(1);ItemStack food=new ItemStack(SpecialItems.KFC,2);SpecialItems.KFC.onItemUseFinish(food,w,p);
                ok(food.getCount()==1&&p.getFoodStats().getFoodLevel()==20,"KFC consumption and full hunger");ok(p.isPotionActive(MobEffects.ABSORPTION)&&p.isPotionActive(MobEffects.REGENERATION),"KFC effects");
                for(Item drink:new Item[]{SpecialItems.WHITE_MONSTER,SpecialItems.DZIK}){p.clearActivePotions();drink.onItemUseFinish(new ItemStack(drink),w,p);ok(p.isPotionActive(MobEffects.HASTE)&&p.isPotionActive(MobEffects.SPEED)&&p.isPotionActive(MobEffects.NIGHT_VISION),"energy effects");}
                p.clearActivePotions();SpecialItems.AMNESIA.onItemUseFinish(new ItemStack(SpecialItems.AMNESIA),w,p);ok(p.isPotionActive(MobEffects.LEVITATION)&&p.isPotionActive(MobEffects.SLOWNESS)&&p.isPotionActive(MobEffects.BLINDNESS),"Amnesia effects");
                p.clearActivePotions();SpecialItems.RED_BULL.onItemUseFinish(new ItemStack(SpecialItems.RED_BULL),w,p);ok(p.isPotionActive(MobEffects.HASTE)&&p.isPotionActive(MobEffects.RESISTANCE),"tobacco effects");
                p.clearActivePotions();ItemWatrouble.worn(p);ok(p.getAbsorptionAmount()==4F,"amulet absorption");p.setAbsorptionAmount(0);ItemWatrouble.worn(p);ok(p.getAbsorptionAmount()==0,"amulet cannot refill every tick");
                p.clearActivePotions();
                ok(!(SpecialItems.BROWN_LEGGINGS instanceof net.minecraft.item.ItemArmor)&&!(SpecialItems.FISHS_FEET instanceof net.minecraft.item.ItemArmor),"accessories are not armor");
                p.setItemStackToSlot(EntityEquipmentSlot.LEGS,new ItemStack(SpecialItems.BROWN_LEGGINGS));
                ok(!CasinoBaubles.isWorn(p,SpecialItems.BROWN_LEGGINGS),"old armor slot does not activate bauble");
                p.setItemStackToSlot(EntityEquipmentSlot.LEGS,ItemStack.EMPTY);
                if(net.minecraftforge.fml.common.Loader.isModLoaded("baubles")){
                    Object handler=Class.forName("baubles.api.BaublesApi").getMethod("getBaublesHandler",net.minecraft.entity.player.EntityPlayer.class).invoke(null,p);
                    net.minecraftforge.items.IItemHandlerModifiable inventory=(net.minecraftforge.items.IItemHandlerModifiable)handler;
                    inventory.setStackInSlot(3,new ItemStack(SpecialItems.BROWN_LEGGINGS));
                    inventory.setStackInSlot(6,new ItemStack(SpecialItems.FISHS_FEET));
                    inventory.setStackInSlot(0,new ItemStack(SpecialItems.WATROUBLE));
                    ok(CasinoBaubles.isWorn(p,SpecialItems.FISHS_FEET)&&CasinoBaubles.isWorn(p,SpecialItems.WATROUBLE),"three accessories equip together");
                }else p.setHeldItem(net.minecraft.util.EnumHand.OFF_HAND,new ItemStack(SpecialItems.BROWN_LEGGINGS));
                p.ticksExisted=200;LegendaryEvents.tick(new TickEvent.PlayerTickEvent(TickEvent.Phase.END,p));ok(p.isPotionActive(MobEffects.INVISIBILITY),"bauble leggings invisibility");
                System.out.println("LEGENDARY_QA_COMPLETE");
            }catch(Throwable ex){failed=true;ex.printStackTrace();}finally{done=true;}
        });}
        if(done){if(failed)throw new AssertionError("Legendary QA failed");mc.shutdown();}
    }
}
