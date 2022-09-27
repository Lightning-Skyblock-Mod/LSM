package me.lightningz.lightningsb.commands;

import me.lightningz.lightningsb.Main;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.List;

public class dungeonCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "lsm dungeon";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.RED + "Usage: /lsm dungeon [true|false]"
            ));
            return;
        }
        if (args[0].equalsIgnoreCase("true")){
            Main.INSTANCE.getConfig().OverlayConfig.dungeonSettings = true;
            Main.INSTANCE.saveConfig();
        } else if (args[0].equalsIgnoreCase("false")){
            Main.INSTANCE.getConfig().OverlayConfig.dungeonSettings = false;
            Main.INSTANCE.saveConfig();
        }
        sender.addChatMessage(new ChatComponentText(
            EnumChatFormatting.GOLD + "Set dungeon to:" + args[0]
        ));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    
    
}