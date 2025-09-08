package fr.frinn.custommachinerypnc.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.crafting.IRequirementList;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.api.requirement.RecipeRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.impl.util.IntRange;
import fr.frinn.custommachinery.impl.util.Restriction;
import fr.frinn.custommachinerypnc.client.jei.heat.Heat;
import fr.frinn.custommachinerypnc.client.jei.heat.HeatJeiIngredientWrapper;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

public record TemperatureRequirement(IntRange range) implements IRequirement<HeatMachineComponent>, IJEIIngredientRequirement<Heat> {

    public static final NamedCodec<TemperatureRequirement> CODEC = NamedCodec.record(temperatureRequirementInstance ->
            temperatureRequirementInstance.group(
                    IntRange.CODEC.fieldOf("range").forGetter(TemperatureRequirement::range)
            ).apply(temperatureRequirementInstance, TemperatureRequirement::new), "PNC Temperature requirement"
    );
    @Override
    public RequirementType<TemperatureRequirement> getType() {
        return Registration.TEMPERATURE_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getComponentType() {
        return Registration.HEAT_COMPONENT.get();
    }

    @Override
    public RequirementIOMode getMode() {
        return RequirementIOMode.INPUT;
    }

    @Override
    public boolean test(HeatMachineComponent component, ICraftingContext context) {
        return this.range.contains(component.getHeatExchanger().getTemperatureAsInt());
    }

    @Override
    public void gatherRequirements(IRequirementList<HeatMachineComponent> list) {
        list.inventoryCondition(((component, context) -> {
            if(this.range.contains(component.getHeatExchanger().getTemperatureAsInt()))
                return CraftingResult.success();
            return CraftingResult.error(Component.translatable("custommachinerypnc.requirements.temperature.error", component.getHeatExchanger().getTemperatureAsInt()));
        }));
    }

    @Override
    public List<IJEIIngredientWrapper<Heat>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        int amount = this.range.getRestrictions().stream().mapToInt(Restriction::lowerBound).min().orElse(0);
        return List.of(new HeatJeiIngredientWrapper(RequirementIOMode.INPUT, new Heat(amount), false, this.range));
    }
}
