package pl.szymon.casino;

import java.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

public class TileBlackjack extends TileEntity implements ITickable {
    public static final int BETTING=0, PLAYERS=1, DEALER=2, RESULTS=3;
    public final UUID[] players=new UUID[4];
    public final String[] names={"","","",""};
    public final int[] bets=new int[4], paid=new int[4];
    public final boolean[] ready=new boolean[4];
    public BlackjackRound round=new BlackjackRound();
    public int phase=BETTING, timer=0;
    private int ticks=0;
    private UUID dealerId;
    private final long[] lastAction={-100,-100,-100,-100};
    public int seat(UUID id){for(int i=0;i<4;i++)if(id.equals(players[i]))return i;return -1;}
    public boolean locked(){for(int b:bets)if(b>0)return true;return phase!=BETTING;}
    public boolean usable(EntityPlayer p){return !isInvalid() && world.getTileEntity(pos)==this && p.isEntityAlive() && p.getDistanceSq(pos)<64;}
    public int join(EntityPlayer p) {
        int old=seat(p.getUniqueID());if(old>=0)return old;
        if(phase!=BETTING){p.sendMessage(new TextComponentString("Runda trwa. Poczekaj na kolejne rozdanie."));return -1;}
        for(int i=0;i<4;i++)if(players[i]==null){players[i]=p.getUniqueID();names[i]=p.getName();ready[i]=false;lastAction[i]=-100;markDirty();return i;}
        p.sendMessage(new TextComponentString("Stol jest pelny: maksymalnie 4 graczy."));return -1;
    }
    private EntityPlayerMP online(int i){return players[i]==null?null:world.getMinecraftServer().getPlayerList().getPlayerByUUID(players[i]);}
    private boolean present(int i) {
        EntityPlayerMP p=online(i);
        return p!=null && p.world==world && usable(p) && p.openContainer instanceof CasinoContainer && ((CasinoContainer)p.openContainer).table==this;
    }
    private void clearSeat(int i){players[i]=null;names[i]="";bets[i]=0;paid[i]=0;ready[i]=false;}
    private void credit(int i,int amount){if(players[i]!=null && amount>0){PayoutLedger ledger=PayoutLedger.get(world);ledger.credit(players[i],amount);EntityPlayerMP p=online(i);if(p!=null)ledger.claim(p);}}
    public void leave(EntityPlayer p) {
        int i=seat(p.getUniqueID());if(i<0)return;
        if(phase==BETTING){int refund=bets[i];bets[i]=0;credit(i,refund);clearSeat(i);}
        else if(phase==PLAYERS){int previous=round.current;round.stand(i);if(round.current!=previous)timer=600;if(round.current<0){phase=DEALER;timer=20;}}
        markDirty();
    }
    public void action(EntityPlayerMP p,int action) {
        int i=seat(p.getUniqueID());if(i<0 || !usable(p))return;
        long now=world.getTotalWorldTime();if(now-lastAction[i]<3)return;lastAction[i]=now;
        if(phase==BETTING) {
            int change=action==5?50:action==6?-50:action==7?500:action==8?-500:0;
            if(change!=0) {
                if(ready[i])return;
                int target=Math.max(0,Math.min(5000,bets[i]+change));int diff=target-bets[i];
                if(diff>0 && !Chips.take(p,diff)){p.sendMessage(new TextComponentString("Brak zetonow na te stawke."));return;}
                bets[i]=target;if(diff<0)credit(i,-diff);
                if(timer==0 && bets[i]>0)timer=400;
            } else if(action==1 && bets[i]>0)ready[i]=!ready[i];
            else if(action==9) { if(!Chips.buy(p))p.sendMessage(new TextComponentString("Potrzebujesz 1 emeralda.")); }
            else if(action==10){if(!Chips.sell(p))p.sendMessage(new TextComponentString("Potrzebujesz 100 zetonow."));}
            boolean any=false,all=true;for(int j=0;j<4;j++)if(players[j]!=null){any|=bets[j]>0;all &= bets[j]>0 && ready[j];}
            if(!any)timer=0;else if(all)startRound();
        } else if(phase==PLAYERS && round.current==i) {
            int before=round.current;
            if(action==2)round.hit(i);
            else if(action==3)round.stand(i);
            else if(action==4 && round.hands.get(i).size()==2 && bets[i]<=5000) {
                if(!Chips.take(p,bets[i])){p.sendMessage(new TextComponentString("Brak zetonow na podwojenie."));return;}
                bets[i]*=2;round.doubleDown(i);
            }
            if(before!=round.current)timer=600;
            if(round.current<0){phase=DEALER;timer=20;}
        }
        markDirty();
    }
    private void startRound() {
        if(world.isRemote || phase!=BETTING)return;
        boolean[] active=new boolean[4];boolean any=false;for(int i=0;i<4;i++){active[i]=bets[i]>0;any|=active[i];paid[i]=0;}
        if(!any)return;
        round=new BlackjackRound();round.deal(active,world.rand);phase=round.current<0?DEALER:PLAYERS;timer=phase==DEALER?20:600;markDirty();
        world.playSound(null,pos,CasinoSounds.NO_MORE_BETS,SoundCategory.MASTER,2.0F,1.0F);
    }
    private void settle() {
        if(phase!=DEALER)return;
        phase=RESULTS;timer=160;
        for(int i=0;i<4;i++)paid[i]=round.payout(i,bets[i]);
        markDirty();
        for(int i=0;i<4;i++)credit(i,paid[i]);
    }
    @Override public void update() {
        if(world.isRemote)return;
        ticks++;
        if(ticks%100==1)ensureDealer();
        if(ticks%20==0){
            for(int i=0;i<4;i++)if(players[i]!=null && !present(i)) {
                if(phase==BETTING){int refund=bets[i];bets[i]=0;credit(i,refund);clearSeat(i);}
                else if(phase==PLAYERS){int previous=round.current;round.stand(i);if(previous!=round.current)timer=600;}
            }
            if(phase==PLAYERS && round.current<0){phase=DEALER;timer=20;}
            markDirty();
        }
        if(timer>0)timer--;
        if(phase==BETTING && timer==0){boolean any=false;for(int b:bets)any|=b>0;if(any)startRound();}
        else if(phase==PLAYERS && timer==0){round.stand(round.current);if(round.current<0){phase=DEALER;timer=20;}else timer=600;markDirty();}
        else if(phase==DEALER && timer==0){if(round.dealerStep())settle();else timer=20;markDirty();}
        else if(phase==RESULTS && timer==0){
            for(int i=0;i<4;i++){bets[i]=0;paid[i]=0;ready[i]=false;if(!present(i))clearSeat(i);}
            phase=BETTING;round=new BlackjackRound();markDirty();
        }
    }
    public void cancel() {
        if(world==null || world.isRemote)return;
        for(int i=0;i<4;i++){int refund=phase==RESULTS?0:bets[i];bets[i]=0;credit(i,refund);clearSeat(i);}
        if(dealerId!=null){net.minecraft.entity.Entity e=((WorldServer)world).getEntityFromUuid(dealerId);if(e instanceof EntityDealer)e.setDead();}
        phase=BETTING;timer=0;markDirty();
    }
    public boolean ownsDealer(UUID id){return dealerId==null || dealerId.equals(id);}
    private void ensureDealer() {
        if(!(world instanceof WorldServer))return;
        EnumFacing front=world.getBlockState(pos).getValue(BlockBlackjack.FACING);
        if(!world.isBlockLoaded(pos.offset(front.getOpposite(),2)))return;
        if(dealerId!=null && ((WorldServer)world).getEntityFromUuid(dealerId) instanceof EntityDealer)return;
        for(EntityDealer e:world.getEntitiesWithinAABB(EntityDealer.class,new AxisAlignedBB(pos).grow(3)))if(e.belongsTo(pos)){dealerId=e.getUniqueID();return;}
        EntityDealer dealer=new EntityDealer(world);dealer.bind(pos);dealer.reposition();
        if(world.spawnEntity(dealer)){dealerId=dealer.getUniqueID();markDirty();}
    }
    public NBTTagCompound snapshot(EntityPlayer p) {
        NBTTagCompound tag=new NBTTagCompound();tag.setInteger("phase",phase);tag.setInteger("timer",timer);tag.setInteger("turn",round.current);tag.setInteger("seat",seat(p.getUniqueID()));tag.setInteger("balance",Chips.balance(p));
        NBTTagList list=new NBTTagList();
        for(int i=0;i<4;i++){NBTTagCompound s=new NBTTagCompound();s.setString("name",names[i]);s.setInteger("bet",bets[i]);s.setInteger("paid",paid[i]);s.setBoolean("ready",ready[i]);s.setInteger("status",round.status[i]);s.setIntArray("cards",ints(round.hands.get(i)));list.appendTag(s);}tag.setTag("seats",list);
        int[] cards=ints(round.dealer);if(phase==PLAYERS && cards.length>1)cards[1]=-1;
        tag.setIntArray("dealer",cards);return tag;
    }
    private static int[] ints(List<Integer> cards){int[] a=new int[cards.size()];for(int i=0;i<a.length;i++)a[i]=cards.get(i);return a;}
    @Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);tag.setInteger("phase",phase);tag.setInteger("timer",timer);if(dealerId!=null)tag.setUniqueId("dealerId",dealerId);
        NBTTagList list=new NBTTagList();for(int i=0;i<4;i++){NBTTagCompound s=new NBTTagCompound();if(players[i]!=null)s.setUniqueId("player",players[i]);s.setString("name",names[i]);s.setInteger("bet",bets[i]);s.setInteger("paid",paid[i]);s.setBoolean("ready",ready[i]);s.setInteger("status",round.status[i]);s.setIntArray("cards",ints(round.hands.get(i)));list.appendTag(s);}tag.setTag("seats",list);
        tag.setIntArray("deck",round.deck);tag.setIntArray("dealer",ints(round.dealer));tag.setInteger("cursor",round.cursor);tag.setInteger("turn",round.current);return tag;
    }
    @Override public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);phase=tag.getInteger("phase");timer=tag.getInteger("timer");dealerId=tag.hasUniqueId("dealerId")?tag.getUniqueId("dealerId"):null;round=new BlackjackRound();
        NBTTagList list=tag.getTagList("seats",10);for(int i=0;i<4;i++){NBTTagCompound s=list.getCompoundTagAt(i);players[i]=s.hasUniqueId("player")?s.getUniqueId("player"):null;names[i]=s.getString("name");bets[i]=s.getInteger("bet");paid[i]=s.getInteger("paid");ready[i]=s.getBoolean("ready");round.status[i]=s.getInteger("status");for(int c:s.getIntArray("cards"))round.hands.get(i).add(c);}
        int[] deck=tag.getIntArray("deck");if(deck.length==52)round.deck=deck;
        for(int c:tag.getIntArray("dealer"))round.dealer.add(c);round.cursor=tag.getInteger("cursor");round.current=tag.hasKey("turn")?tag.getInteger("turn"):-1;
    }
    /** Never expose the saved deck or dealer hole card via chunk update NBT. */
    @Override public NBTTagCompound getUpdateTag(){return super.writeToNBT(new NBTTagCompound());}
    @Override public void handleUpdateTag(NBTTagCompound tag){super.readFromNBT(tag);}
}
