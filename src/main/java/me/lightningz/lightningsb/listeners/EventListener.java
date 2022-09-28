package me.lightningz.lightningsb.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventListener {
    private GuiScreen toOpen;

    public void openGui(GuiScreen guiScreen) {
        toOpen = guiScreen;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (toOpen != null) Minecraft.getMinecraft().displayGuiScreen(toOpen);
        toOpen = null;
    }

    @SubscribeEvent
    public void onChatMessageRecieved(ClientChatReceivedEvent event) {

    }
}
