package me.lightningz.lightningsb.commands;

import me.lightningz.lightningsb.Main;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;

public class ApiCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "apikey";
    }

    @Override
    public String getCommandUsage(ICommandSender arg0) {
        return "/" + getCommandName() + " <key>";
    }

    public static String usage(ICommandSender arg0) {
        return new ApiCommand().getCommandUsage(arg0);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) throws CommandException {
        final EntityPlayer player = (EntityPlayer)arg0;

        if (arg1.length == 0) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(arg0)));
            return;
        }

        Main.INSTANCE.getConfig().apiKey = Arrays.toString(arg1);
        Main.INSTANCE.saveConfig();
        player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD+ "Set API key to " + EnumChatFormatting.BLUE + arg1[0]));
    }
}
