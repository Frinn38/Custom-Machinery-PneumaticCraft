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
import fr.frinn.custommachinerypnc.client.jei.pressure.Pressure;
import fr.frinn.custommachinerypnc.client.jei.pressure.PressureJeiIngredientWrapper;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public record PressureRequirement(RequirementIOMode mode, float minPressure, float maxPressure, int volume) implements IRequirement<PressureMachineComponent>, IJEIIngredientRequirement<Pressure> {

    public static final NamedCodec<PressureRequirement> CODEC = NamedCodec.record(pressureRequirementInstance ->
            pressureRequirementInstance.group(
                    RequirementIOMode.CODEC.fieldOf("mode").forGetter(requirement -> requirement.mode),
                    NamedCodec.FLOAT.optionalFieldOf("min", -1f).forGetter(requirement -> requirement.minPressure),
                    NamedCodec.FLOAT.optionalFieldOf("max", 25f).forGetter(requirement -> requirement.maxPressure),
                    NamedCodec.INT.optionalFieldOf("volume", 0).forGetter(requirement -> requirement.volume)
            ).apply(pressureRequirementInstance, PressureRequirement::new), "Pressure requirement"
    );

    @Override
    public RequirementType<PressureRequirement> getType() {
        return Registration.PRESSURE_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<PressureMachineComponent> getComponentType() {
        return Registration.PRESSURE_COMPONENT.get();
    }

    @Override
    public RequirementIOMode getMode() {
        return this.mode;
    }

    @Override
    public boolean test(PressureMachineComponent component, ICraftingContext context) {
        return this.mode == RequirementIOMode.OUTPUT || this.check(component, context).isSuccess();
    }

    @Override
    public void gatherRequirements(IRequirementList<PressureMachineComponent> list) {
        if(this.mode == RequirementIOMode.OUTPUT)
            list.processOnEnd(this::output);
        else {
            list.inventoryCondition(this::check);
            list.processOnStart(this::input);
        }
    }

    private CraftingResult check(PressureMachineComponent component, ICraftingContext context) {
        if(component.getHandler().getPressure() < this.minPressure)
            return CraftingResult.error(Component.translatable("custommachinerypnc.requirements.pressure.error.min", this.minPressure));
        else if(component.getHandler().getPressure() > this.maxPressure)
            return CraftingResult.error(Component.translatable("custommachinerypnc.requirements.pressure.error.max", this.maxPressure));
        else
            return CraftingResult.success();
    }

    private CraftingResult input(PressureMachineComponent component, ICraftingContext context) {
        int volume = (int)context.getIntegerModifiedValue(this.volume, this, "");
        if(component.getHandler().getAir() < volume)
            return CraftingResult.error(Component.translatable("custommachinerypnc.requirements.pressure.error.air", volume, component.getHandler().getAir()));
        component.getHandler().addAir(-volume);
        return CraftingResult.success();
    }

    private CraftingResult output(PressureMachineComponent component, ICraftingContext context) {
        int volume = (int)context.getIntegerModifiedValue(this.volume, this, "");
        component.getHandler().addAir(volume);
        return CraftingResult.success();
    }

    @Override
    public List<IJEIIngredientWrapper<Pressure>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new PressureJeiIngredientWrapper(this.mode, new Pressure(this.minPressure, this.maxPressure, this.volume)));
    }
}
