package fr.frinn.custommachinerypnc.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerypnc.common.requirement.PressureRequirement;

public interface PressureRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requirePressure(float min, float max) {
        return requirePressure(min, max, 0);
    }

    default RecipeJSBuilder requirePressure(float min, float max, int volume) {
        return addRequirement(new PressureRequirement(RequirementIOMode.INPUT, min, max, volume));
    }

    default RecipeJSBuilder producePressure(int volume) {
        return addRequirement(new PressureRequirement(RequirementIOMode.OUTPUT, -1, 25, volume));
    }
}
