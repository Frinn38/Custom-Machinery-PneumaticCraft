package fr.frinn.custommachinerypnc.client.jei.heat;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIElementRenderer;
import fr.frinn.custommachinerypnc.common.guielement.HeatGuiElement;
import fr.frinn.custommachinerypnc.common.requirement.HeatPerTickRequirement;
import fr.frinn.custommachinerypnc.common.requirement.HeatRequirement;
import fr.frinn.custommachinerypnc.common.requirement.TemperatureRequirement;
import me.desht.pneumaticcraft.api.crafting.TemperatureRange;
import me.desht.pneumaticcraft.client.gui.widget.WidgetTemperature;
import net.minecraft.client.gui.GuiGraphics;

public class HeatGuiElementJeiRenderer implements IJEIElementRenderer<HeatGuiElement> {

    private final WidgetTemperature temperatureWidget;

    public HeatGuiElementJeiRenderer() {
        this.temperatureWidget = new WidgetTemperature(0, 0, TemperatureRange.of(273, 373), 300, 10);
        this.temperatureWidget.autoScaleForTemperature();
    }

    @Override
    public void renderElementInJEI(GuiGraphics graphics, HeatGuiElement element, IMachineRecipe recipe, int mouseX, int mouseY) {
        if(recipe.getDisplayInfoRequirements().stream().noneMatch(recipeRequirement ->
                recipeRequirement.requirement() instanceof HeatRequirement
                || recipeRequirement.requirement() instanceof HeatPerTickRequirement
                || recipeRequirement.requirement() instanceof TemperatureRequirement)) {
            this.temperatureWidget.setPosition(element.getX(), element.getY());
            this.temperatureWidget.render(graphics, mouseX, mouseY, 0);
        }
    }
}
