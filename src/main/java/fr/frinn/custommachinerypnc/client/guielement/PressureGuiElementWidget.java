package fr.frinn.custommachinerypnc.client.guielement;

import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.client.render.pressure_gauge.PressureGaugeRenderer2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public class PressureGuiElementWidget extends AbstractGuiElementWidget<PressureGuiElement> {

    public PressureGuiElementWidget(PressureGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("Pressure"));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.pose().pushPose();
        float scaleX = this.width / 40f;
        float scaleY = this.height / 40f;
        graphics.pose().translate(this.getX() + this.width / 2f, this.getY() + this.height / 2f, 0);
        graphics.pose().scale(scaleX, scaleY, 1);
        this.getScreen()
                .getTile()
                .getComponentManager()
                .getComponent(Registration.PRESSURE_COMPONENT.get())
                .ifPresentOrElse(component -> {
                    PressureGaugeRenderer2D.drawPressureGauge(graphics, Minecraft.getInstance().font, -1, component.getHandler().getCriticalPressure(), component.getHandler().getDangerPressure(), -Float.MAX_VALUE, component.getHandler().getPressure(), 0, 0);
                }, () -> {
                    PneumaticRegistry.getInstance().getClientRegistry().drawPressureGauge(graphics, Minecraft.getInstance().font, -1, 5, 4, -Float.MAX_VALUE, 0, 0, 0);
                });
        graphics.pose().popPose();
    }

    @Override
    public List<Component> getTooltips() {
        if(!super.getTooltips().isEmpty())
            return super.getTooltips();
        return this.getScreen().getTile().getComponentManager()
                .getComponent(Registration.PRESSURE_COMPONENT.get())
                .map(component -> Collections.singletonList((Component)Component.translatable("custommachinerypnc.gui.element.pressure.tooltip", String.format("%.2f", component.getHandler().getPressure()))))
                .orElse(Collections.emptyList());
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return false;
    }
}
