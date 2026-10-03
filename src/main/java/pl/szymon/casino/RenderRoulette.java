package pl.szymon.casino;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/** World-space table, wheel, pockets and ball; no open GUI is needed. */
public class RenderRoulette extends TileEntitySpecialRenderer<TileRoulette> {
    private static final double CX=0.91,CZ=1.0;
    @Override public void render(TileRoulette t,double x,double y,double z,float partial,int destroyStage,float alpha){
        if(!t.master()||!t.formed)return;
        GlStateManager.pushMatrix();GlStateManager.translate(x,y,z);GlStateManager.disableLighting();GlStateManager.disableCull();GlStateManager.disableTexture2D();GlStateManager.color(1,1,1,1);
        cube(0,0.77,0,3,0.90,2,0x553222);
        cube(0.06,0.90,0.06,2.94,0.912,1.94,0x175B43);
        cube(0,0.90,0,3,0.94,0.06,0xB19451);cube(0,0.90,1.94,3,0.94,2,0xB19451);
        cube(0,0.90,0.06,0.06,0.94,1.94,0xB19451);cube(2.94,0.90,0.06,3,0.94,1.94,0xB19451);
        for(double lx:new double[]{0.16,2.64})for(double lz:new double[]{0.16,1.64}){cube(lx,0,lz,lx+0.20,0.78,lz+0.20,0x392A23);cube(lx-0.015,0.08,lz-0.015,lx+0.215,0.12,lz+0.215,0xAF8C45);}
        cube(0.25,0.20,0.23,2.75,0.28,0.33,0x493123);cube(0.25,0.20,1.68,2.75,0.28,1.78,0x493123);
        double progress=t.phase==TileRoulette.SPINNING?t.progress(partial):1.0;
        double angle=RouletteRules.wheelAngle(t.startAngle,progress);
        ring(0,0.89,0.946,0x36291F,0);
        ring(0.84,0.89,1.035,0xC0A267,0);ring(0.755,0.84,1.014,0x6F4D30,0);
        ring(0,0.49,1.006,0x865B32,angle);
        BufferBuilder b=begin(GL11.GL_QUADS);
        for(int i=0;i<37;i++){
            int n=RouletteRules.WHEEL[i],color=n==0?0x238054:RouletteRules.red(n)?0xAF3444:0x242B30;
            double a=angle+i*RouletteRules.SECTOR,half=RouletteRules.SECTOR/2;
            sector(b,0.49,0.75,a-half+0.003,a+half-0.003,1.016,color);
            sector(b,0.49,0.75,a+half-0.003,a+half+0.003,1.021,0xCFB576);
        }end();
        ring(0.46,0.49,1.026,0xC0A267,angle);cone(0.19,1.010,1.19,0xB49356);
        for(int i=0;i<37;i++){double a=angle+i*RouletteRules.SECTOR;topText(Integer.toString(RouletteRules.WHEEL[i]),CX+Math.cos(a)*0.691,1.023,CZ+Math.sin(a)*0.691,0.0048F,0xFFF1CF);}
        if(t.pocket>=0){
            double ballAngle=RouletteRules.ballAngle(t.startAngle,t.pocket,progress),r=RouletteRules.ballRadius(progress);
            double bounce=progress>0.63 && progress<0.96?Math.abs(Math.sin(progress*100))*0.032*(1-progress):0;
            sphere(CX+Math.cos(ballAngle)*r,1.062+0.025*(1-progress)+bounce,CZ+Math.sin(ballAngle)*r,0.038);
        }else sphere(CX+0.79,1.07,CZ,0.038);
        board(t);
        topText(t.phase==TileRoulette.SPINNING?"KULKA W RUCHU":t.phase==TileRoulette.RESULT?"WYNIK: "+t.lastNumber:"OBSTAWIANIE",1.47,0.924,1.84,0.0055F,0xF7E1A7);
        topText("PULA: "+t.pot,1.48,0.924,0.16,0.005F,0xF7E1A7);
        GlStateManager.enableTexture2D();GlStateManager.enableCull();GlStateManager.enableLighting();GlStateManager.color(1,1,1,1);GlStateManager.popMatrix();
    }
    private void board(TileRoulette t){
        final double x0=1.92,z0=0.31,w=0.29,h=0.105;
        for(int row=0;row<12;row++)for(int col=0;col<3;col++){
            int n=row*3+col+1;double x=x0+col*w,z=z0+row*h;int color=RouletteRules.red(n)?0xA93542:0x222B2E;
            flat(x,z,x+w-0.008,z+h-0.008,0.916,0xC9B27D);flat(x+0.008,z+0.008,x+w-0.016,z+h-0.016,0.918,color);
            topText(Integer.toString(n),x+w/2,0.923,z+h/2-0.015,0.0048F,0xFFFFFF);
            if(t.lastNumber==n && t.phase==TileRoulette.RESULT)flat(x+0.015,z+0.015,x+0.04,z+0.04,0.925,0xFFD569);
        }
        flat(x0,0.15,x0+w*3-0.008,0.28,0.919,0x237C4B);topText("0",x0+w*1.5,0.924,0.185,0.007F,0xFFFFFF);
        flat(x0,1.62,x0+0.42,1.80,0.918,0xA93542);flat(x0+0.44,1.62,x0+0.86,1.80,0.918,0x222B2E);
        topText("RED",x0+0.21,0.924,1.675,0.006F,0xFFFFFF);topText("BLACK",x0+0.65,0.924,1.675,0.005F,0xFFFFFF);
    }
    private void topText(String s,double x,double y,double z,float scale,int color){
        GlStateManager.enableTexture2D();GlStateManager.pushMatrix();GlStateManager.translate(x,y,z);GlStateManager.rotate(90,1,0,0);GlStateManager.scale(scale,scale,scale);
        getFontRenderer().drawString(s,-getFontRenderer().getStringWidth(s)/2,0,color);GlStateManager.popMatrix();GlStateManager.disableTexture2D();GlStateManager.color(1,1,1,1);
    }
    private static BufferBuilder begin(int mode){BufferBuilder b=Tessellator.getInstance().getBuffer();b.begin(mode,DefaultVertexFormats.POSITION_COLOR);return b;}
    private static void end(){Tessellator.getInstance().draw();}
    private static void vertex(BufferBuilder b,double x,double y,double z,int color){b.pos(x,y,z).color((color>>16)&255,(color>>8)&255,color&255,255).endVertex();}
    private static int shade(int c,double v){return ((int)(((c>>16)&255)*v)<<16)|((int)(((c>>8)&255)*v)<<8)|(int)((c&255)*v);}
    private static void flat(double x,double z,double xx,double zz,double y,int c){BufferBuilder b=begin(GL11.GL_QUADS);vertex(b,x,y,z,c);vertex(b,xx,y,z,c);vertex(b,xx,y,zz,c);vertex(b,x,y,zz,c);end();}
    private static void cube(double x,double y,double z,double xx,double yy,double zz,int c){
        BufferBuilder b=begin(GL11.GL_QUADS);
        vertex(b,x,yy,z,c);vertex(b,xx,yy,z,c);vertex(b,xx,yy,zz,c);vertex(b,x,yy,zz,c);
        int side=shade(c,0.78);
        vertex(b,x,y,z,side);vertex(b,xx,y,z,side);vertex(b,xx,yy,z,side);vertex(b,x,yy,z,side);
        vertex(b,x,y,zz,side);vertex(b,xx,y,zz,side);vertex(b,xx,yy,zz,side);vertex(b,x,yy,zz,side);
        side=shade(c,0.64);
        vertex(b,x,y,z,side);vertex(b,x,y,zz,side);vertex(b,x,yy,zz,side);vertex(b,x,yy,z,side);
        vertex(b,xx,y,z,side);vertex(b,xx,y,zz,side);vertex(b,xx,yy,zz,side);vertex(b,xx,yy,z,side);
        vertex(b,x,y,z,side);vertex(b,xx,y,z,side);vertex(b,xx,y,zz,side);vertex(b,x,y,zz,side);end();
    }
    private static void sector(BufferBuilder b,double r1,double r2,double a,double aa,double y,int c){
        vertex(b,CX+Math.cos(a)*r1,y,CZ+Math.sin(a)*r1,c);vertex(b,CX+Math.cos(a)*r2,y,CZ+Math.sin(a)*r2,c);vertex(b,CX+Math.cos(aa)*r2,y,CZ+Math.sin(aa)*r2,c);vertex(b,CX+Math.cos(aa)*r1,y,CZ+Math.sin(aa)*r1,c);
    }
    private static void ring(double inner,double outer,double y,int color,double rotation){BufferBuilder b=begin(GL11.GL_QUADS);for(int i=0;i<96;i++)sector(b,inner,outer,rotation+i*RouletteRules.TAU/96,rotation+(i+1)*RouletteRules.TAU/96,y,color);end();}
    private static void cone(double r,double bottom,double top,int c){BufferBuilder b=begin(GL11.GL_TRIANGLES);for(int i=0;i<32;i++){double a=i*RouletteRules.TAU/32,aa=(i+1)*RouletteRules.TAU/32;int color=shade(c,0.7+0.3*Math.abs(Math.cos(a)));vertex(b,CX,bottom+(top-bottom),CZ,color);vertex(b,CX+Math.cos(a)*r,bottom,CZ+Math.sin(a)*r,color);vertex(b,CX+Math.cos(aa)*r,bottom,CZ+Math.sin(aa)*r,color);}end();}
    private static void sphere(double x,double y,double z,double r){
        BufferBuilder b=begin(GL11.GL_QUADS);
        for(int lat=0;lat<8;lat++)for(int lon=0;lon<12;lon++){
            double a=-Math.PI/2+lat*Math.PI/8,aa=a+Math.PI/8,phi=lon*RouletteRules.TAU/12,phii=phi+RouletteRules.TAU/12;
            int color=shade(0xFFF8DE,0.65+0.35*(lat/7.0));
            vertex(b,x+r*Math.cos(a)*Math.cos(phi),y+r*Math.sin(a),z+r*Math.cos(a)*Math.sin(phi),color);
            vertex(b,x+r*Math.cos(a)*Math.cos(phii),y+r*Math.sin(a),z+r*Math.cos(a)*Math.sin(phii),color);
            vertex(b,x+r*Math.cos(aa)*Math.cos(phii),y+r*Math.sin(aa),z+r*Math.cos(aa)*Math.sin(phii),color);
            vertex(b,x+r*Math.cos(aa)*Math.cos(phi),y+r*Math.sin(aa),z+r*Math.cos(aa)*Math.sin(phi),color);
        }end();
    }
}
