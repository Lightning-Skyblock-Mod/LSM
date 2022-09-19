package me.lightningz.lightningsb.buttons;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;
import com.google.common.collect.Lists;
import me.lightningz.lightningsb.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;


public class IconButton extends CustomButton{
    private int textureX, textureY;

    private Supplier<Boolean> enabledPredicate = null;
    private Runnable clickCallback = null;

    private List<String> tooltip = new ArrayList<>();

    public IconButton(int buttonId, int x, int y, int textureX, int textureY) {
        super(buttonId, x, y, 16, 16, "");
        this.textureX = textureX;
        this.textureY = textureY;
    }
    public IconButton withTooltip(String... lore) {
        tooltip = Lists.newArrayList(lore);
        return this;
    }

    public IconButton withEnabledPredicate(Supplier<Boolean> pred) {
        enabledPredicate = pred;
        return this;
    }

    public IconButton withClickCallback(Runnable cb) {
        clickCallback = cb;
        return this;
    }

    public void tick() {
        if (enabledPredicate != null)
            enabled = enabledPredicate.get();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = isScaledHover(mouseX, mouseY);
        if (this.visible && this.enabled) {
            mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "Sprite-0002.png"));
            drawTexturedModalRect(xPosition, yPosition, textureX, textureY, 16, 16);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return enabled && isScaledHover(mouseX, mouseY);
    }


    public List<String> getTooltip() {
        return tooltip;
    }

    public Runnable getClickCallback() {
        return clickCallback;
    }
}
