package fr.frinn.custommachinerypnc.common;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerypnc.CustomMachineryPnc;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent.Template;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import fr.frinn.custommachinerypnc.common.requirement.PressureRequirement;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Registration {

    public static final DeferredRegister<GuiElementType<?>> GUI_ELEMENTS = DeferredRegister.create(GuiElementType.REGISTRY_KEY, CustomMachinery.MODID);
    public static final DeferredRegister<MachineComponentType<?>> MACHINE_COMPONENTS = DeferredRegister.create(MachineComponentType.REGISTRY_KEY, CustomMachinery.MODID);
    public static final DeferredRegister<RequirementType<?>> REQUIREMENTS = DeferredRegister.create(RequirementType.REGISTRY_KEY, CustomMachineryPnc.MODID);

    public static final Supplier<GuiElementType<PressureGuiElement>> PRESSURE_ELEMENT = GUI_ELEMENTS.register("pressure", () -> GuiElementType.create(PressureGuiElement.CODEC));

    public static final Supplier<MachineComponentType<PressureMachineComponent>> PRESSURE_COMPONENT = MACHINE_COMPONENTS.register("pressure", () -> MachineComponentType.create(Template.CODEC));

    public static final Supplier<RequirementType<PressureRequirement>> PRESSURE_REQUIREMENT = REQUIREMENTS.register("pressure", () -> RequirementType.inventory(PressureRequirement.CODEC));
}
