package pl.szymon.casino;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
public class GuiShop extends GuiContainer {
    private final ShopContainer shop;
    private int page;
    private static final int PAGE_SIZE=5;
    public GuiShop(ShopContainer c){super(c);shop=c;xSize=310;ySize=222;}
    @Override public void initGui(){super.initGui();buttonList.clear();for(int i=0;i<PAGE_SIZE;i++)buttonList.add(new GuiButton(i,guiLeft+212,guiTop+39+i*33,87,20,"..."));buttonList.add(new GuiButton(100,guiLeft+190,guiTop+202,25,18,"<"));buttonList.add(new GuiButton(101,guiLeft+270,guiTop+202,25,18,">"));}
    @Override public void updateScreen(){super.updateScreen();int[] prices=shop.view.getIntArray("prices");for(GuiButton b:buttonList){if(b.id>=100){b.enabled=b.id==100?page>0:(page+1)*PAGE_SIZE<ShopCatalog.IDS.length;continue;}int index=page*PAGE_SIZE+b.id; b.visible=index<ShopCatalog.IDS.length;int price=index<prices.length?prices[index]:0;b.displayString=price>0?"Kup: "+price:"Niedostepne";b.enabled=price>0 && shop.view.getInteger("balance")>=price;}}
    @Override protected void actionPerformed(GuiButton b)throws IOException{if(!b.enabled)return;if(b.id==100)page--;else if(b.id==101)page++;else mc.playerController.sendEnchantPacket(shop.windowId,page*PAGE_SIZE+b.id);updateScreen();}
    @Override public void drawScreen(int x,int y,float partial){drawDefaultBackground();super.drawScreen(x,y,partial);}
    @Override protected void drawGuiContainerBackgroundLayer(float partial,int x,int y){drawRect(guiLeft,guiTop,guiLeft+xSize,guiTop+ySize,0xFFCCA758);drawRect(guiLeft+3,guiTop+3,guiLeft+xSize-3,guiTop+ySize-3,0xFF133D32);}
    @Override protected void drawGuiContainerForegroundLayer(int mx,int my){
        fontRenderer.drawString("SKLEPIKARZ KASYNA",10,10,0xFFE2A8);fontRenderer.drawString("Saldo: "+shop.view.getInteger("balance")+" chips",10,23,0xFFFFFF);
        for(int row=0;row<PAGE_SIZE;row++){int i=page*PAGE_SIZE+row;if(i>=ShopCatalog.IDS.length)break;int y=39+row*33;fontRenderer.drawString(ShopCatalog.NAMES[i],10,y,0xFFFFFF);
            net.minecraft.client.renderer.GlStateManager.pushMatrix();net.minecraft.client.renderer.GlStateManager.scale(0.65F,0.65F,1);fontRenderer.drawString(ShopCatalog.HELP[i],15,(int)((y+13)/0.65F),0xB9D1C3);net.minecraft.client.renderer.GlStateManager.popMatrix();}
        fontRenderer.drawString("Ceny: config serwera",10,207,0xB9D1C3);fontRenderer.drawString((page+1)+" / "+((ShopCatalog.IDS.length+4)/5),220,207,0xFFFFFF);
    }
}
