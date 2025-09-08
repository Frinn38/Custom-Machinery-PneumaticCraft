package fr.frinn.custommachinerypnc.client.creation.component;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentConfigBuilderWidget;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.DoubleSlider;
import fr.frinn.custommachinery.impl.component.config.ToggleSideConfig;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent;
import fr.frinn.custommachinerypnc.common.component.HeatMachineComponent.Template;
import me.desht.pneumaticcraft.common.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HeatComponentBuilder implements IMachineComponentBuilder<HeatMachineComponent, Template> {

    @Override
    public MachineComponentType<HeatMachineComponent> type() {
        return Registration.HEAT_COMPONENT.get();
    }

    @Override
    public PopupScreen makePopup(MachineEditScreen parent, @Nullable Template template, Consumer<Template> onFinish) {
        return new HeatComponentBuilderPopup(parent, template, onFinish);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, Template template) {
        graphics.renderFakeItem(ModBlocks.VORTEX_TUBE.toStack(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
    }

    public static class HeatComponentBuilderPopup extends ComponentBuilderPopup<Template> {

        private DoubleSlider capacity;
        private DoubleSlider resistance;
        private ToggleSideConfig.Template config;

        public HeatComponentBuilderPopup(BaseScreen parent, @Nullable Template template, Consumer<Template> onFinish) {
            super(parent, template, onFinish, Component.translatable("custommachinerypnc.gui.creation.components.pressure.title"));
        }

        @Override
        public Template makeTemplate() {
            return new Template(this.capacity.doubleValue(), this.resistance.doubleValue(), this.config);
        }

        @Override
        protected void init() {
            super.init();

            //Capacity
            this.capacity = this.propertyList.add(Component.translatable("custommachinerypnc.gui.creation.components.heat.capacity"), DoubleSlider.builder()
                    .bounds(0, 200)
                    .displayOnlyValue()
                    .defaultValue(this.baseTemplate().map(Template::capacity).orElse(1.0D))
                    .create(0, 0, 180, 20, Component.translatable("custommachinerypnc.gui.creation.components.heat.capacity")));
            this.capacity.setTooltip(Tooltip.create(Component.translatable("custommachinerypnc.gui.creation.components.heat.capacity.tooltip")));

            //Resistance
            this.resistance = this.propertyList.add(Component.translatable("custommachinerypnc.gui.creation.components.heat.resistance"), DoubleSlider.builder()
                    .bounds(0, 200)
                    .displayOnlyValue()
                    .defaultValue(this.baseTemplate().map(Template::resistance).orElse(1.0D))
                    .create(0, 0, 180, 20, Component.translatable("custommachinerypnc.gui.creation.components.heat.resistance")));
            this.resistance.setTooltip(Tooltip.create(Component.translatable("custommachinerypnc.gui.creation.components.heat.resistance.tooltip")));

            //Config
            this.baseTemplate().ifPresentOrElse(template -> this.config = template.config(), () -> this.config = ToggleSideConfig.Template.DEFAULT_ALL_ENABLED);
            this.propertyList.add(Component.translatable("custommachinery.gui.config.component"), ComponentConfigBuilderWidget.make(0, 0, 180, 20, Component.translatable("custommachinery.gui.config.component"), this.parent, () -> this.config, template -> this.config = template));
        }
    }
}
