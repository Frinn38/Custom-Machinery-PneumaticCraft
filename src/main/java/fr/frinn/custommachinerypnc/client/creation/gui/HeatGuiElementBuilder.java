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
import fr.frinn.custommachinerypnc.common.guielement.HeatGuiElement;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HeatGuiElementBuilder implements IGuiElementBuilder<HeatGuiElement> {

    @Override
    public GuiElementType<HeatGuiElement> type() {
        return Registration.HEAT_ELEMENT.get();
    }

    @Override
    public HeatGuiElement make(Properties properties, @Nullable HeatGuiElement from) {
        return new HeatGuiElement(properties);
    }

    @Override
    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable HeatGuiElement from, Consumer<HeatGuiElement> onFinish) {
        return new HeatGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class HeatGuiElementBuilderPopup extends GuiElementBuilderPopup<HeatGuiElement> {

        public HeatGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable HeatGuiElement from, Consumer<HeatGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        @Override
        public HeatGuiElement makeElement() {
            return new HeatGuiElement(this.properties.build());
        }

        @Override
        public void addWidgets(RowHelper row) {
            this.addPriority(row);
        }
    }
}
