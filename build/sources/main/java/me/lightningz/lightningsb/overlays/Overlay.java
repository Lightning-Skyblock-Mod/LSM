package me.lightningz.lightningsb.overlays;

import me.lightningz.lightningsb.Main;
import me.lightningz.lightningsb.buttons.IconButton;
import me.lightningz.lightningsb.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.util.ArrayList;
import java.util.List;

public class Overlay extends GuiScreen {

    private int guiLeft, guiTop;
    private int offsetX, offsetY;
    private float guiScale;
    private int sizeX;
    private int sizeY;
    private int button_sizeX;
    private float button_sizeY;
    private boolean isDragging;
    private boolean save = true;
    private List<IconButton> iconButtons = new ArrayList<>();

    private static final ResourceLocation gui_bg = new ResourceLocation("lightningsb:sprite-0001.png");
    private static final ResourceLocation gui_button = new ResourceLocation("lightningsb:Sprite-0002.png");



    @Override
    public void initGui() {
        super.initGui();

    }

    public void calculateOffset(int mouseX, int mouseY) {
        offsetX = guiLeft - mouseX;
        offsetY = guiTop - mouseY;
    }

    public void tick(int mouseX, int mouseY) {
        int lastGuiLeft = guiLeft, lastGuiTop = guiTop;
        guiLeft = Math.round(Math.max(Math.min(isDragging ? mouseX + offsetX : guiLeft, width - 256 * guiScale), 0));
        guiTop = Math.round(Math.max(Math.min(isDragging ? mouseY + offsetY : guiTop, height - 256 * guiScale), 0));

        if (wouldRenderOutOfBoundsX(guiLeft, guiScale)) {
            guiLeft = lastGuiLeft;
            calculateOffset(mouseX, mouseY);
        }
        if (wouldRenderOutOfBoundsY(guiTop, guiScale)) {
            guiTop = lastGuiTop;
            calculateOffset(mouseX, mouseY);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        this.sizeX = 431;
        this.sizeY = 202;
        this.guiLeft = (this.width - this.sizeX) / 2;
        this.guiTop = (this.height - this.sizeY) / 2;
        this.button_sizeX = 128;
        this.button_sizeY = 16.5F;
        this.guiLeft = (this.width - this.sizeX) / 2;
        this.guiTop = (this.height - this.sizeY) / 2;
        Minecraft.getMinecraft().getTextureManager().bindTexture(gui_bg);

        Utils.drawTexturedRect(guiLeft, guiTop, sizeX, sizeY, GL11.GL_NEAREST);

        Minecraft.getMinecraft().getTextureManager().bindTexture(gui_button);
        Utils.drawTexturedRect(1, 8, -32+8, 194, 16);

        GlStateManager.enableDepth();
        GlStateManager.translate(0, 0, 5);
        GlStateManager.translate(0, 0, -3);

        GlStateManager.disableDepth();
        GlStateManager.translate(0, 0, -2);
        GlStateManager.translate(0, 0, 2);

        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        iconButtons.add(new IconButton(0, guiLeft, guiTop, button_sizeX, (int) button_sizeY).withTooltip(EnumChatFormatting.GOLD + "Test"));
        for (IconButton button: iconButtons) {
            button.drawButton(Minecraft.getMinecraft(), mouseX - guiLeft, mouseY - guiTop);
        }


        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    public static void drawButton(GuiButton button, Minecraft mc, int mouseX, int mouseY) {
        button.drawButton(mc, mouseX, mouseY);
    }

    private boolean wouldRenderOutOfBoundsX(int x, float sf) {
        return (x == 0 || x >= width - 1 - 256 * sf);
    }

    private boolean wouldRenderOutOfBoundsY(int y, float sf) {
        return (y <= 32 * sf || y >= height - 1 - 256 * sf);
    }

    @Override
    public void onGuiClosed() {
        save();
    }

    private void save() {
        if (save) {
            boolean relative = Main.INSTANCE.getConfig().OverlayConfig.relativeGui;
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            Main.INSTANCE.getConfig().OverlayConfig.guiLeft = relative ? Math.round(((float) (guiLeft == 0 ? 1 : guiLeft) / ((float) sr.getScaledWidth())) * 1000) : guiLeft;
            Main.INSTANCE.getConfig().OverlayConfig.guiTop = relative ? Math.round(((float) (guiTop == 0 ? 1 : guiTop) / ((float) sr.getScaledHeight())) * 1000) : guiTop;
            Main.INSTANCE.getConfig().OverlayConfig.guiScale = relative ? (255f * guiScale) / sr.getScaledWidth() : guiScale;
            Main.INSTANCE.saveConfig();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            guiScale = 1;
            guiLeft = 1;
            guiTop = 33;
        }
    }

}