package pl.szymon.casino;

import java.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;

public class TileRoulette extends TileEntity implements ITickable {
    public static final int BETTING=0,SPINNING=1,RESULT=2;
    public BlockPos origin;
    public boolean formed,breaking,dropTable=true;
    public int phase=BETTING,timer,elapsed,pocket=-1,lastNumber=-1,pot;
    public double startAngle;
    private long receivedTick;
    private int tick;
    private final LinkedHashMap<UUID,Bettor> bettors=new LinkedHashMap<>();
    public static class Bettor {
        String name="";
        final List<int[]> bets=new ArrayList<>();
        int paid;
        int total(){int n=0;for(int[] b:bets)n+=b[1];return n;}
    }
    public boolean master(){return origin!=null && origin.equals(pos);}
    public TileRoulette controller(){
        if(origin==null || !world.isBlockLoaded(origin))return null;
        TileEntity te=world.getTileEntity(origin);return te instanceof TileRoulette && ((TileRoulette)te).master()?(TileRoulette)te:null;
    }
    public void configure(BlockPos p){origin=p.toImmutable();markDirty();}
    public boolean usable(EntityPlayer p){return master() && formed && !isInvalid() && world.getTileEntity(pos)==this && p.isEntityAlive() && p.getDistanceSq(pos.getX()+1.5,pos.getY()+0.5,pos.getZ()+1.0)<100;}
    public boolean locked(){return phase!=BETTING || !bettors.isEmpty();}
    public double progress(float partial){
        if(phase==RESULT)return 1;
        if(phase!=SPINNING)return 0;
        double e=elapsed+(world.isRemote?Math.max(0,world.getTotalWorldTime()-receivedTick):0)+partial;
        return RouletteRules.clamp(e/RouletteRules.SPIN_TICKS);
    }
    private void message(EntityPlayer p,String text){p.sendMessage(new TextComponentString(text));}
    public void action(EntityPlayerMP player,int action,int amount){
        if(!usable(player)||phase!=BETTING)return;
        UUID id=player.getUniqueID();Bettor b=bettors.get(id);
        if(action>=0 && action<=48){
            if(amount!=5&&amount!=25&&amount!=50&&amount!=100&&amount!=200&&amount!=500)return;
            if(b==null && bettors.size()>=4){message(player,"W tej rundzie obstawiaja juz 4 osoby. Mozesz ogladac losowanie.");return;}
            if(b!=null && b.total()+amount>5000){message(player,"Limit jednej osoby: 5000 zetonow na runde.");return;}
            if(!Chips.take(player,amount)){message(player,"Brak zetonow. Wymien emeraldy lub zmniejsz nominal.");return;}
            if(b==null){b=new Bettor();b.name=player.getName();bettors.put(id,b);}
            b.bets.add(new int[]{action,amount});if(timer==0)timer=400;
        }else if(action==70 && b!=null && !b.bets.isEmpty()){
            int[] bet=b.bets.remove(b.bets.size()-1);refund(id,bet[1]);if(b.bets.isEmpty())bettors.remove(id);
        }else if(action==71 && b!=null){int sum=b.total();bettors.remove(id);refund(id,sum);}
        else if(action==72){if(!Chips.buy(player))message(player,"Potrzebujesz 1 emeralda.");}
        else if(action==73){if(!Chips.sell(player))message(player,"Potrzebujesz 100 zetonow.");}
        if(bettors.isEmpty())timer=0;recount();sync();
    }
    private void recount(){pot=0;for(Bettor b:bettors.values())pot+=b.total();}
    private void refund(UUID id,int amount){
        if(amount<=0)return;PayoutLedger l=PayoutLedger.get(world);l.credit(id,amount);
        EntityPlayerMP p=world.getMinecraftServer().getPlayerList().getPlayerByUUID(id);if(p!=null)l.claim(p);
    }
    private void beginSpin(){
        if(phase!=BETTING||bettors.isEmpty())return;
        phase=SPINNING;elapsed=0;timer=0;pocket=world.rand.nextInt(37);startAngle=world.rand.nextDouble()*RouletteRules.TAU;sync();
        world.playSound(null,pos,SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON,SoundCategory.BLOCKS,0.5F,0.8F);
    }
    private void settle(){
        if(phase!=SPINNING)return;
        phase=RESULT;timer=160;elapsed=RouletteRules.SPIN_TICKS;lastNumber=RouletteRules.WHEEL[pocket];
        for(Bettor b:bettors.values()){b.paid=0;for(int[] bet:b.bets)b.paid+=RouletteRules.payout(bet[0],lastNumber,bet[1]);}
        markDirty();
        for(Map.Entry<UUID,Bettor> e:bettors.entrySet()){
            Bettor b=e.getValue();refund(e.getKey(),b.paid);
            EntityPlayerMP player=world.getMinecraftServer().getPlayerList().getPlayerByUUID(e.getKey());
            if(player!=null)message(player,"Ruletka: "+lastNumber+". Zwrot: "+b.paid+", wynik netto: "+(b.paid-b.total())+" zetonow.");
        }
        world.playSound(null,pos,SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,SoundCategory.BLOCKS,0.8F,0.8F);sync();
    }
    @Override public void update(){
        if(world.isRemote)return;
        tick++;
        if(!master()){if(origin!=null && tick%40==0 && world.isBlockLoaded(origin) && controller()==null)world.setBlockToAir(pos);return;}
        if(!formed || breaking)return;
        // Pause while a footprint chunk is unloaded; never force-load chunks.
        for(int x=0;x<3;x++)for(int z=0;z<2;z++)if(!world.isBlockLoaded(pos.add(x,0,z)))return;
        if(tick%20==0){for(int x=0;x<3;x++)for(int z=0;z<2;z++){
            TileEntity t=world.getTileEntity(pos.add(x,0,z));
            if(!(t instanceof TileRoulette)||!pos.equals(((TileRoulette)t).origin)){dismantle(null);return;}
        }}
        if(phase==BETTING && timer>0){if(--timer==0)beginSpin();}
        else if(phase==SPINNING){
            elapsed++;
            if(elapsed<RouletteRules.SPIN_TICKS && elapsed%(elapsed<140?5:12)==0)world.playSound(null,pos,SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON,SoundCategory.BLOCKS,0.18F,1.6F);
            if(elapsed>=RouletteRules.SPIN_TICKS)settle();
        }else if(phase==RESULT && timer>0 && --timer==0){phase=BETTING;bettors.clear();pot=0;sync();}
        if(tick%10==0)sync();
    }
    public void dismantle(BlockPos alreadyBreaking){
        if(world==null||world.isRemote||breaking||!master()||!formed)return;
        breaking=true;formed=false;
        if(phase!=RESULT)for(Map.Entry<UUID,Bettor> e:bettors.entrySet())refund(e.getKey(),e.getValue().total());
        bettors.clear();markDirty();
        // Children first; keep the controller available to guard recursive break callbacks.
        for(int x=0;x<3;x++)for(int z=0;z<2;z++){
            BlockPos q=pos.add(x,0,z);if(q.equals(pos)||q.equals(alreadyBreaking)||!world.isBlockLoaded(q))continue;
            TileEntity t=world.getTileEntity(q);if(t instanceof TileRoulette && pos.equals(((TileRoulette)t).origin))world.setBlockToAir(q);
        }
        if(dropTable)net.minecraft.block.Block.spawnAsEntity(world,pos,new ItemStack(CasinoMod.ROULETTE_ITEM));
        if(!pos.equals(alreadyBreaking))world.setBlockToAir(pos);
    }
    public void sync(){markDirty();if(world!=null&&!world.isRemote){IBlockState state=world.getBlockState(pos);world.notifyBlockUpdate(pos,state,state,2);}}
    private NBTTagCompound visual(NBTTagCompound tag){
        if(origin!=null)tag.setLong("origin",origin.toLong());tag.setBoolean("formed",formed);tag.setInteger("phase",phase);tag.setInteger("timer",timer);tag.setInteger("elapsed",elapsed);tag.setInteger("pocket",pocket);tag.setInteger("last",lastNumber);tag.setInteger("pot",pot);tag.setDouble("angle",startAngle);return tag;
    }
    private void readVisual(NBTTagCompound t){
        origin=t.hasKey("origin")?BlockPos.fromLong(t.getLong("origin")):null;formed=t.getBoolean("formed");phase=t.getInteger("phase");timer=t.getInteger("timer");elapsed=t.getInteger("elapsed");pocket=t.hasKey("pocket")?t.getInteger("pocket"):-1;lastNumber=t.hasKey("last")?t.getInteger("last"):-1;pot=t.getInteger("pot");startAngle=t.getDouble("angle");receivedTick=world==null?0:world.getTotalWorldTime();
    }
    @Override public NBTTagCompound getUpdateTag(){return visual(super.writeToNBT(new NBTTagCompound()));}
    @Override public void handleUpdateTag(NBTTagCompound t){super.readFromNBT(t);readVisual(t);}
    @Override public SPacketUpdateTileEntity getUpdatePacket(){return new SPacketUpdateTileEntity(pos,1,getUpdateTag());}
    @Override public void onDataPacket(NetworkManager net,SPacketUpdateTileEntity packet){readVisual(packet.getNbtCompound());}
    @Override public NBTTagCompound writeToNBT(NBTTagCompound t){
        visual(super.writeToNBT(t));NBTTagList list=new NBTTagList();
        for(Map.Entry<UUID,Bettor> e:bettors.entrySet()){
            NBTTagCompound b=new NBTTagCompound();b.setUniqueId("uuid",e.getKey());b.setString("name",e.getValue().name);b.setInteger("paid",e.getValue().paid);
            int[] bets=new int[e.getValue().bets.size()*2];int i=0;for(int[] bet:e.getValue().bets){bets[i++]=bet[0];bets[i++]=bet[1];}b.setIntArray("bets",bets);list.appendTag(b);
        }t.setTag("bettors",list);return t;
    }
    @Override public void readFromNBT(NBTTagCompound t){
        super.readFromNBT(t);readVisual(t);bettors.clear();NBTTagList list=t.getTagList("bettors",10);
        for(int i=0;i<list.tagCount();i++){NBTTagCompound v=list.getCompoundTagAt(i);if(!v.hasUniqueId("uuid"))continue;Bettor b=new Bettor();b.name=v.getString("name");b.paid=v.getInteger("paid");int[] bets=v.getIntArray("bets");for(int j=0;j+1<bets.length;j+=2)b.bets.add(new int[]{bets[j],bets[j+1]});bettors.put(v.getUniqueId("uuid"),b);}
    }
    public NBTTagCompound snapshot(EntityPlayer player,int selected){
        NBTTagCompound t=new NBTTagCompound();t.setInteger("phase",phase);t.setInteger("timer",phase==SPINNING?RouletteRules.SPIN_TICKS-elapsed:timer);t.setInteger("last",lastNumber);t.setInteger("balance",Chips.balance(player));t.setInteger("selected",selected);t.setInteger("pot",pot);
        int[] mine=new int[49],all=new int[49];Bettor own=bettors.get(player.getUniqueID());NBTTagList people=new NBTTagList();
        for(Bettor b:bettors.values()){
            for(int[] bet:b.bets)all[bet[0]]+=bet[1];NBTTagCompound who=new NBTTagCompound();who.setString("name",b.name);who.setInteger("bet",b.total());who.setInteger("paid",b.paid);people.appendTag(who);
        }
        if(own!=null){for(int[] b:own.bets)mine[b[0]]+=b[1];t.setInteger("mine",own.total());t.setInteger("paid",own.paid);}
        t.setIntArray("bets",mine);t.setIntArray("all",all);t.setTag("people",people);return t;
    }
    @Override public AxisAlignedBB getRenderBoundingBox(){return master()?new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+3,pos.getY()+2,pos.getZ()+2):new AxisAlignedBB(pos);}
    @Override public double getMaxRenderDistanceSquared(){return 4096;}
}
