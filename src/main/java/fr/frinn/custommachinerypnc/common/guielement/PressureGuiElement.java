package fr.frinn.custommachinerypnc.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent;

public class PressureGuiElement extends AbstractGuiElement implements IComponentGuiElement<PressureMachineComponent> {

    public static final NamedCodec<PressureGuiElement> CODEC = NamedCodec.record(pressureGuiElementInstance ->
            pressureGuiElementInstance.group(
                    makePropertiesCodec().forGetter(PressureGuiElement::getProperties)
            ).apply(pressureGuiElementInstance, PressureGuiElement::new), "Pressure gui element"
    );

    public PressureGuiElement(Properties properties) {
        super(properties);
    }

    @Override
    public GuiElementType<PressureGuiElement> getType() {
        return Registration.PRESSURE_ELEMENT.get();
    }

    @Override
    public MachineComponentType<PressureMachineComponent> getComponentType() {
        return Registration.PRESSURE_COMPONENT.get();
    }

    @Override
    public String getComponentId() {
        return "pressure";
    }

    @Override
    public int getWidth() {
        return super.getWidth() == -1 ? 40 : super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight() == -1 ? 40 : super.getHeight();
    }
}
