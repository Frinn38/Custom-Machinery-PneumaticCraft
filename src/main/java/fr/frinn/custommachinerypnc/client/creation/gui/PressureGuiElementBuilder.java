package fr.frinn.custommachinerypnc.client.creation.gui;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement.Properties;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.guielement.PressureGuiElement;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PressureGuiElementBuilder implements IGuiElementBuilder<PressureGuiElement> {

    @Override
    public GuiElementType<PressureGuiElement> type() {
        return Registration.PRESSURE_ELEMENT.get();
    }

    @Override
    public PressureGuiElement make(Properties properties, @Nullable PressureGuiElement from) {
        return new PressureGuiElement(properties);
    }

    @Override
    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable PressureGuiElement from, Consumer<PressureGuiElement> onFinish) {
        return new PressureGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class PressureGuiElementBuilderPopup extends GuiElementBuilderPopup<PressureGuiElement> {

        public PressureGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable PressureGuiElement from, Consumer<PressureGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        @Override
        public PressureGuiElement makeElement() {
            return new PressureGuiElement(this.properties.build());
        }

        @Override
        public void addWidgets(RowHelper row) {
            this.addPriority(row);
        }
    }
}
