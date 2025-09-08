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
import fr.frinn.custommachinerypnc.client.jei.heat.Heat;
import fr.frinn.custommachinerypnc.client.jei.heat.HeatJeiIngredientWrapper;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

public record HeatRequirement(RequirementIOMode mode, int amount) implements IRequirement<HeatMachineComponent>, IJEIIngredientRequirement<Heat> {

    public static final NamedCodec<HeatRequirement> CODEC = NamedCodec.record(heatRequirementInstance ->
            heatRequirementInstance.group(
                    RequirementIOMode.CODEC.fieldOf("mode").forGetter(HeatRequirement::mode),
                    NamedCodec.intRange(1, Integer.MAX_VALUE).fieldOf("amount").forGetter(HeatRequirement::amount)
            ).apply(heatRequirementInstance, HeatRequirement::new), "PNC Heat requirement"
    );

    @Override
    public RequirementType<HeatRequirement> getType() {
        return Registration.HEAT_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getComponentType() {
        return Registration.HEAT_COMPONENT.get();
    }

    @Override
    public RequirementIOMode getMode() {
        return this.mode;
    }

    @Override
    public boolean test(HeatMachineComponent component, ICraftingContext context) {
        return this.mode == RequirementIOMode.OUTPUT || component.getHeatExchanger().getTemperature() * component.getHeatExchanger().getThermalCapacity() >= this.amount;
    }

    @Override
    public void gatherRequirements(IRequirementList<HeatMachineComponent> list) {
        if(this.mode == RequirementIOMode.INPUT)
            list.processOnStart(this::processInput);
        else
            list.processOnEnd(this::processOutput);
    }

    private CraftingResult processInput(HeatMachineComponent component, ICraftingContext context) {
        double amount = context.getModifiedValue(this.amount, this, null);

        if(component.getHeatExchanger().getTemperature() * component.getHeatExchanger().getThermalCapacity() < amount)
            return CraftingResult.error(Component.translatable("custommachinerypnc.requirements.heat.error.input"));

        component.getHeatExchanger().addHeat(-amount);
        return CraftingResult.success();
    }

    private CraftingResult processOutput(HeatMachineComponent component, ICraftingContext context) {
        double amount = context.getModifiedValue(this.amount, this, null);
        component.getHeatExchanger().addHeat(amount);
        return CraftingResult.success();
    }

    @Override
    public List<IJEIIngredientWrapper<Heat>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return List.of(new HeatJeiIngredientWrapper(this.mode, new Heat(this.amount), false, null));
    }
}
