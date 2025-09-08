package fr.frinn.custommachinerypnc.client.jei.heat;

import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.integration.jei.IRecipeHelper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.impl.util.IntRange;
import fr.frinn.custommachinerypnc.client.jei.CMPncJeiPlugin;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent.Template;
import fr.frinn.custommachinerypnc.common.guielement.HeatGuiElement;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public record HeatJeiIngredientWrapper(RequirementIOMode mode, Heat heat, boolean perTick, @Nullable IntRange range) implements IJEIIngredientWrapper<Heat> {

    @Override
    public boolean setupRecipe(IRecipeLayoutBuilder builder, int xOffset, int yOffset, IGuiElement element, IRecipeHelper helper) {
        if(!(element instanceof HeatGuiElement heatElement) || element.getType() != Registration.HEAT_ELEMENT.get())
            return false;

        Template template = (Template) helper.getComponentForElement(heatElement).orElse(null);
        if(template != null)
            builder.addSlot(roleFromMode(this.mode), element.getX() - xOffset + 1, element.getY() - yOffset + 1)
                    .setCustomRenderer(CMPncJeiPlugin.HEAT_INGREDIENT, new HeatJeiIngredientRenderer(heatElement, this.heat.amount()))
                    .addIngredient(CMPncJeiPlugin.HEAT_INGREDIENT, this.heat)
                    .addRichTooltipCallback((view, tooltip) -> {
                        if(this.mode == RequirementIOMode.INPUT)
                            if(this.range != null)
                                tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.temperature.range", this.range.toFormattedString()));
                            else if(this.perTick)
                                tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.heat.input.per-tick", this.heat.amount()));
                            else
                                tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.heat.input", this.heat.amount()));
                        else
                            if(this.perTick)
                                tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.heat.output.per-tick", this.heat.amount()));
                            else
                                tooltip.add(Component.translatable("custommachinerypnc.jei.ingredient.heat.output", this.heat.amount()));
                    });
        return true;
    }
}
