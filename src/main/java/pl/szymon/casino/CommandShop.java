package pl.szymon.casino;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
public class CommandShop extends CommandBase {
    @Override public String getName(){return "casinoshop";}
    @Override public int getRequiredPermissionLevel(){return 2;}
    @Override public String getUsage(ICommandSender s){return "/casinoshop reload";}
    @Override public void execute(MinecraftServer server,ICommandSender sender,String[] args)throws CommandException{
        if(args.length!=1 || !"reload".equals(args[0]))throw new WrongUsageException(getUsage(sender));
        try{ShopConfig.reload();for(net.minecraft.entity.player.EntityPlayerMP p:server.getPlayerList().getPlayers())if(p.openContainer instanceof ShopContainer)p.closeScreen();sender.sendMessage(new TextComponentString("Ceny sklepu przeladowane."));}
        catch(java.io.IOException e){throw new CommandException("Nie wczytano cen; zachowano poprzednie. %s",e.getMessage());}
    }
}
