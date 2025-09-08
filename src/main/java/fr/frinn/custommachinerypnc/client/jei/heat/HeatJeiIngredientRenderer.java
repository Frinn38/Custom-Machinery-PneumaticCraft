package fr.frinn.custommachinerypnc.client.jei.heat;

import fr.frinn.custommachinery.api.integration.jei.JEIIngredientRenderer;
import fr.frinn.custommachinerypnc.client.jei.CMPncJeiPlugin;
import fr.frinn.custommachinerypnc.common.guielement.HeatGuiElement;
import me.desht.pneumaticcraft.api.crafting.TemperatureRange;
import me.desht.pneumaticcraft.client.gui.widget.WidgetTemperature;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collections;
import java.util.List;

public class HeatJeiIngredientRenderer extends JEIIngredientRenderer<Heat, HeatGuiElement> {

    private final WidgetTemperature temperatureWidget;

    public HeatJeiIngredientRenderer(HeatGuiElement element, int amount) {
        super(element);
        this.temperatureWidget = new WidgetTemperature(5, 0, TemperatureRange.of(273, 373), amount + 273, 10);
        this.temperatureWidget.autoScaleForTemperature();
    }

    @Override
    public IIngredientType<Heat> getType() {
        return CMPncJeiPlugin.HEAT_INGREDIENT;
    }

    @Override
    public void render(GuiGraphics graphics, Heat ingredient) {
        this.temperatureWidget.render(graphics, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
    }

    @Override
    public int getWidth() {
        return this.element.getWidth();
    }

    @Override
    public int getHeight() {
        return this.element.getHeight();
    }

    @Override
    public List<Component> getTooltip(Heat ingredient, TooltipFlag tooltipFlag) {
        return Collections.emptyList();
    }
}
