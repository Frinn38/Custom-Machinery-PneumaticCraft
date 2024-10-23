package fr.frinn.custommachinerypnc.client.creation.component;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentConfigBuilderWidget;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.FloatSlider;
import fr.frinn.custommachinery.client.screen.widget.IntegerEditBox;
import fr.frinn.custommachinery.impl.component.config.IOSideConfig;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent.Template;
import me.desht.pneumaticcraft.common.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PressureComponentBuilder implements IMachineComponentBuilder<PressureMachineComponent, Template> {

    @Override
    public MachineComponentType<PressureMachineComponent> type() {
        return Registration.PRESSURE_COMPONENT.get();
    }

    @Override
    public PopupScreen makePopup(MachineEditScreen parent, @Nullable Template template, Consumer<Template> onFinish) {
        return new PressureComponentBuilderPopup(parent, template, onFinish);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, Template template) {
        graphics.renderFakeItem(ModItems.PRESSURE_GAUGE.toStack(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
    }

    public static class PressureComponentBuilderPopup extends ComponentBuilderPopup<Template> {

        private IntegerEditBox volume;
        private FloatSlider danger;
        private FloatSlider critical;
        private IOSideConfig.Template config;

        public PressureComponentBuilderPopup(BaseScreen parent, @Nullable Template template, Consumer<Template> onFinish) {
            super(parent, template, onFinish, Component.translatable("custommachinerypnc.gui.creation.components.pressure.title"));
        }

        @Override
        public Template makeTemplate() {
            return new Template(this.volume.getIntValue(), this.danger.floatValue(), this.critical.floatValue(), this.config);
        }

        @Override
        public Component canCreate() {
            if(this.critical.floatValue() < this.danger.floatValue())
                return Component.translatable("custommachinerypnc.gui.creation.components.pressure.critical_inferior");
            return super.canCreate();
        }

        @Override
        protected void init() {
            super.init();

            //Capacity
            this.volume = this.propertyList.add(Component.translatable("custommachinerypnc.gui.creation.components.pressure.volume"), new IntegerEditBox(this.font, 0, 0, 180, 20, Component.translatable("custommachinerypnc.gui.creation.components.pressure.volume")));
            this.volume.bounds(0, Integer.MAX_VALUE);
            this.baseTemplate().ifPresentOrElse(template -> this.volume.setValue("" + template.volume()), () -> this.volume.setValue("5000"));
            this.volume.setTooltip(Tooltip.create(Component.translatable("custommachinerypnc.gui.creation.components.pressure.volume.tooltip")));

            //Max input
            this.danger = this.propertyList.add(Component.translatable("custommachinerypnc.gui.creation.components.pressure.danger"), new FloatSlider.Builder().bounds(-1, 25).displayOnlyValue().decimalsToShow(1).create(0, 0, 180, 20, Component.translatable("custommachinerypnc.gui.creation.components.pressure.danger")));
            this.baseTemplate().ifPresentOrElse(template -> this.danger.setValue(template.danger()), () -> this.danger.setValue(3.0f));
            this.danger.setTooltip(Tooltip.create(Component.translatable("custommachinerypnc.gui.creation.components.pressure.danger.tooltip")));

            //Max output
            this.critical = this.propertyList.add(Component.translatable("custommachinerypnc.gui.creation.components.pressure.critical"), new FloatSlider.Builder().bounds(-1, 25).displayOnlyValue().decimalsToShow(1).create(0, 0, 180, 20, Component.translatable("custommachinerypnc.gui.creation.components.pressure.critical")));
            this.baseTemplate().ifPresentOrElse(template -> this.critical.setValue(template.critical()), () -> this.critical.setValue(5.0f));
            this.critical.setTooltip(Tooltip.create(Component.translatable("custommachinerypnc.gui.creation.components.pressure.critical.tooltip")));

            //Config
            this.baseTemplate().ifPresentOrElse(template -> this.config = template.config(), () -> this.config = IOSideConfig.Template.DEFAULT_ALL_INPUT);
            this.propertyList.add(Component.translatable("custommachinery.gui.config.component"), ComponentConfigBuilderWidget.make(0, 0, 180, 20, Component.translatable("custommachinery.gui.config.component"), this.parent, () -> this.config, template -> this.config = template));
        }
    }
}
