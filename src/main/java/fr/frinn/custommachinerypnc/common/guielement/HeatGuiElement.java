package fr.frinn.custommachinerypnc.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent;

public class HeatGuiElement extends AbstractGuiElement implements IComponentGuiElement<HeatMachineComponent> {

    public static final NamedCodec<HeatGuiElement> CODEC = NamedCodec.record(heatGuiElementInstance ->
            heatGuiElementInstance.group(
                    makePropertiesCodec().forGetter(HeatGuiElement::getProperties)
            ).apply(heatGuiElementInstance, HeatGuiElement::new), "Heat gui element"
    );

    public HeatGuiElement(Properties properties) {
        super(properties);
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getComponentType() {
        return Registration.HEAT_COMPONENT.get();
    }

    @Override
    public String getComponentId() {
        return "PNCHeat";
    }

    @Override
    public GuiElementType<HeatGuiElement> getType() {
        return Registration.HEAT_ELEMENT.get();
    }

    @Override
    public int getWidth() {
        return super.getWidth() == -1 ? 20 : super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight() == -1 ? 55 : super.getHeight();
    }
}
