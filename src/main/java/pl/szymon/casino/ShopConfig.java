package pl.szymon.casino;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.apache.logging.log4j.Logger;

/** Server-authoritative prices. Invalid reloads retain the last good catalog. */
public final class ShopConfig {
    private static File file;
    private static Logger log;
    private static Map<String,Integer> prices=Collections.emptyMap();
    private ShopConfig(){}
    public static void initialize(File path,Logger logger){
        file=path;log=logger;
        try {
            if(!file.exists()){
                Files.createDirectories(file.toPath().getParent());
                JsonObject root=new JsonObject(),offers=new JsonObject();root.addProperty("schemaVersion",1);
                for(String id:ShopCatalog.IDS){JsonObject offer=new JsonObject();offer.addProperty("enabled",false);offer.addProperty("price",0);offers.add(id,offer);}
                root.add("offers",offers);
                try(Writer w=new OutputStreamWriter(new FileOutputStream(file),StandardCharsets.UTF_8)){new GsonBuilder().setPrettyPrinting().create().toJson(root,w);}
            }
            reload();
        }catch(Exception e){log.error("Casino shop disabled: cannot read "+file,e);}
    }
    public static void reload()throws IOException{
        try(Reader reader=new InputStreamReader(new FileInputStream(file),StandardCharsets.UTF_8)){
            Map<String,Integer> candidate=parse(new JsonParser().parse(reader));prices=candidate;
        }catch(RuntimeException e){throw new IOException("Invalid shop JSON: "+e.getMessage(),e);}
    }
    static Map<String,Integer> parse(JsonElement json){
        JsonObject root=json.getAsJsonObject();
        if(integer(root.get("schemaVersion"))!=1)throw new IllegalArgumentException("schemaVersion must equal 1");
        JsonObject offers=root.getAsJsonObject("offers");if(offers==null)throw new IllegalArgumentException("Missing offers");
        Map<String,Integer> next=new HashMap<>();Set<String> known=new HashSet<>(Arrays.asList(ShopCatalog.IDS));
        for(Map.Entry<String,JsonElement> entry:offers.entrySet()){
            if(!known.contains(entry.getKey()))throw new IllegalArgumentException("Unknown item: "+entry.getKey());
            JsonObject offer=entry.getValue().getAsJsonObject();JsonElement enabled=offer.get("enabled");
            if(enabled==null || !enabled.isJsonPrimitive() || !enabled.getAsJsonPrimitive().isBoolean())throw new IllegalArgumentException("enabled must be boolean");
            int price=integer(offer.get("price"));
            if(price<0 || price>1000000 || price%5!=0 || (enabled.getAsBoolean() && price==0))throw new IllegalArgumentException("Enabled prices: 5..1000000, multiples of 5");
            if(enabled.getAsBoolean())next.put(entry.getKey(),price);
        }
        return Collections.unmodifiableMap(next);
    }
    private static int integer(JsonElement v){
        if(v==null || !v.isJsonPrimitive() || !v.getAsJsonPrimitive().isNumber())throw new IllegalArgumentException("Expected integer");
        return v.getAsBigDecimal().intValueExact();
    }
    public static int price(int index){return index>=0&&index<ShopCatalog.IDS.length?prices.getOrDefault(ShopCatalog.IDS[index],0):0;}
}
