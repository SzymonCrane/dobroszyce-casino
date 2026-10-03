package pl.szymon.casino;

import java.io.IOException;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.*;

public class GuiRoulette extends GuiContainer {
    private final RouletteContainer roulette;
    public GuiRoulette(RouletteContainer c){super(c);roulette=c;xSize=312;ySize=234;}
    @Override public void initGui(){
        super.initGui();buttonList.clear();
        buttonList.add(new BetButton(0,guiLeft+8,guiTop+49,22,59,"0",0xFF226A45));
        for(int row=0;row<3;row++)for(int col=0;col<12;col++){
            int n=col*3+3-row;buttonList.add(new BetButton(n,guiLeft+32+col*22,guiTop+49+row*20,21,19,Integer.toString(n),RouletteRules.red(n)?0xFF9F3341:0xFF252832));
        }
        for(int i=0;i<6;i++)buttonList.add(new BetButton(37+i,guiLeft+8+i*50,guiTop+111,49,18,RouletteRules.label(37+i),i==0?0xFF9F3341:i==1?0xFF252832:0xFF235446));
        for(int i=0;i<6;i++)buttonList.add(new BetButton(43+i,guiLeft+8+i*50,guiTop+131,49,18,RouletteRules.label(43+i),0xFF235446));
        for(int i=0;i<CasinoMod.VALUES.length;i++)buttonList.add(new GuiButton(60+i,guiLeft+8+i*31,guiTop+154,30,18,Integer.toString(CasinoMod.VALUES[i])));
        buttonList.add(new GuiButton(70,guiLeft+200,guiTop+154,49,18,"Cofnij"));buttonList.add(new GuiButton(71,guiLeft+251,guiTop+154,53,18,"Anuluj"));
        buttonList.add(new GuiButton(72,guiLeft+8,guiTop+176,145,18,"Kup 100 / 1 emerald"));buttonList.add(new GuiButton(73,guiLeft+157,guiTop+176,147,18,"Sprzedaj 100 / 1 emerald"));
    }
    @Override protected void actionPerformed(GuiButton button)throws IOException{if(button.enabled)mc.playerController.sendEnchantPacket(roulette.windowId,button.id);}
    @Override public void updateScreen(){
        super.updateScreen();NBTTagCompound v=roulette.view;boolean open=v.hasKey("phase") && v.getInteger("phase")==TileRoulette.BETTING;
        int selected=v.getInteger("selected");for(GuiButton b:buttonList){b.enabled=open;if(b.id>=60&&b.id<=65)b.displayString=(CasinoMod.VALUES[b.id-60]==selected?">":"")+CasinoMod.VALUES[b.id-60];}
    }
    @Override public void drawScreen(int x,int y,float pt){
        drawDefaultBackground();super.drawScreen(x,y,pt);
        for(GuiButton b:buttonList)if(b.id<=48&&b.isMouseOver()){
            int[] mine=roulette.view.getIntArray("bets"),all=roulette.view.getIntArray("all");
            if(mine.length==49&&all.length==49)drawHoveringText(Arrays.asList(RouletteRules.label(b.id),"Twoje: "+mine[b.id]+"  Razem: "+all[b.id],"Klik: +"+roulette.view.getInteger("selected")+" zetonow"),x,y);
        }
    }
    @Override protected void drawGuiContainerBackgroundLayer(float pt,int x,int y){drawRect(guiLeft,guiTop,guiLeft+xSize,guiTop+ySize,0xFF241C1B);drawRect(guiLeft+3,guiTop+3,guiLeft+xSize-3,guiTop+ySize-3,0xFFC1A264);drawRect(guiLeft+5,guiTop+5,guiLeft+xSize-5,guiTop+ySize-5,0xFF103E32);}
    @Override protected void drawGuiContainerForegroundLayer(int x,int y){
        NBTTagCompound v=roulette.view;if(!v.hasKey("phase")){fontRenderer.drawString("Laczenie...",10,12,0xFFFFFF);return;}
        int phase=v.getInteger("phase");String[] labels={"OBSTAWIANIE","KULKA W RUCHU","WYNIK"};int last=v.getInteger("last");
        fontRenderer.drawString("RULETKA EUROPEJSKA   /   "+labels[phase],9,10,0xF4D99E);
        fontRenderer.drawString("Saldo "+v.getInteger("balance")+"  |  Twoje "+v.getInteger("mine")+"  |  Pula "+v.getInteger("pot"),9,23,0xFFFFFF);
        fontRenderer.drawString("Czas: "+((v.getInteger("timer")+19)/20)+"s   Ostatni: "+(last<0?"-":last+(last==0?" zielone":RouletteRules.red(last)?" czerwone":" czarne")),9,36,0xC5DCCB);
        int[] mine=v.getIntArray("bets");if(mine.length==49)for(GuiButton b:buttonList)if(b.id<=48&&mine[b.id]>0){int bx=b.x-guiLeft,by=b.y-guiTop;drawRect(bx+1,by+1,bx+5,by+5,0xFFFFCF59);}
        if(phase==TileRoulette.RESULT)fontRenderer.drawString("Twoj zwrot: "+v.getInteger("paid")+"  Netto: "+(v.getInteger("paid")-v.getInteger("mine")),9,200,0xFFE29B);
        else fontRenderer.drawString("Zamknij ESC, aby ogladac kolo w swiecie.",9,200,0xFFFFFF);
        fontRenderer.drawString("Zaklady zostaja po zamknieciu. Limit: 5000/os.",9,213,0xBBCFC1);
    }
    private static class BetButton extends GuiButton {
        private final int color;
        BetButton(int id,int x,int y,int w,int h,String name,int color){super(id,x,y,w,h,name);this.color=color;}
        @Override public void drawButton(Minecraft mc,int mx,int my,float pt){
            if(!visible)return;hovered=mx>=x&&my>=y&&mx<x+width&&my<y+height;
            drawRect(x,y,x+width,y+height,hovered&&enabled?0xFFFFD98F:0xFF9F956F);drawRect(x+1,y+1,x+width-1,y+height-1,color);
            String name=id>=46&&id<=48?"Kol. "+(id-45):id==37?"Czerw.":id==39?"Parz.":id==40?"Nieparz.":displayString;
            String text=mc.fontRenderer.trimStringToWidth(name,width-3);drawCenteredString(mc.fontRenderer,text,x+width/2,y+(height-8)/2,enabled?0xFFFFFF:0xFF9FABA2);
        }
    }
}
