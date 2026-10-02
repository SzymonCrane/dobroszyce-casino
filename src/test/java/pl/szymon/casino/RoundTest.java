package pl.szymon.casino;
import java.util.*;

public class RoundTest {
    private static int checks;
    private static void expect(boolean ok,String message){checks++;if(!ok)throw new AssertionError(message);}
    private static BlackjackRound prepared(int... cards){BlackjackRound r=new BlackjackRound();for(int i=0;i<52;i++)r.deck[i]=i;for(int i=0;i<cards.length;i++)r.deck[i]=cards[i];return r;}
    public static void main(String[] args){
        expect(BlackjackRound.total(Arrays.asList(0,13,8))==21,"two aces + 9");
        expect(BlackjackRound.total(Arrays.asList(0,13,26,8))==12,"three aces + 9");
        expect(BlackjackRound.total(Arrays.asList(12,11,2))==23,"bust total");
        BlackjackRound r=prepared(0,8,12,6);r.dealFromDeck(new boolean[]{true,false,false,false});
        expect(r.current==-1,"natural skips turn");expect(r.payout(0,50)==125,"3:2 exact payout including stake");
        r=prepared(0,13,12,25);r.dealFromDeck(new boolean[]{true,false,false,false});expect(r.payout(0,100)==100,"both natural push");
        r=prepared(8,0,7,12);r.dealFromDeck(new boolean[]{true,false,false,false});expect(r.current==-1 && r.payout(0,100)==0,"dealer natural wins immediately");
        r=prepared(9,0,6,5);r.dealFromDeck(new boolean[]{true,false,false,false});r.stand(0);int before=r.cursor;expect(r.dealerStep() && r.cursor==before,"stand soft 17");expect(r.payout(0,100)==100,"17 push");
        r=prepared(9,8,6,7,9);r.dealFromDeck(new boolean[]{true,false,false,false});expect(!r.hit(1),"out of turn rejected");expect(r.hit(0) && r.status[0]==BlackjackRound.BUST,"player bust");expect(r.payout(0,100)==0,"bust loses");
        r=prepared(4,9,5,6,9);r.dealFromDeck(new boolean[]{true,false,false,false});expect(r.doubleDown(0),"double accepted at two cards");expect(r.hands.get(0).size()==3 && r.current==-1,"double draws one and stands");expect(r.payout(0,200)==400,"double return");expect(!r.doubleDown(0),"double cannot repeat");
        r=prepared(1,5,2,6,3);r.dealFromDeck(new boolean[]{true,false,false,false});r.hit(0);expect(!r.doubleDown(0),"no double after hit");
        int rounds=10000;
        for(int seed=0;seed<rounds;seed++){
            r=new BlackjackRound();r.deal(new boolean[]{true,true,true,true},new Random(seed));
            while(r.current>=0){int seat=r.current;if(BlackjackRound.total(r.hands.get(seat))<17)r.hit(seat);else r.stand(seat);}
            while(!r.dealerStep()){}
            Set<Integer> seen=new HashSet<>();for(List<Integer> h:r.hands)for(int c:h)expect(seen.add(c),"duplicate player card");for(int c:r.dealer)expect(seen.add(c),"duplicate dealer card");
            expect(r.cursor<=52,"deck bound");for(int i=0;i<4;i++){int payout=r.payout(i,50);expect(payout==0||payout==50||payout==100||payout==125,"valid payout");}
        }
        System.out.println("Blackjack rules: "+checks+" checks passed across "+rounds+" four-player rounds.");
    }
}
