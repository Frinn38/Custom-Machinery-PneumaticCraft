package fr.frinn.custommachinerypnc.common;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerypnc.CustomMachineryPnc;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent.Template;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent;
import fr.frinn.custommachinerypnc.common.guielement.HeatGuiElement;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import fr.frinn.custommachinerypnc.common.requirement.PressureRequirement;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Registration {

    public static final DeferredRegister<GuiElementType<?>> GUI_ELEMENTS = DeferredRegister.create(GuiElementType.REGISTRY_KEY, CustomMachinery.MODID);
    public static final DeferredRegister<MachineComponentType<?>> MACHINE_COMPONENTS = DeferredRegister.create(MachineComponentType.REGISTRY_KEY, CustomMachinery.MODID);
    public static final DeferredRegister<RequirementType<?>> REQUIREMENTS = DeferredRegister.create(RequirementType.REGISTRY_KEY, CustomMachineryPnc.MODID);

    public static final Supplier<GuiElementType<PressureGuiElement>> PRESSURE_ELEMENT = GUI_ELEMENTS.register("pressure", () -> GuiElementType.create(PressureGuiElement.CODEC));
    public static final Supplier<GuiElementType<HeatGuiElement>>     HEAT_ELEMENT     = GUI_ELEMENTS.register("pnc_heat", () -> GuiElementType.create(HeatGuiElement.CODEC));

    public static final Supplier<MachineComponentType<PressureMachineComponent>> PRESSURE_COMPONENT = MACHINE_COMPONENTS.register("pressure", () -> MachineComponentType.create(PressureMachineComponent.Template.CODEC));
    public static final Supplier<MachineComponentType<HeatMachineComponent>>     HEAT_COMPONENT     = MACHINE_COMPONENTS.register("pnc_heat", () -> MachineComponentType.create(Template.CODEC));

    public static final Supplier<RequirementType<PressureRequirement>> PRESSURE_REQUIREMENT = REQUIREMENTS.register("pressure", () -> RequirementType.inventory(PressureRequirement.CODEC));
}
