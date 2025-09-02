package fr.frinn.custommachinerypnc.client.guielement;

import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.guielement.HeatGuiElement;
import me.desht.pneumaticcraft.api.crafting.TemperatureRange;
import me.desht.pneumaticcraft.client.gui.widget.WidgetTemperature;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class HeatGuiElementWidget extends AbstractGuiElementWidget<HeatGuiElement> {

    private final WidgetTemperature temperatureWidget;

    public HeatGuiElementWidget(HeatGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("heat"));
        this.temperatureWidget = new WidgetTemperature(this.getX(), this.getY(), TemperatureRange.of(element.getMin() + 273, element.getMax() + 273), 300, 10);
        this.temperatureWidget.autoScaleForTemperature();
        this.setSize(this.temperatureWidget.getWidth(), this.temperatureWidget.getHeight());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.getScreen().getTile().getComponentManager()
                .getComponent(Registration.HEAT_COMPONENT.get())
                .ifPresent(component -> this.temperatureWidget.setTemperature(component.getHeatExchanger().getTemperatureAsInt()));
        this.temperatureWidget.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.temperatureWidget.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.temperatureWidget.setY(y);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }
}
