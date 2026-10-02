package pl.szymon.casino;

import java.io.IOException;
import java.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.*;

public class GuiCasino extends GuiContainer {
    private final CasinoContainer casino;
    public GuiCasino(CasinoContainer container){super(container);casino=container;xSize=304;ySize=234;}
    @Override public void initGui(){
        super.initGui();buttonList.clear();
        button(5,10,162,42,"+50");button(6,55,162,42,"-50");button(7,100,162,45,"+500");button(8,148,162,45,"-500");button(1,197,162,97,"Gotowy");
        button(2,10,184,90,"Dobierz");button(3,106,184,90,"Pas");button(4,202,184,92,"Podwoj");
        button(9,10,208,138,"Kup 100 / 1 emerald");button(10,154,208,140,"Sprzedaj 100 / 1 emerald");
    }
    private void button(int id,int x,int y,int w,String label){buttonList.add(new GuiButton(id,guiLeft+x,guiTop+y,w,18,label));}
    @Override protected void actionPerformed(GuiButton b)throws IOException{if(b.enabled)mc.playerController.sendEnchantPacket(casino.windowId,b.id);}
    @Override public void updateScreen(){
        super.updateScreen();NBTTagCompound v=casino.view;int phase=v.getInteger("phase"),seat=v.hasKey("seat")?v.getInteger("seat"):-1;
        NBTTagCompound own=seat>=0?v.getTagList("seats",10).getCompoundTagAt(seat):new NBTTagCompound();
        boolean betting=seat>=0 && phase==TileBlackjack.BETTING;
        boolean turn=phase==TileBlackjack.PLAYERS && seat>=0 && v.getInteger("turn")==seat;
        for(GuiButton b:buttonList){
            if(b.id==1){b.enabled=betting && own.getInteger("bet")>0;b.displayString=own.getBoolean("ready")?"Czekaj":"Gotowy";}
            else if(b.id>=5 && b.id<=8)b.enabled=betting && !own.getBoolean("ready");
            else if(b.id>=9)b.enabled=betting;
            else if(b.id==4)b.enabled=turn && own.getIntArray("cards").length==2 && v.getInteger("balance")>=own.getInteger("bet");
            else b.enabled=turn;
        }
    }
    @Override public void drawScreen(int mouseX,int mouseY,float partialTicks){drawDefaultBackground();super.drawScreen(mouseX,mouseY,partialTicks);}
    @Override protected void drawGuiContainerBackgroundLayer(float partialTicks,int mouseX,int mouseY){
        drawRect(guiLeft,guiTop,guiLeft+xSize,guiTop+ySize,0xFF251C19);
        drawRect(guiLeft+3,guiTop+3,guiLeft+xSize-3,guiTop+ySize-3,0xFFB29457);
        drawRect(guiLeft+5,guiTop+5,guiLeft+xSize-5,guiTop+ySize-5,0xFF123F35);
    }
    private void text(String s,int x,int y,int color){fontRenderer.drawString(s,x,y,color);}
    @Override protected void drawGuiContainerForegroundLayer(int mouseX,int mouseY){
        NBTTagCompound v=casino.view;if(!v.hasKey("seats")){text("Laczenie ze stolem...",12,12,0xFFFFFF);return;}
        int phase=v.getInteger("phase"),seat=v.getInteger("seat");
        text("DOBROSZYCE  /  BLACKJACK",12,10,0xE9D39A);
        text("Zetony: "+v.getInteger("balance"),12,24,0xFFFFFF);
        text("3:2  |  S17  |  50-5000",158,24,0xB9D9CB);
        String[] phases={"Obstawianie","Tura gracza","Krupier dobiera","Wyniki"};
        text(phases[Math.max(0,Math.min(3,phase))]+"  "+((v.getInteger("timer")+19)/20)+"s",12,40,0xFFFFFF);
        int[] dealer=v.getIntArray("dealer");cards(dealer,160,40,120);
        text("Krupier: "+score(dealer),12,54,0xE9D39A);
        NBTTagList seats=v.getTagList("seats",10);
        for(int i=0;i<4;i++){
            int x=9+i*73;NBTTagCompound s=seats.getCompoundTagAt(i);boolean active=phase==TileBlackjack.PLAYERS && v.getInteger("turn")==i;
            drawRect(x,78,x+69,156,active?0xFFD4B969:0xFF527666);drawRect(x+1,79,x+68,155,0xFF173329);
            String name=s.getString("name");if(name.isEmpty())name="Wolne";
            text(fontRenderer.trimStringToWidth((i==seat?"> ":"")+name,64),x+3,83,i==seat?0xFFE29D:0xFFFFFF);
            text("Stawka "+s.getInteger("bet"),x+3,95,0xCBDDD4);
            int[] hand=s.getIntArray("cards");cards(hand,x+3,108,62);
            text("Suma "+score(hand),x+3,132,0xFFFFFF);
            String state;
            if(phase==TileBlackjack.RESULTS && s.getInteger("bet")>0){int net=s.getInteger("paid")-s.getInteger("bet");state=(net>=0?"+":"")+net;}
            else if(phase==TileBlackjack.BETTING)state=s.getBoolean("ready")?"GOTOWY":"";
            else {String[] labels={"","GRA","PAS","FURA","BLACKJACK"};state=labels[Math.max(0,Math.min(4,s.getInteger("status")))];}
            text(state,x+3,143,0xE9D39A);
        }
    }
    private String score(int[] cards){List<Integer> list=new ArrayList<>();for(int c:cards){if(c<0)return "?";list.add(c);}return cards.length==0?"-":Integer.toString(BlackjackRound.total(list));}
    private void cards(int[] cards,int x,int y,int width){
        int step=cards.length<2?18:Math.min(19,(width-17)/(cards.length-1));
        for(int i=0;i<cards.length;i++){
            int left=x+i*step,c=cards[i];drawRect(left,y,left+17,y+22,0xFFBCAB8B);drawRect(left+1,y+1,left+16,y+21,c<0?0xFF962F3D:0xFFFFF6DC);
            if(c<0){text("?",left+5,y+7,0xFFFFFF);continue;}
            int suit=c/13,rank=c%13;String[] ranks={"A","2","3","4","5","6","7","8","9","10","J","Q","K"};String[] suits={"C","D","H","S"};
            int color=(suit==1||suit==2)?0xB52E38:0x202D29;text(ranks[rank],left+2,y+2,color);text(suits[suit],left+8,y+12,color);
        }
    }
}
