package kiln;

import calebzhou.skyutils.SkyutilsMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class KilnScreen extends AbstractContainerScreen<KilnScreenHandler> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(SkyutilsMod.MOD_ID, "textures/gui/container/kiln.png");

    public KilnScreen(KilnScreenHandler container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageHeight = 114 + 6 * 18;
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        float p = (float) ((KilnScreenHandler) this.menu).getProgress() / 10.0f;
        String string = this.title.getString() + " " + String.format("%.01f", p) + "%";
        this.resize(minecraft, width, height);
        this.font.draw(matrices, string,
                (float) (this.imageWidth / 2 - this.font.width(string) / 2), 6.0F, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {

        // RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        // client.getTextureManager().bindTexture(TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrices, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int p = ((KilnScreenHandler) this.menu).getBurnTime();
        float prog = (p / (float) KilnBlockEntity.CHARCOAL_BURN_TIME);
        if (prog != 0) {
            int l = (int) (12.0 * prog);
            this.blit(matrices, i + 56, j + 36 + 12 - l, 176, 12 - l, 14, l + 1);
        }
        float prog2 = (float) ((KilnScreenHandler) this.menu).getProgress() / 1000.0f;
        if (prog2 != 0) {
            this.blit(matrices, i + 79, j + 34, 176, 14, (int) (24 * prog2) + 1, 16);
        }
    }

}