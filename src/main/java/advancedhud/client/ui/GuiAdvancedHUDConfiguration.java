package advancedhud.client.ui;

import advancedhud.SaveController;
import advancedhud.api.HUDRegistry;
import advancedhud.api.HudItem;
import advancedhud.client.huditems.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiAdvancedHUDConfiguration extends GuiScreen {

    private static boolean asMount = false;
    private static boolean help = true;

    @Override
    public void initGui() {
        super.initGui();
        addButtons();
    }

    @SuppressWarnings("unchecked")
    private void addButtons() {
        buttonList.clear();
        buttonList.add(new GuiButton(-1, HUDRegistry.screenWidth - 30, 10, 20, 20, "X"));
        for (HudItem huditem : HUDRegistry.getHudItemList()) {
            if (asMount && huditem.shouldDrawOnMount()) {
                buttonList.add(new GuiHudItemButton(huditem.getDefaultID(), huditem.posX, huditem.posY, huditem.getWidth(), huditem.getHeight(), huditem.getButtonLabel()));
            } else if (!asMount && huditem.shouldDrawAsPlayer()) {
                buttonList.add(new GuiHudItemButton(huditem.getDefaultID(), huditem.posX, huditem.posY, huditem.getWidth(), huditem.getHeight(), huditem.getButtonLabel()));
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();

        if (!HUDRegistry.checkForResize()) {
            initGui();
        }

        if (help) {
            drawCenteredString(mc.fontRendererObj, "LEFT CLICK to reposition, RIGHT CLICK to change settings", width / 2, 17, 0xFFFFFF);
            drawCenteredString(mc.fontRendererObj, "ESCAPE to cancel, R to reset all", width / 2, 27, 0xFFFFFF);
            drawCenteredString(mc.fontRendererObj, "M to change to" + (asMount ? " player " : " mount ") + "HUD", width / 2, 37, 0xFFFFFF);
        }

        super.drawScreen(par1, par2, par3);
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        if (par2 == 19) {
            HUDRegistry.resetAllDefaults();
            HUDRegistry.getHudItemList().clear();
            HUDRegistry.registerHudItem(new HudItemHotbar());
            HUDRegistry.registerHudItem(new HudItemHealth());
            HUDRegistry.registerHudItem(new HudItemAir());
            HUDRegistry.registerHudItem(new HudItemFood());
            HUDRegistry.registerHudItem(new HudItemArmor());
            HUDRegistry.registerHudItem(new HudItemBossBar());
            HUDRegistry.registerHudItem(new HudItemJumpBar());
            HUDRegistry.registerHudItem(new HudItemHealthMount());
            HUDRegistry.registerHudItem(new HudItemExperienceBar());
            HUDRegistry.registerHudItem(new HudItemCrosshairs());
            HUDRegistry.registerHudItem(new HudItemTooltips());
            HUDRegistry.registerHudItem(new HudItemRecordDisplay());
            initGui();
        } else if (par2 == Keyboard.KEY_M) {
            asMount = !asMount;
            initGui();
        } else if (par2 == Keyboard.KEY_F1) {
            help = !help;
        }
        SaveController.saveConfig("config");
        super.keyTyped(par1, par2);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) throws IOException {
        if (par1GuiButton.id == -1) {
            mc.displayGuiScreen(null);
            SaveController.saveConfig("config");
        }
        if (par1GuiButton instanceof GuiHudItemButton) {
            HudItem hudItem = HUDRegistry.getHudItemByID(par1GuiButton.id);
            if (hudItem != null && hudItem.isMoveable()) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenReposition(this, hudItem));
            }
        }
        super.actionPerformed(par1GuiButton);
    }

    @Override
    public void mouseClicked(int i, int j, int mouseButton) throws IOException {
        if (mouseButton == 1) {
            for (Object button : buttonList) {
                GuiButton guibutton = (GuiButton) button;

                if (guibutton.mousePressed(mc, i, j)) {
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                    HudItem hudItem = HUDRegistry.getHudItemByID(guibutton.id);
                    if (hudItem != null) {
                        Minecraft.getMinecraft().displayGuiScreen(hudItem.getConfigScreen());
                    }
                }
            }
        }
        super.mouseClicked(i, j, mouseButton);
    }

}
