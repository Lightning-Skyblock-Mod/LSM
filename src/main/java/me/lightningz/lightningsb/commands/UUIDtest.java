package me.lightningz.lightningsb.commands;

import com.google.gson.JsonObject;
import me.lightningz.lightningsb.utils.HypixelAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.Arrays;

public class UUIDtest extends CommandBase {
    private static JsonObject uuid2 = null;

    @Override
    public String getCommandName() {
        return "lsmuuid";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + "<name>";
    }


    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        String uuid = null;
        try {
            uuid = String.valueOf(HypixelAPI.UsernameToUUID(Arrays.toString(args).replace("[", "").replaceAll("]", "")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "uuid: " + uuid));

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
