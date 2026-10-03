package pl.szymon.casino;

/** Currency rules independent of Minecraft, shared by payments and tests. */
public final class ChipMath {
    public static final int[] VALUES={5,25,50,100,200,500};
    private ChipMath(){}
    public static boolean valid(int amount){return amount>0 && amount%5==0;}
    public static int[] change(int amount){
        if(amount<0 || amount%5!=0)throw new IllegalArgumentException("Amount must be a nonnegative multiple of 5");
        int[] counts=new int[VALUES.length];
        for(int i=VALUES.length-1;i>=0;i--){counts[i]=amount/VALUES[i];amount%=VALUES[i];}
        return counts;
    }
}
