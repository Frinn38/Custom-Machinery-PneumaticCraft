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
                    makePropertiesCodec().forGetter(HeatGuiElement::getProperties),
                    NamedCodec.intRange(-273, Integer.MAX_VALUE).optionalFieldOf("min", 0).forGetter(HeatGuiElement::getMin),
                    NamedCodec.intRange(-273, Integer.MAX_VALUE).optionalFieldOf("max", 100).forGetter(HeatGuiElement::getMax)
            ).apply(heatGuiElementInstance, HeatGuiElement::new), "Heat gui element"
    );

    private final int min;
    private final int max;

    public HeatGuiElement(Properties properties, int min, int max) {
        super(properties);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
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
        return super.getWidth() == -1 ? 13 : super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight() == -1 ? 50 : super.getHeight();
    }
}
