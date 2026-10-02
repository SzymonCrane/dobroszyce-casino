package pl.szymon.casino;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class Snapshot implements IMessage {
    public int window;
    public NBTTagCompound data;
    public Snapshot(){}
    public Snapshot(int window,NBTTagCompound data){this.window=window;this.data=data;}
    @Override public void toBytes(ByteBuf buf){buf.writeInt(window);ByteBufUtils.writeTag(buf,data);}
    @Override public void fromBytes(ByteBuf buf){window=buf.readInt();data=ByteBufUtils.readTag(buf);}
    public static class Handler implements IMessageHandler<Snapshot,IMessage> {
        @Override public IMessage onMessage(Snapshot message,MessageContext ctx){CasinoMod.proxy.receive(message);return null;}
    }
}
