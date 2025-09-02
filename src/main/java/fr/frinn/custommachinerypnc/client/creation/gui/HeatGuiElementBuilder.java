package fr.frinn.custommachinerypnc.client.creation.gui;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.IntegerSlider;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement.Properties;
import fr.frinn.custommachinerypnc.client.creation.gui.PressureGuiElementBuilder.PressureGuiElementBuilderPopup;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.guielement.HeatGuiElement;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HeatGuiElementBuilder implements IGuiElementBuilder<HeatGuiElement> {

    @Override
    public GuiElementType<HeatGuiElement> type() {
        return Registration.HEAT_ELEMENT.get();
    }

    @Override
    public HeatGuiElement make(Properties properties, @Nullable HeatGuiElement from) {
        if(from != null)
            return new HeatGuiElement(properties, from.getMin(), from.getMax());
        else
            return new HeatGuiElement(properties, 0, 100);
    }

    @Override
    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable HeatGuiElement from, Consumer<HeatGuiElement> onFinish) {
        return new HeatGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class HeatGuiElementBuilderPopup extends GuiElementBuilderPopup<HeatGuiElement> {

        private int min = 0;
        private int max = 100;

        public HeatGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable HeatGuiElement from, Consumer<HeatGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
            if(from != null) {
                this.min = from.getMin();
                this.max = from.getMax();
            }
        }

        @Override
        public HeatGuiElement makeElement() {
            return new HeatGuiElement(this.properties.build(), this.min, this.max);
        }

        @Override
        public void addWidgets(RowHelper row) {
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinerypnc.gui.creation.gui.heat.min"), this.font));
            row.addChild(IntegerSlider.builder().bounds(-273, 1000).defaultValue(this.min).displayOnlyValue().setResponder(value -> this.min = value).create(0, 0, 100, 20, Component.empty()));
            row.addChild(new StringWidget(Component.translatable("custommachinerypnc.gui.creation.gui.heat.max"), this.font));
            row.addChild(IntegerSlider.builder().bounds(-273, 1000).defaultValue(this.max).displayOnlyValue().setResponder(value -> this.max = value).create(0, 0, 100, 20, Component.empty()));
        }
    }
}
