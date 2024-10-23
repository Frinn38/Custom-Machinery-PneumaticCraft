package fr.frinn.custommachinerypnc.client.jei.pressure;

import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.integration.jei.IRecipeHelper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerypnc.client.jei.CMPncJeiPlugin;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent.Template;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import net.minecraft.network.chat.Component;

public record PressureJeiIngredientWrapper(RequirementIOMode mode, Pressure pressure) implements IJEIIngredientWrapper<Pressure> {

    @Override
    public boolean setupRecipe(IRecipeLayoutBuilder builder, int xOffset, int yOffset, IGuiElement element, IRecipeHelper helper) {
        if(!(element instanceof PressureGuiElement pressureElement) || element.getType() != Registration.PRESSURE_ELEMENT.get())
            return false;

        Template template = (Template) helper.getComponentForElement(pressureElement).orElse(null);
        if(template != null)
            builder.addSlot(roleFromMode(this.mode), element.getX() - xOffset + 1, element.getY() - yOffset + 1)
                    .setCustomRenderer(CMPncJeiPlugin.PRESSURE_INGREDIENT, new PressureJeiIngredientRenderer(pressureElement, template.danger(), template.critical()))
                    .addIngredient(CMPncJeiPlugin.PRESSURE_INGREDIENT, this.pressure)
                    .addRichTooltipCallback((view, tooltip) -> {
                        if(this.mode == RequirementIOMode.INPUT)
                            tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.pressure", this.pressure.min(), this.pressure.max()));
                        if(this.pressure.volume() != 0)
                            if(this.mode == RequirementIOMode.INPUT)
                                tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.pressure.volume.input", this.pressure.volume()));
                            else
                                tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.pressure.volume.output", this.pressure.volume()));
                    });
        return true;
    }
}
