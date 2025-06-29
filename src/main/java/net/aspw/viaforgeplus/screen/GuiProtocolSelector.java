package net.aspw.viaforgeplus.screen;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.VfpMain;
import net.aspw.viaforgeplus.ViaForgePlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiProtocolSelector extends GuiScreen {

    private final GuiScreen parent;
    private final boolean simple;
    private final FinishedCallback finishedCallback;

    private SlotList list;

    public GuiProtocolSelector(final GuiScreen parent) {
        this(parent, false, (version, unused) -> {
            ViaForgePlus.vfpMain.setTargetVersion(version);
        });
    }

    public GuiProtocolSelector(final GuiScreen parent, final boolean simple, final FinishedCallback finishedCallback) {
        this.parent = parent;
        this.simple = simple;
        this.finishedCallback = finishedCallback;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(1, 5, height - 25, 20, 20, "<-"));
        list = new SlotList(mc, width, height, 3 + 3 + (fontRendererObj.FONT_HEIGHT + 2) * 3, height - 30, fontRendererObj.FONT_HEIGHT + 2);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        list.actionPerformed(button);

        if (button.id == 1) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        list.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        list.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        drawCenteredString(fontRendererObj, ChatFormatting.GOLD + "ViaForgePlus (" + (ViaForgePlus.updatesChecker.isModLatest ? "Latest Build" : "Outdated Build") + ")", width / 4, 3, 16777215);
        GL11.glPopMatrix();

        drawCenteredString(fontRendererObj, "https://nattogreatapi.pages.dev/ViaForgePlus/", width / 2, (fontRendererObj.FONT_HEIGHT + 2) * 2 + 3, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class SlotList extends GuiSlot {

        public SlotList(Minecraft client, int width, int height, int top, int bottom, int slotHeight) {
            super(client, width, height, top, bottom, slotHeight);
        }

        @Override
        protected int getSize() {
            return ProtocolVersion.getProtocols().size();
        }

        @Override
        protected void elementClicked(int index, boolean b, int i1, int i2) {
            finishedCallback.finished(ProtocolVersion.getProtocols().get(index), parent);
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {
            drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int index, int x, int y, int slotHeight, int mouseX, int mouseY) {
            final ProtocolVersion targetVersion = ViaForgePlus.vfpMain.getTargetVersion();
            final ProtocolVersion version = ProtocolVersion.getProtocols().get(index);

            String color;
            if (targetVersion == version) {
                color = GuiProtocolSelector.this.simple ? ChatFormatting.GOLD.toString() : ChatFormatting.GREEN.toString();
            } else {
                color = GuiProtocolSelector.this.simple ? ChatFormatting.WHITE.toString() : ChatFormatting.DARK_RED.toString();
            }

            drawCenteredString(mc.fontRendererObj,(color) + version.getName(), width / 2, y, -1);
        }
    }

    public interface FinishedCallback {

        void finished(final ProtocolVersion version, final GuiScreen parent);
        
    }
    
}
