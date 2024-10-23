package fr.frinn.custommachinerypnc.client.jei.pressure;

import fr.frinn.custommachinery.api.integration.jei.JEIIngredientRenderer;
import fr.frinn.custommachinerypnc.client.jei.CMPncJeiPlugin;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import me.desht.pneumaticcraft.client.render.pressure_gauge.PressureGaugeRenderer2D;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collections;
import java.util.List;

public class PressureJeiIngredientRenderer extends JEIIngredientRenderer<Pressure, PressureGuiElement> {

    private final float danger;
    private final float critical;

    public PressureJeiIngredientRenderer(PressureGuiElement element, float danger, float critical) {
        super(element);
        this.danger = danger;
        this.critical = critical;
    }

    @Override
    public IIngredientType<Pressure> getType() {
        return CMPncJeiPlugin.PRESSURE_INGREDIENT;
    }

    @Override
    public void render(GuiGraphics graphics, Pressure ingredient) {
        PressureGaugeRenderer2D.drawPressureGauge(graphics, Minecraft.getInstance().font, -1, this.critical, Math.min(this.danger, ingredient.max()), ingredient.min(), (ingredient.min() + ingredient.max()) / 2, this.element.getWidth() / 2, this.element.getHeight() / 2);
    }

    @Override
    public int getWidth() {
        return this.element.getWidth();
    }

    @Override
    public int getHeight() {
        return this.element.getHeight();
    }

    //Safe to remove
    @SuppressWarnings("removal")
    @Override
    public List<Component> getTooltip(Pressure ingredient, TooltipFlag tooltipFlag) {
        return Collections.emptyList();
    }
}
