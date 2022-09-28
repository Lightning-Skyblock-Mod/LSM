package me.lightningz.lightningsb.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class LSMConfig {
    public @interface ConfigCategory {

        String name();
        String description();

    }

    public void processConfig() {
        if (!configOpened) {
            OverlayConfig.setRelativeGui(true);
        }
    }

    @Expose
    @SerializedName("config_opened")
    public boolean configOpened = false;

    @ConfigCategory(name = "Overlay", description = "Overlay Config")
    @SerializedName("overlay")
    @Expose
    public OverlayConfig OverlayConfig = new OverlayConfig();

    @Expose
    @SerializedName("api_key")
    public String apiKey = "";

    public static class OverlayConfig {

        @Expose
        @SerializedName("gui_scale")
        public float guiScale = 0.5f;

        @Expose
        @SerializedName("gui_left")
        public int guiLeft = 0;

        @Expose
        @SerializedName("gui_top")
        public int guiTop = 0;

        @Expose
        @SerializedName("relative_gui")
        public boolean relativeGui = true;

        @Expose
        @SerializedName("dungeon_settings")
        public boolean dungeonSettings = false;

        public void setRelativeGui(boolean relativeGui) {
            this.relativeGui = relativeGui;
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            guiScale = relativeGui ? (255f * guiScale) / sr.getScaledWidth() : guiScale * sr.getScaledWidth() / 255f;
            guiTop = relativeGui ? (sr.getScaledHeight() - Math.round(256f * guiScale)) / 2 : (sr.getScaledHeight() - 256) / 2;
            guiLeft = relativeGui ? Math.round(sr.getScaledWidth() - 256f * (guiScale * sr.getScaledWidth()) / 255f) : Math.round(sr.getScaledWidth() - 256 * guiScale);
        }

    }

}
