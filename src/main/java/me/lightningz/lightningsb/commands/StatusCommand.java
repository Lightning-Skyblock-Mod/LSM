package me.lightningz.lightningsb.commands;

import com.google.gson.JsonObject;
import me.lightningz.lightningsb.Main;
import me.lightningz.lightningsb.utils.HypixelAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StatusCommand extends CommandBase {




        private static JsonObject playerStatus = null;
        private static JsonObject uuid2 = null;
        private static final AtomicBoolean updatingPlayerStatusState = new AtomicBoolean(false);

        public static JsonObject getPlayerStatus(String name) {
            if (playerStatus != null) return playerStatus;
            if (updatingPlayerStatusState.get()) return null;

            String uuid = HypixelAPI.getUUID(name,  jsonObject -> {
                if (jsonObject == null) return;

                updatingPlayerStatusState.set(false);
                if (jsonObject.has("success") && jsonObject.get("success").getAsBoolean()) {
                    uuid2 = jsonObject.get("owner").getAsJsonObject();
                }
            });

            updatingPlayerStatusState.set(true);

            HashMap<String, String> args = new HashMap<>();
            args.put("uuid", "" + uuid);
            me.lightningz.lightningsb.utils.HypixelAPI.getHypixelApiAsync(Main.INSTANCE.getConfig().apiKey, "status",
                    args, jsonObject -> {
                        if (jsonObject == null) return;

                        updatingPlayerStatusState.set(false);
                        if (jsonObject.has("success") && jsonObject.get("success").getAsBoolean()) {
                            playerStatus = jsonObject.get("session").getAsJsonObject();
                        }
                    }, () -> updatingPlayerStatusState.set(false)
            );

            return null;

    }



    @Override
    public String getCommandName() {
        return "lsmstatus";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <username>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        final EntityPlayer player = (EntityPlayer)sender;
        String location = null;
        if (args.length == 0) {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(sender)));
            return;
        }
        JsonObject status = getPlayerStatus(Arrays.toString(args));
        if (status != null && status.has("mode")) {
            location = status.get("mode").getAsString();
        }
        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GOLD + "Status: " + status + " - " + location
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
