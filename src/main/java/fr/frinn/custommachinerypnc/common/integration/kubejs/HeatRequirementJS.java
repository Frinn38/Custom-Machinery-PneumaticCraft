package fr.frinn.custommachinerypnc.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.impl.util.IntRange;
import fr.frinn.custommachinerypnc.common.requirement.HeatPerTickRequirement;
import fr.frinn.custommachinerypnc.common.requirement.HeatRequirement;
import fr.frinn.custommachinerypnc.common.requirement.TemperatureRequirement;

public interface HeatRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requirePNCHeat(int amount) {
        if(amount <= 0)
            return error("Invalid heat amount specified: {}", amount);
        return addRequirement(new HeatRequirement(RequirementIOMode.INPUT, amount));
    }

    default RecipeJSBuilder producePNCHeat(int amount) {
        if(amount <= 0)
            return error("Invalid heat amount specified: {}", amount);
        return addRequirement(new HeatRequirement(RequirementIOMode.OUTPUT, amount));
    }

    default RecipeJSBuilder requirePNCHeatPerTick(int amount) {
        if(amount <= 0)
            return error("Invalid heat amount specified: {}", amount);
        return addRequirement(new HeatPerTickRequirement(RequirementIOMode.INPUT, amount));
    }

    default RecipeJSBuilder producePNCHeatPerTick(int amount) {
        if(amount <= 0)
            return error("Invalid heat amount specified: {}", amount);
        return addRequirement(new HeatPerTickRequirement(RequirementIOMode.OUTPUT, amount));
    }

    default RecipeJSBuilder requirePNCTemperature(IntRange range) {
        if(range == null)
            return error("Invalid range in PNC temperature requirement");
        return addRequirement(new TemperatureRequirement(range));
    }
}
