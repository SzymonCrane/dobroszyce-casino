package pl.szymon.casino;

import java.util.*;
public class RouletteTest {
    private static int checks;
    private static void expect(boolean condition,String message){checks++;if(!condition)throw new AssertionError(message);}
    private static double normalized(double a){return (a%RouletteRules.TAU+RouletteRules.TAU)%RouletteRules.TAU;}
    public static void main(String[] args){
        Set<Integer> numbers=new HashSet<>();int reds=0;
        for(int n:RouletteRules.WHEEL){expect(n>=0&&n<=36&&numbers.add(n),"Wheel has all 37 unique numbers");if(RouletteRules.red(n))reds++;}
        expect(reds==18,"18 red, 18 black, one zero");
        for(int amount=5;amount<=5000;amount+=5)for(int type=0;type<=48;type++){
            int total=0;for(int number=0;number<=36;number++){
                int pay=RouletteRules.payout(type,number,amount);expect(pay>=0 && pay%5==0,"payout preserves chip units");total+=pay;
                if(type>=37 && number==0)expect(pay==0,"zero loses outside bets");
            }expect(total==36*amount,"every standard bet has the correct aggregate return");
        }
        expect(RouletteRules.payout(0,0,25)==900,"straight zero gross 36x");
        expect(RouletteRules.payout(37,1,25)==50,"red gross 2x");
        expect(RouletteRules.payout(43,12,25)==75,"dozen boundary");
        expect(RouletteRules.payout(43,13,25)==0,"next dozen excluded");
        expect(RouletteRules.payout(46,1,25)==75 && RouletteRules.payout(46,34,25)==75,"first column");
        Random random=new Random(37);
        for(int run=0;run<500;run++)for(int pocket=0;pocket<37;pocket++){
            double start=random.nextDouble()*RouletteRules.TAU;
            double wheel=RouletteRules.wheelAngle(start,1),ball=RouletteRules.ballAngle(start,pocket,1);
            double delta=normalized(ball-wheel);int visible=(int)Math.round(delta/RouletteRules.SECTOR)%37;
            expect(visible==pocket,"physical landing agrees with server pocket");
            expect(Math.abs(RouletteRules.ballRadius(1)-0.59)<1e-9,"ball in pocket ring");
            double previous=Double.POSITIVE_INFINITY;
            for(int tick=0;tick<=240;tick+=20){double angle=RouletteRules.ballAngle(start,pocket,tick/240.0);expect(angle<=previous+1e-9,"ball motion never reverses");previous=angle;}
        }
        expect(RouletteRules.payout(-1,10,100)==0 && RouletteRules.payout(49,10,100)==0,"invalid wager IDs rejected");
        System.out.println("Roulette: "+checks+" checks passed (all bets, all outcomes, all stakes, 18500 physical landings).");
    }
}
