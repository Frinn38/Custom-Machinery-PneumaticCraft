package fr.frinn.custommachinerypnc.client.jei.pressure;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIElementRenderer;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import fr.frinn.custommachinerypnc.common.requirement.PressureRequirement;
import me.desht.pneumaticcraft.client.render.pressure_gauge.PressureGaugeRenderer2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class PressureGuiElementJeiRenderer implements IJEIElementRenderer<PressureGuiElement> {

    @Override
    public void renderElementInJEI(GuiGraphics graphics, PressureGuiElement element, IMachineRecipe recipe, int mouseX, int mouseY) {
        if(recipe.getDisplayInfoRequirements().stream().noneMatch(recipeRequirement -> recipeRequirement.requirement() instanceof PressureRequirement))
            PressureGaugeRenderer2D.drawPressureGauge(graphics, Minecraft.getInstance().font, 0, 5, 5, 0, 0, element.getX() + element.getWidth() / 2, element.getY() + element.getHeight() / 2);
    }
}
