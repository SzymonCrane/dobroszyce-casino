package pl.szymon.casino;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandChips extends CommandBase {
    @Override public String getName(){return "casinochips";}
    @Override public String getUsage(ICommandSender sender){return "/casinochips <gracz> <wartosc: wielokrotnosc 25>";}
    @Override public int getRequiredPermissionLevel(){return 2;}
    @Override public void execute(MinecraftServer server,ICommandSender sender,String[] args)throws CommandException{
        if(args.length!=2)throw new WrongUsageException(getUsage(sender));
        EntityPlayerMP p=getPlayer(server,sender,args[0]);int amount=parseInt(args[1],25,1000000);
        if(amount%25!=0)throw new CommandException("Wartosc musi byc wielokrotnoscia 25.");
        Chips.give(p,amount);sender.sendMessage(new TextComponentString("Przyznano "+amount+" zetonow graczowi "+p.getName()));
    }
}
