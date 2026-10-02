package pl.szymon.casino;

import java.util.*;

/** Pure server-side rules. Card id = suit * 13 + rank, where rank 0 is an ace. */
public final class BlackjackRound {
    public static final int EMPTY=0, PLAYING=1, STOOD=2, BUST=3, NATURAL=4;
    public final List<List<Integer>> hands = new ArrayList<>();
    public final List<Integer> dealer = new ArrayList<>();
    public final int[] status = new int[4];
    public int[] deck = new int[52];
    public int cursor, current = -1;
    public BlackjackRound() { for(int i=0;i<4;i++) hands.add(new ArrayList<Integer>()); }
    public void deal(boolean[] active, Random random) {
        List<Integer> cards=new ArrayList<>(); for(int i=0;i<52;i++) cards.add(i);
        Collections.shuffle(cards, random); for(int i=0;i<52;i++) deck[i]=cards.get(i);
        dealFromDeck(active);
    }
    public void dealFromDeck(boolean[] active) {
        cursor=0; dealer.clear();
        for(int i=0;i<4;i++) { hands.get(i).clear(); status[i]=active[i]?PLAYING:EMPTY; }
        for(int pass=0;pass<2;pass++) {
            for(int i=0;i<4;i++) if(active[i]) draw(hands.get(i));
            draw(dealer);
        }
        for(int i=0;i<4;i++) if(active[i] && natural(hands.get(i))) status[i]=NATURAL;
        if(natural(dealer)) for(int i=0;i<4;i++) if(status[i]==PLAYING) status[i]=STOOD;
        current=-1; advance();
    }
    private void draw(List<Integer> hand) {
        if(cursor>=deck.length) throw new IllegalStateException("Deck exhausted");
        hand.add(deck[cursor++]);
    }
    private void advance() { do { current++; } while(current<4 && status[current]!=PLAYING); if(current>=4) current=-1; }
    public boolean hit(int seat) {
        if(seat<0 || seat!=current || status[seat]!=PLAYING) return false;
        draw(hands.get(seat)); int value=total(hands.get(seat));
        if(value>=21) { status[seat]=value>21?BUST:STOOD; advance(); }
        return true;
    }
    public boolean stand(int seat) {
        if(seat<0 || seat>=4 || status[seat]!=PLAYING) return false;
        status[seat]=STOOD; if(current==seat) advance(); return true;
    }
    public boolean doubleDown(int seat) {
        if(seat<0 || seat!=current || status[seat]!=PLAYING || hands.get(seat).size()!=2) return false;
        draw(hands.get(seat)); status[seat]=total(hands.get(seat))>21?BUST:STOOD; advance(); return true;
    }
    /** Returns true when the dealer is finished; stands on ALL 17s, including soft 17. */
    public boolean dealerStep() {
        if(current!=-1) return false;
        boolean needsDealer=false;
        for(int s:status) if(s==STOOD) needsDealer=true;
        if(!needsDealer || natural(dealer) || total(dealer)>=17) return true;
        draw(dealer); return total(dealer)>=17;
    }
    public static int total(List<Integer> hand) {
        int sum=0,aces=0;
        for(int c:hand) { int r=c%13; if(r==0){sum+=11;aces++;}else sum+=Math.min(r+1,10); }
        while(sum>21 && aces-->0) sum-=10;
        return sum;
    }
    public static boolean natural(List<Integer> hand) { return hand.size()==2 && total(hand)==21; }
    /** Gross amount returned, including the original stake. */
    public int payout(int seat,int bet) {
        if(status[seat]==EMPTY || total(hands.get(seat))>21) return 0;
        boolean playerBJ=natural(hands.get(seat)), dealerBJ=natural(dealer);
        if(dealerBJ) return playerBJ?bet:0;
        if(playerBJ) return bet+bet*3/2;
        int p=total(hands.get(seat)),d=total(dealer);
        if(d>21 || p>d) return bet*2;
        return p==d?bet:0;
    }
}
