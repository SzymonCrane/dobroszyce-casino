package pl.szymon.casino;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
public class GuiShop extends GuiContainer {
    private final ShopContainer shop;
    public GuiShop(ShopContainer c){super(c);shop=c;xSize=310;ySize=222;}
    @Override public void initGui(){super.initGui();buttonList.clear();for(int i=0;i<5;i++)buttonList.add(new GuiButton(i,guiLeft+212,guiTop+39+i*33,87,20,"..."));}
    @Override public void updateScreen(){super.updateScreen();int[] prices=shop.view.getIntArray("prices");for(GuiButton b:buttonList){int price=prices.length==5?prices[b.id]:0;b.displayString=price>0?"Kup: "+price:"Niedostepne";b.enabled=price>0 && shop.view.getInteger("balance")>=price;}}
    @Override protected void actionPerformed(GuiButton b)throws IOException{if(b.enabled)mc.playerController.sendEnchantPacket(shop.windowId,b.id);}
    @Override public void drawScreen(int x,int y,float partial){drawDefaultBackground();super.drawScreen(x,y,partial);}
    @Override protected void drawGuiContainerBackgroundLayer(float partial,int x,int y){drawRect(guiLeft,guiTop,guiLeft+xSize,guiTop+ySize,0xFFCCA758);drawRect(guiLeft+3,guiTop+3,guiLeft+xSize-3,guiTop+ySize-3,0xFF133D32);}
    @Override protected void drawGuiContainerForegroundLayer(int mx,int my){
        fontRenderer.drawString("SKLEPIKARZ KASYNA",10,10,0xFFE2A8);fontRenderer.drawString("Saldo: "+shop.view.getInteger("balance")+" chips",10,23,0xFFFFFF);
        for(int i=0;i<5;i++){int y=39+i*33;fontRenderer.drawString(ShopCatalog.NAMES[i],10,y,0xFFFFFF);
            net.minecraft.client.renderer.GlStateManager.pushMatrix();net.minecraft.client.renderer.GlStateManager.scale(0.65F,0.65F,1);fontRenderer.drawString(ShopCatalog.HELP[i],15,(int)((y+13)/0.65F),0xB9D1C3);net.minecraft.client.renderer.GlStateManager.popMatrix();}
        fontRenderer.drawString("Ceny ustala administrator serwera.",10,207,0xB9D1C3);
    }
}
