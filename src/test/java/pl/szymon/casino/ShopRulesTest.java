package pl.szymon.casino;
import com.google.gson.*;
import java.util.*;
public class ShopRulesTest {
    private static int checks;
    private static void ok(boolean condition){checks++;if(!condition)throw new AssertionError("Check "+checks);}
    private static JsonObject catalog(){JsonObject root=new JsonObject(),offers=new JsonObject();root.addProperty("schemaVersion",1);root.add("offers",offers);for(String id:ShopCatalog.IDS){JsonObject offer=new JsonObject();offer.addProperty("enabled",true);offer.addProperty("price",205);offers.add(id,offer);}return root;}
    private static void rejects(JsonObject root){try{ShopConfig.parse(root);throw new AssertionError("Accepted invalid catalog");}catch(IllegalArgumentException|IllegalStateException|ArithmeticException expected){checks++;}}
    public static void main(String[] args){
        for(int amount=0;amount<=1000000;amount+=5){int[] c=ChipMath.change(amount);int sum=0;for(int i=0;i<c.length;i++){ok(c[i]>=0);sum+=c[i]*ChipMath.VALUES[i];}ok(sum==amount);}
        for(int amount:new int[]{-1,-5,1,24,26,201}){try{ChipMath.change(amount);throw new AssertionError("Invalid change accepted");}catch(IllegalArgumentException expected){checks++;}}
        // Every denomination can pay all smaller prices without losing change.
        for(int coin:ChipMath.VALUES)for(int cost=5;cost<=coin;cost+=5){int sum=0;int[] change=ChipMath.change(coin-cost);for(int i=0;i<change.length;i++)sum+=change[i]*ChipMath.VALUES[i];ok(sum+cost==coin);}
        ok(ShopCatalog.IDS.length==ShopCatalog.NAMES.length&&ShopCatalog.IDS.length==ShopCatalog.HELP.length);
        ok(new HashSet<>(Arrays.asList(ShopCatalog.IDS)).size()==ShopCatalog.IDS.length);
        JsonObject legacy=new JsonObject(),legacyOffers=new JsonObject(),legacyOffer=new JsonObject();legacy.addProperty("schemaVersion",1);legacyOffer.addProperty("enabled",true);legacyOffer.addProperty("price",25);legacyOffers.add("fishs_feet",legacyOffer);legacy.add("offers",legacyOffers);
        ok(ShopConfig.parse(legacy).size()==1); // Upgrading does not require resetting old prices.
        JsonObject root=catalog();ok(ShopConfig.parse(root).size()==ShopCatalog.IDS.length);ok(ShopConfig.parse(root).get("fishs_feet")==205);
        JsonObject offer=root.getAsJsonObject("offers").getAsJsonObject("fishs_feet");
        for(int bad:new int[]{-5,0,1,24,1000005}){offer.addProperty("price",bad);rejects(root);}
        offer.addProperty("price",5.5);rejects(root);offer.addProperty("price",5000000000L);rejects(root);offer.addProperty("price","25");rejects(root);
        offer.addProperty("price",5);offer.addProperty("enabled","true");rejects(root);offer.addProperty("enabled",true);ok(ShopConfig.parse(root).get("fishs_feet")==5);
        offer.addProperty("enabled",false);offer.addProperty("price",0);ok(!ShopConfig.parse(root).containsKey("fishs_feet"));
        root.getAsJsonObject("offers").add("typo",offer);rejects(root);
        root=catalog();root.addProperty("schemaVersion",2);rejects(root);
        root=catalog();root.remove("offers");rejects(root);
        System.out.println("Currency and shop: "+checks+" checks passed.");
    }
}
