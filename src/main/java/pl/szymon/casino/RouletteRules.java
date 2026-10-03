package pl.szymon.casino;

public final class RouletteRules {
    private RouletteRules(){}
    public static final int[] WHEEL={0,32,15,19,4,21,2,25,17,34,6,27,13,36,11,30,8,23,10,5,24,16,33,1,20,14,31,9,22,18,29,7,28,12,35,3,26};
    public static final int SPIN_TICKS=240;
    public static final double TAU=Math.PI*2, SECTOR=TAU/37.0;
    public static boolean red(int n){return n==1||n==3||n==5||n==7||n==9||n==12||n==14||n==16||n==18||n==19||n==21||n==23||n==25||n==27||n==30||n==32||n==34||n==36;}
    public static int payout(int type,int number,int stake){
        if(type<0||type>48||number<0||number>36||stake<=0)return 0;
        if(type<=36)return type==number?stake*36:0;
        if(number==0)return 0;
        boolean win=false;int multiplier=2;
        switch(type){
            case 37:win=red(number);break;case 38:win=!red(number);break;
            case 39:win=number%2==0;break;case 40:win=number%2!=0;break;
            case 41:win=number<=18;break;case 42:win=number>=19;break;
            case 43:case 44:case 45:win=(number-1)/12==type-43;multiplier=3;break;
            case 46:case 47:case 48:win=(number-1)%3==type-46;multiplier=3;break;
        }
        return win?stake*multiplier:0;
    }
    public static String label(int type){
        if(type>=0&&type<=36)return Integer.toString(type);
        String[] labels={"Czerwone","Czarne","Parzyste","Nieparzyste","1-18","19-36","1-12","13-24","25-36","Kolumna 1","Kolumna 2","Kolumna 3"};
        return type>=37&&type<=48?labels[type-37]:"?";
    }
    public static double clamp(double p){return Math.max(0,Math.min(1,p));}
    public static double ease(double p){p=clamp(p);return 1-Math.pow(1-p,3);}
    public static double wheelAngle(double start,double progress){return start+TAU*5*ease(progress);}
    public static double ballAngle(double start,int pocket,double progress){
        double p=clamp(progress),landing=start+TAU*5+pocket*SECTOR;
        return landing+TAU*8*Math.pow(1-p,2.2);
    }
    public static double ballRadius(double progress){return 0.80-0.21*ease((progress-0.64)/0.30);}
}
